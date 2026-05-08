package gui;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.GridLayout;

import db.Producto;

public class ProductosPanel extends Panel {
    Producto productos = new Producto();

    public ProductosPanel() {
        this.modelo = productos;
        btnNuevo.addActionListener(e -> mostrarFormularioNuevo());
        campoBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { buscarProducto(); }
            public void removeUpdate(DocumentEvent e) { buscarProducto(); }
            public void changedUpdate(DocumentEvent e) {}
        });
        cargarDatos();
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
            productos.crear(nombre.getText(), precio.getText(), cantidad.getText());
            cargarDatos();
        }
    }

    private void buscarProducto(){
        String nombreProducto = campoBuscar.getText().strip();
        String condicion = "nombre LIKE '%" + nombreProducto + "%'";
        cargarDatos(condicion);
    }

}