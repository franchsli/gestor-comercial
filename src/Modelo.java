import java.lang.reflect.AnnotatedType;
import java.security.KeyStore.Entry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sqlite.core.DB;

import java.sql.PreparedStatement;

public class Modelo {
    //protected String nombreTabla = "GASTOS";
    protected String nombreTabla = this.getClass().getSimpleName().toUpperCase() + "S";

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

    /**
     * Borra todos los datos de una tabla.
     */
    void borrarTodos(){
        borrarTodos("");
    }

    /**
     * Borra todos los datos de una tabla que
     * cumplan la condición dada.
     * @param condicion La condición que deben
     * cumplir los datos para ser borrados.
     */
    void borrarTodos(String condicion){
        String sql = "DELETE * FROM " + nombreTabla;
        if (!condicion.isEmpty()) {
            sql += "WHERE " + condicion;
        }
        DBConnection.ejecutar(sql);
    }

    /**
     * Inserta los datos dados en la tabla.
     * @param datos Los datos a insertar. 
     * Deben ir entre parentesis y separados por comas
     * Ej: "(1,2,3,4,5,'texto')"
     */
    void insertar(String datos){
        String sql = "INSERT INTO " + nombreTabla + " VALUES " + datos;
        DBConnection.ejecutar(sql);
    }

    /**
     * Actualiza los registros que cumplan con la condición dada
     * con los datos ingresados.
     * @param datos Los nuevos datos para los registros.
     * Ej: "columna":"valor", "otraColumna": 1
     * @param condicion
     */
    void actualizar(LinkedHashMap<String, String> datos, String condicion){
        String sql = "UPDATE " + nombreTabla + " SET ";
        Iterator<Map.Entry<String, String>> iterator = datos.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> dato = iterator.next();
            String columna = dato.getKey();
            String valor = dato.getValue();
            sql += " " + columna + "=" + valor + ",";
        }
        Map.Entry<String, String> ultimoDato = datos.lastEntry();
        String columna = ultimoDato.getKey();
        String valor = ultimoDato.getValue();
        sql += " " + columna + "=" + valor;
        sql += " WHERE " + condicion;

        DBConnection.ejecutar(sql);
    }

    /**
     * Devuelve el número de filas en la tabla.
     * @return El número de filas en la tabla.
     */
    int contarTodos(){
        return contarTodos("");
    }

    /**
     * Devuelve el número de filas en la tabla
     * que cumplen la condición dada.
     * @param condicion La condición que deben cumplir las filas.
     * @return El número de filas que cumplen la condición.
     */
    int contarTodos(String condicion){
        String sql = "SELECT COUNT(*) FROM " + nombreTabla;
        if (!condicion.isEmpty()) {
            sql += " WHERE " + condicion;
        }
        try {
            ResultSet resultSet = DBConnection.consultar(sql);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public static void main(String[] args) {
        Modelo model = new Modelo();
        List<Map<String, String>> resultados = model.todos();
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
    }

}
