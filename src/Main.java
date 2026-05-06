import javax.swing.JTabbedPane;

import db.DBConnection;
import gui.CierreDiarioPanel;
import gui.GastosPanel;
import gui.PresupuestosPanel;
import gui.ProductosPanel;
import gui.Ventana;
import gui.VentasPanel;

public class Main {
    public static void main(String[] args) {
        JTabbedPane pestañas = new JTabbedPane();
        ProductosPanel pestañaProductos = new ProductosPanel();
        GastosPanel pestañaGastos = new GastosPanel();
        VentasPanel pestañaVentas = new VentasPanel();
        PresupuestosPanel pestañaPresupuestos = new PresupuestosPanel();
        CierreDiarioPanel pestañaCierresDiarios = new CierreDiarioPanel();
        // se debe reemplazar JPanel con la implementación completa
        pestañas.addTab("Productos", pestañaProductos);
        pestañas.addTab("Ventas", pestañaVentas);
        pestañas.addTab("Gastos", pestañaGastos);
        pestañas.addTab("Presupuestos", pestañaPresupuestos);
        pestañas.addTab("Cierres diarios", pestañaCierresDiarios);
        Ventana ventana = new Ventana();
        ventana.add(pestañas);
        DBConnection.cerrar();
    }
}