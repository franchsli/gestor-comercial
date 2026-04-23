import java.util.List;
import java.util.Map;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Panel extends JPanel {
   Modelo modelo = new Modelo();
   JTable tabla;
   DefaultTableModel modeloTabla;

    Panel() {
        setLayout(new BorderLayout());
        modeloTabla = new DefaultTableModel();
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    void cargarDatos() {
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(0);

        List<Map<String, String>> filas = modelo.todos();
        if (filas.isEmpty()) return;

        // columnas desde la primera fila
        filas.get(0).keySet().forEach(modeloTabla::addColumn);

        // filas
        for (Map<String, String> fila : filas) {
            modeloTabla.addRow(fila.values().toArray());
        }
    }
}
