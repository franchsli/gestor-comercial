package gui;
import db.Producto;

public class ProductosPanel extends Panel {

    public ProductosPanel() {
        this.modelo = new Producto();
        cargarDatos();
    }


}