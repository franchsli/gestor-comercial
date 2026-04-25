package gui;
import db.Gasto;

public class GastosPanel extends Panel {
    public GastosPanel(){
        this.modelo = new Gasto();
        cargarDatos();
    }
}
