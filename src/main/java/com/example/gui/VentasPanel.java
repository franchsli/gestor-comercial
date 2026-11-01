package com.example.gui;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.time.LocalDate;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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
        btnEliminar.setText("Anular");
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

        // formulario completo
        JPanel form = new JPanel(new BorderLayout(5, 5));
        JPanel camposFijos = new JPanel(new GridLayout(3, 2, 5, 5));
        camposFijos.add(new JLabel("Fecha (YYYY-MM-DD):")); 
        camposFijos.add(campoFecha);
        camposFijos.add(new JLabel("Tipo:")); 
        camposFijos.add(tipo);

        form.add(camposFijos, BorderLayout.NORTH);
        form.add(new JLabel("Productos:"), BorderLayout.CENTER);
        form.add(panelProductos, BorderLayout.SOUTH);
        form.setPreferredSize(new Dimension(450, 250));

        int result = JOptionPane.showConfirmDialog(this, form,
            "Nueva venta", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && formularioEsValido(form)) {
            try {
                // crear la venta primero (valor se calculará en vender())
                Venta.Tipo tipoSeleccionado = (Venta.Tipo) tipo.getSelectedItem();

                Venta nuevaVenta = new Venta(fechaDeCampo(campoFecha), 0, tipoSeleccionado);

                ventasControlador.insertar(nuevaVenta);

                // iterar filas de productos
                for (Component c : panelProductos.getComponents()) {
                    JPanel fila = (JPanel) c;
                    JComboBox<?> combo = (JComboBox<?>) fila.getComponent(0);
                    JTextField campoCantidad = (JTextField) fila.getComponent(1);

                    Producto producto = (Producto) combo.getSelectedItem();
                    String cantidadStr = campoCantidad.getText().trim();
                    if (cantidadStr.isEmpty()) continue;

                    ventasControlador.vender(nuevaVenta, producto, Integer.parseInt(cantidadStr));
                }

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
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JComboBox<Producto> combo = new JComboBox<>(productos.toArray(Producto[]::new));
        JTextField cantidad = new JTextField(5);
        cantidad.setToolTipText("Cantidad");

        cantidad.addActionListener(e -> {
            // Si se presiona Enter, se añade una fila
            panelProductos.add(crearFilaProducto(productos, panelProductos));
            panelProductos.revalidate();
            panelProductos.repaint();
        });

        fila.add(combo);
        fila.add(cantidad);
        return fila;
    }
}
