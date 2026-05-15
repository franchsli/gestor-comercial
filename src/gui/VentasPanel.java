package gui;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import db.Producto;
import db.Venta;

public class VentasPanel extends Panel {
    private Venta ventas = new Venta();
    private Producto productos = new Producto();
    
    public VentasPanel(){
        this.modelo = ventas;
        btnNuevo.setText("+ Nueva venta");
        btnEliminar.setText("Anular");
        this.campoBuscar.setVisible(false);
        btnNuevo.addActionListener(e -> mostrarFormularioNuevo());
        cargarDatos();
    }

    @Override
    public void mostrarFormularioNuevo() {
        String[] estados = {"PENDIENTE", "FINALIZADA", "CANCELADA"};
        JComboBox<String> estado = new JComboBox<>(estados);
        String[] tipos = {"EFECTIVO", "CREDITO"};
        JComboBox<String> tipo = new JComboBox<>(tipos);


        // panel dinámico de productos
        JPanel panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));

        // cargar nombres de productos
        List<Map<String, String>> listaProductos = productos.todos();
        String[] nombresProductos = listaProductos.stream()
            .map(p -> p.get("nombre"))
            .toArray(String[]::new);

        // primera fila de producto
        panelProductos.add(crearFilaProducto(nombresProductos, panelProductos));

        // formulario completo
        JPanel form = new JPanel(new BorderLayout(5, 5));
        JPanel camposFijos = new JPanel(new GridLayout(3, 2, 5, 5));
        camposFijos.add(new JLabel("Fecha (YYYY-MM-DD):")); 
        camposFijos.add(campoFecha);
        camposFijos.add(new JLabel("Estado:")); 
        camposFijos.add(estado);
        camposFijos.add(new JLabel("Tipo:")); 
        camposFijos.add(tipo);

        form.add(camposFijos, BorderLayout.NORTH);
        form.add(new JLabel("Productos:"), BorderLayout.CENTER);
        form.add(panelProductos, BorderLayout.SOUTH);
        form.setPreferredSize(new Dimension(450, 250));

        int result = JOptionPane.showConfirmDialog(this, form,
            "Nueva venta", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && formularioEsValido(form)) {
            // crear la venta primero (valor se calculará en vender())
            String fechaStr = fechaATexto(editorFecha, campoFecha);
            ventas.crear(fechaStr, estado.getSelectedItem().toString(), "0", tipo.getSelectedItem().toString());

            // obtener el id de la venta recién creada
            String ventaId = ventas.ultimoId();

            // iterar filas de productos
            for (Component c : panelProductos.getComponents()) {
                JPanel fila = (JPanel) c;
                JComboBox<?> combo = (JComboBox<?>) fila.getComponent(0);
                JTextField campoCantidad = (JTextField) fila.getComponent(1);

                String nombreProducto = combo.getSelectedItem().toString();
                String cantidadStr = campoCantidad.getText().trim();
                if (cantidadStr.isEmpty()) continue;

                ventas.vender(ventaId, nombreProducto, cantidadStr);
            }

            cargarDatos();
        }
    }

    /**
     * Crea una nueva fila de producto para añadir un producto y darle una cantidad.
     * @param nombres Los nombres de los productos en la base de datos.
     * @param panelProductos El panel que contiene los productos y su cantidad.
     * @return Un panel con la nueva fila creada.
     */
    private JPanel crearFilaProducto(String[] nombres, JPanel panelProductos) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> combo = new JComboBox<>(nombres);
        JTextField cantidad = new JTextField(5);
        cantidad.setToolTipText("Cantidad");

        cantidad.addActionListener(e -> {
            // Si se presiona Enter, se añade una fila
            panelProductos.add(crearFilaProducto(nombres, panelProductos));
            panelProductos.revalidate();
            panelProductos.repaint();
        });

        fila.add(combo);
        fila.add(cantidad);
        return fila;
    }
}
