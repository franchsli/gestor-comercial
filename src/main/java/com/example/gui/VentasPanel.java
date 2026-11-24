package com.example.gui;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;
import java.time.LocalDate;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import com.example.controller.ProductosControlador;
import com.example.controller.VentasControlador;
import com.example.db.DBException;
import com.example.model.Producto;
import com.example.model.Venta;

public class VentasPanel extends PanelCrud<Venta> {
    private final VentasControlador ventasControlador;
    private final ProductosControlador productosControlador;
    
    public VentasPanel(){
        this(new VentasControlador(), new ProductosControlador());
    }

    private VentasPanel(VentasControlador ventasControlador, ProductosControlador productosControlador){
        super(ventasControlador);
        this.ventasControlador = ventasControlador;
        this.productosControlador = productosControlador;
        btnNuevo.setText("+ Nueva venta");
        this.campoBuscar.setVisible(false);
        btnNuevo.addActionListener(e -> mostrarFormularioNuevo());
        cargarDatos();
    }

    @Override 
    protected String[] columnas(){
        String[] columnas = {"id", "fecha", "valor", "tipo"};
        return columnas;
    }

    @Override
    protected Object[] fila(Venta venta){
        Object[] fila = {venta.getId(), venta.getFecha(),
                        venta.getValor(), venta.getTipo()};
        return fila;
    }

    @Override
    public void mostrarFormularioNuevo() {
        Venta.Tipo[] tipos = Venta.Tipo.values();
        JComboBox<Venta.Tipo> tipo = new JComboBox<>(tipos);

        // panel dinámico de productos
        JPanel panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));

        // cargar productos
        List<Producto> productos = productosControlador.todos();

        // primera fila de producto
        panelProductos.add(crearFilaProducto(productos, panelProductos));

        // scroll de productos
        JScrollPane scroll = new JScrollPane(panelProductos,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(360, 125));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        // campos fijos
        JPanel camposFijos = new JPanel(new GridLayout(2, 2, 5, 5));
        camposFijos.add(new JLabel("Fecha (YYYY-MM-DD):")); 
        camposFijos.add(campoFecha);
        camposFijos.add(new JLabel("Tipo:")); 
        camposFijos.add(tipo);

        // zona de productos: etiqueta arriba, scroll debajo
        JPanel zonaProductos = new JPanel(new BorderLayout(5, 5));
        zonaProductos.add(new JLabel("Productos:"), BorderLayout.NORTH);
        zonaProductos.add(scroll, BorderLayout.WEST);

        // formulario completo
        JPanel form = new JPanel(new BorderLayout(5, 10));
        form.add(camposFijos, BorderLayout.NORTH);
        form.add(zonaProductos, BorderLayout.CENTER);
        form.setPreferredSize(new Dimension(450, 250));

        int result = JOptionPane.showConfirmDialog(this, form,
            "Nueva venta", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && formularioEsValido(form)) {
            try {
                Venta.Tipo tipoSeleccionado = (Venta.Tipo) tipo.getSelectedItem();

                Venta nuevaVenta = new Venta(fechaDeCampo(campoFecha), 0, tipoSeleccionado);

                ArrayList<Producto> productosEnVenta = new ArrayList<>();
                ArrayList<Integer> cantidadesPorProductoAVender = new ArrayList<>();

                // iterar filas de productos
                for (Component c : panelProductos.getComponents()) {
                    JPanel fila = (JPanel) c;
                    JComboBox<?> combo = (JComboBox<?>) fila.getComponent(0);
                    JTextField campoCantidad = (JTextField) fila.getComponent(2);

                    Producto producto = (Producto) combo.getSelectedItem();
                    String cantidadStr = campoCantidad.getText().trim();
                    if (cantidadStr.isEmpty()) continue;

                    productosEnVenta.add(producto);
                    cantidadesPorProductoAVender.add(Integer.parseInt(cantidadStr));
                }

                ventasControlador.venderProductos(nuevaVenta, productosEnVenta, cantidadesPorProductoAVender);

                cargarDatos();
            } catch (DBException e) {
                mostrarError("Error al guardar venta: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                mostrarError(e.getMessage());
            }
        }
    }

    @Override
    public void aplicarCambios(String[] valores){
        int id = Integer.parseInt(valores[0]);
        LocalDate fecha = fechaDeTexto(valores[1]);
        int valor = Integer.parseInt(valores[2]);
        Venta.Tipo tipo = Venta.Tipo.valueOf(valores[3]);
        Venta venta = new Venta(id, fecha, valor, tipo);
        controladorCrud.actualizar(venta);
    }

    /**
     * Crea una nueva fila de producto para añadir un producto y darle una cantidad.
     * @param productos Lista de los productos en la base de datos.
     * @param panelProductos El panel que contiene los productos y su cantidad.
     * @return Un panel con la nueva fila creada.
     */
    private JPanel crearFilaProducto(List<Producto> productos, JPanel panelProductos) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        
        JComboBox<Producto> combo = new JComboBox<>(productos.toArray(Producto[]::new));
        combo.setPreferredSize(new Dimension(270, combo.getPreferredSize().height));
        JTextField cantidad = new JTextField(5);
        cantidad.setToolTipText("Cantidad");

        cantidad.addActionListener(e -> {
            // Si se presiona Enter, se añade una fila
            panelProductos.add(crearFilaProducto(productos, panelProductos));
            panelProductos.revalidate();
            panelProductos.repaint();

            // Hace scroll a la fila agregada
            SwingUtilities.invokeLater(() -> {
                JScrollPane scrollPaneEnPanel = (JScrollPane) SwingUtilities.getAncestorOfClass(
                    JScrollPane.class, panelProductos);
                    if (scrollPaneEnPanel != null) {
                        JScrollBar scrollBar = scrollPaneEnPanel.getVerticalScrollBar();
                        scrollBar.setValue(scrollBar.getMaximum());
                    }
            });
        });

        JButton btnQuitar = new JButton("Eliminar");
        btnQuitar.addActionListener(e -> {
            if (panelProductos.getComponentCount() <= 1) {
                mostrarAviso("La venta debe tener al menos un producto.");
                return;
            }
            panelProductos.remove(fila);
            panelProductos.revalidate();
            panelProductos.repaint();
        });
        

        fila.add(combo);
        fila.add(Box.createHorizontalStrut(5)); // separación entre combo y cantidad
        fila.add(cantidad);
        fila.add(Box.createHorizontalStrut(5)); // separación entre cantidad y botón para eliminar
        fila.add(btnQuitar);

        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, fila.getPreferredSize().height));
        return fila;
    }
}
