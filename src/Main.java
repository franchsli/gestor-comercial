import javax.swing.JTabbedPane;

import db.DBConnection;
import gui.CierreDiarioPanel;
import gui.GastosPanel;
import gui.ProductosPanel;
import gui.Ventana;
import gui.VentasPanel;

public class Main {
    public static void main(String[] args) {
        JTabbedPane pestañas = new JTabbedPane();
        ProductosPanel pestañaProductos = new ProductosPanel();
        GastosPanel pestañaGastos = new GastosPanel();
        VentasPanel pestañaVentas = new VentasPanel();
        CierreDiarioPanel pestañaCierresDiarios = new CierreDiarioPanel();
        // se debe reemplazar JPanel con la implementación completa
        pestañas.addTab("Productos", pestañaProductos);
        pestañas.addTab("Ventas", pestañaVentas);
        pestañas.addTab("Gastos", pestañaGastos);
        pestañas.addTab("Cierres diarios", pestañaCierresDiarios);
        Ventana ventana = new Ventana();
        ventana.add(pestañas);
        DBConnection.cerrar();
    }
}