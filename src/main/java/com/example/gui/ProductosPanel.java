package com.example.gui;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.GridLayout;
import java.util.List;

import com.example.controller.ProductosControlador;
import com.example.db.DBException;
import com.example.model.Producto;

public class ProductosPanel extends PanelCrud<Producto> {
    private final ProductosControlador productosControlador;

    public ProductosPanel(){
        this(new ProductosControlador());
    }

    private ProductosPanel(ProductosControlador productosControlador) {
        super(productosControlador);
        this.productosControlador = productosControlador;
        btnNuevo.addActionListener(e -> mostrarFormularioNuevo());
        campoBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { buscarProducto(); }
            public void removeUpdate(DocumentEvent e) { buscarProducto(); }
            public void changedUpdate(DocumentEvent e) {}
        });
        cargarDatos();
    }

    @Override 
    protected String[] columnas(){
        String[] columnas = {"id", "nombre", "precio_unitario", "cantidad"};
        return columnas;
    }

    @Override
    protected Object[] fila(Producto producto){
        Object[] fila = {producto.getId(), producto.getNombre(),
            producto.getPrecioUnitario(), producto.getCantidad()
        };
        return fila;
    }

    @Override
    public void mostrarFormularioNuevo() {
        JTextField nombre = new JTextField();
        JTextField precio = new JTextField();
        JTextField cantidad = new JTextField();

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Nombre:")); form.add(nombre);
        form.add(new JLabel("Precio unitario:")); form.add(precio);
        form.add(new JLabel("Cantidad:")); form.add(cantidad);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Nuevo producto", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && formularioEsValido(form)) {
            try {
                int precioProducto = Integer.parseInt(precio.getText());
                int cantidadProducto = Integer.parseInt(cantidad.getText());
                Producto nuevoProducto = new Producto(nombre.getText(), precioProducto, cantidadProducto);
                controladorCrud.insertar(nuevoProducto);
                cargarDatos();
            } catch (DBException e) {
                JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void aplicarCambios(String[] valores) {
        Producto producto = new Producto(
        Integer.parseInt(valores[0]),
        valores[1],
        Integer.parseInt(valores[2]),
        Integer.parseInt(valores[3]));
        controladorCrud.actualizar(producto);
    }

    /**
     * Busca en la base de datos los productos cuyo nombre contenga
     * lo escrito en el campoBuscar y lo muestra en la pestaña.
     */
    private void buscarProducto(){
        String nombreProducto = campoBuscar.getText().strip();
        List<Producto> productosCoincidentes = productosControlador.buscarPorNombre(nombreProducto);
        cargarDatos(productosCoincidentes);
    }

}
