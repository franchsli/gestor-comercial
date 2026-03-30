import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.sqlite.core.DB;

import java.sql.PreparedStatement;

public class Modelo {
    //protected String nombreTabla = "GASTOS";
    protected String nombreTabla = this.getClass().getSimpleName();

    // TODO: Añadir más métodos GENERALES
    // EJ: Borrar todos, borrar segun condición
    // Insertar, actualizar segun condición
    // seleccionar solo un dato (lo dudo pero bueno)
    // contar todo o solo lo que cumpla una condición

    /**
     * Devuelve todos los datos de la tabla.
     * @return Todos los datos de la tabla.
     */
    public List<Map<String, String>> todos(){
        return todos("");
    }

    /**
     * Devuelve todos los datos de la tabla que cumplen
     * la condición dada.
     * @param condicion La condición que los datos deben cumplir.
     * @return Los datos que cumplen con la condición.
     */
    public List<Map<String, String>> todos(String condicion){
        String sql = "SELECT * FROM " + nombreTabla;
        if (!condicion.isEmpty()) {
            sql += "WHERE " + condicion;
        }
        List<Map<String, String>> resultados = new ArrayList<>();
        
        try {
            ResultSet resultSet = DBConnection.consultar(sql);
            ResultSetMetaData rSetMetaData = resultSet.getMetaData();
            int columnas = rSetMetaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, String> fila = new LinkedHashMap<>();
                for (int index = 1; index <= columnas; index++) {
                    // guarda los datos de la fila
                    // "columna" : "dato"
                    fila.put(rSetMetaData.getColumnName(index), resultSet.getString(index));
                }
                // guarda los datos obtenidos de la fila en la lista
                resultados.add(fila);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return resultados;
    }

    public static void main(String[] args) {
        Modelo model = new Modelo();
        List<Map<String, String>> resultados = model.todos();
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
    }

}
