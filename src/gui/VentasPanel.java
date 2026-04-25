package gui;
import db.Venta;

public class VentasPanel extends Panel{
    public VentasPanel(){
        this.modelo = new Venta();
        cargarDatos();
    }
    
}
