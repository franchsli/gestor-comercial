import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Main {
    public static void main(String[] args) {
        Ventana ventana = new Ventana();
        JTabbedPane pestañas = new JTabbedPane();
        // se debe reemplazar JPanel con la implementación completa
        pestañas.addTab("Productos", new JPanel());
        pestañas.addTab("Ventas", new JPanel());
        pestañas.addTab("Gastos", new JPanel());
        //pestañas.addTab("Reportes", new JPanel());
        ventana.add(pestañas);
        DBConnection.cerrar();
    }
}