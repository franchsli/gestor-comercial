import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Main {
    public static void main(String[] args) {
        JTabbedPane pestañas = new JTabbedPane();
        ProductosPanel pestañaProductos = new ProductosPanel();
        // se debe reemplazar JPanel con la implementación completa
        pestañas.addTab("Productos", pestañaProductos);
        pestañas.addTab("Ventas", new JPanel());
        pestañas.addTab("Gastos", new JPanel());
        //pestañas.addTab("Reportes", new JPanel());
        Ventana ventana = new Ventana();
        ventana.add(pestañas);
        DBConnection.cerrar();
    }
}