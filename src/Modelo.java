import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Modelo {
    protected String nombreTabla = "GASTOS";
    //protected String nombreTabla = this.getClass().getSimpleName().toUpperCase() + "S";

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
            sql += " WHERE " + condicion;
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
            sql += " WHERE " + condicion;
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

        if (datos.size() > 1) {
            StringJoiner stringJoiner = new StringJoiner(", ");
            for (Map.Entry<String, String> dato : datos.entrySet()) {
                String textoDato = dato.getKey() + "=" + formatearValor(dato.getValue());
                stringJoiner.add(textoDato);
            }
            sql += stringJoiner.toString();
        }
        else{
            Map.Entry<String, String> unicoDato = datos.firstEntry();
            String columna = unicoDato.getKey();
            String valor = formatearValor(unicoDato.getValue());
            sql += " " + columna + "=" + valor;
        }
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

    /**
     * Formatea una String para darle comillas si
     * es alfanumérica o dejarla igual si es
     * numérica.
     * @param valor El valor a formatear.
     * @return El valor formateado.
     */
    private String formatearValor(String valor){
        try {
            Double.parseDouble(valor);
            return valor;
        } catch (NumberFormatException e) {
            return "'" + valor + "'";
        }
    }

    public static void main(String[] args) {
        Modelo model = new Modelo();
        // muestra todos los registros
        List<Map<String, String>> resultados = model.todos();
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("----------");
        // cuenta todos los registros
        int contador = model.contarTodos();
        System.out.println(contador);
        System.out.println("----------");
        // cuenta todos los registros segun condición
        int contadorCondicionado = model.contarTodos("estado='PENDIENTE'");
        System.out.println(contadorCondicionado);
        System.out.println("----------");
        // inserta un nuevo registro
        model.insertar("(null, '2026-02-25', 'PAGADO', 50000)");
        // verifica que esté el nuevo registro
        resultados = model.todos();
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("----------");
        // actualiza un registro (en este caso, el último)
        LinkedHashMap<String, String> nuevosDatos = new LinkedHashMap<>();
        nuevosDatos.put("estado", "PENDIENTE");
        model.actualizar(nuevosDatos, "id=11");
        // verifica que el nuevo registro está actualizado
        resultados = model.todos("id=11");
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("----------");
        // elimina todos los registros según una condición
        model.borrarTodos("estado='PAGADO'");
        // verifica que se hayan borrado
        resultados = model.todos();
        for (Map<String,String> map : resultados) {
            System.out.println(map);
        }
        System.out.println("----------");
        // elimina todos los registros
        model.borrarTodos();
        // confirma que todo se ha borrado al ver 0
        model.contarTodos();
        
    }

}
