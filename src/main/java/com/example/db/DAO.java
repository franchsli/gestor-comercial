package com.example.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class DAO<T> {
    String nombreTabla = this.getClass().getSimpleName().toUpperCase() + "S";

    public void mapear(ResultSet resultSet){}

    /**
     * Inserta los datos dados en la tabla.
     * Lo implementan las clases hijas.
     */
    public abstract void insertar(T entidad);

    /**
     * Actualiza los registros que cumplan con la condición dada
     * con los datos ingresados.
     */
    public abstract void actualizar(T entidad);

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
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return resultados;
    }

    /**
     * Devuelve el único registro de la tabla cuya columna tiene el valor dado.
     * Este método debe usarse en columnas cuyo valores sean únicos.
     * @param columna La columna a la que se le va a revisar el valor.
     * @param valor El valor que debe tener la columna.
     * @return El único registro de la tabla cuya columna tiene el valor dado.
     */
    public Map<String, String> unicoRegistro(String columna, String valor){
        String sql = "SELECT * FROM " + nombreTabla + " WHERE " + columna + "=" + valor;
        try {
            ResultSet resultSet = DBConnection.consultar(sql);
            if (resultSet.next()) {
                ResultSetMetaData rSetMetaData = resultSet.getMetaData();
                int columnas = rSetMetaData.getColumnCount();
                Map<String, String> registro = new LinkedHashMap<>();
                for (int index = 1; index <= columnas; index++) {
                    // guarda los datos del registro (fila)
                    // "columna" : "dato"
                    registro.put(rSetMetaData.getColumnName(index), resultSet.getString(index));
                }
                return registro;
            }
            else return null;
            
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Devuelve el valor (String) de una columna del registro con el id dado.
     * @param nombreColumna La columna de la cual se va a sacar el valor
     * @param id El id del registro en la tabla.
     * @return El valor String de la columna.
     */
    public String stringColumna(String nombreColumna, String id){
        String sql = "SELECT " + nombreColumna + " FROM " + nombreTabla + " WHERE id=" + id;
        try {
            ResultSet resultSet = DBConnection.consultar(sql);
            if (resultSet.next()) {
                return resultSet.getString(nombreColumna);
            }
            return null;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Devuelve el valor (int) de una columna del registro con el id dado.
     * @param nombreColumna La columna de la cual se va a sacar el valor
     * @param id El id del registro en la tabla.
     * @return El valor entero de la columna.
     */
    public int intColumna(String nombreColumna, String id){
        String sql = "SELECT " + nombreColumna + " FROM " + nombreTabla + " WHERE id=" + id;
        try {
            ResultSet resultSet = DBConnection.consultar(sql);
            if (resultSet.next()) {
                return resultSet.getInt(nombreColumna);
            }
            return 0;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Devuelve el valor (double) de una columna del registro con el id dado.
     * @param nombreColumna La columna de la cual se va a sacar el valor
     * @param id El id del registro en la tabla.
     * @return El valor flotante (double) de la columna.
     */
    public double doubleColumna(String nombreColumna, String id){
        String sql = "SELECT " + nombreColumna + " FROM " + nombreTabla + " WHERE id=" + id;
        try {
            ResultSet resultSet = DBConnection.consultar(sql);
            if (resultSet.next()) {
                return resultSet.getDouble(nombreColumna);
            }
            return 0.0;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Borra todos los datos de una tabla que
     * cumplan la condición dada.
     * @param condicion La condición que deben
     * cumplir los datos para ser borrados.
     */
    public void borrarTodos(String condicion){
        String sql = "DELETE FROM " + nombreTabla;
        if (!condicion.isEmpty()) {
            sql += " WHERE " + condicion;
        }
        DBConnection.ejecutar(sql);
    }

    /**
     * Devuelve el ultimo id en la tabla.
     * @return El ultimo id en la tabla.
     */
    public String ultimoId(){
        String sql = "SELECT MAX(id) FROM " + nombreTabla;
        try {
            ResultSet resultSet = DBConnection.consultar(sql);
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return null;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Retorna la suma de los registros en la columna que cumplen la condicion dada.
     * @param columna El nombre de la columna a sumar.
     * @param condicion La condicion que deben cumplir los registros.
     * @return El valor total de sumar todos los registros que cumplen la condicion en la columna.
     */
    public int sumarColumna(String columna, String condicion) {
        String sql = "SELECT SUM(" + columna + ") FROM " + nombreTabla;
        if (condicion != null && !condicion.isEmpty()) {
            sql += " WHERE " + condicion;
        }
        try {
            ResultSet rs = DBConnection.consultar(sql);
            if (rs.next()) return rs.getInt(1);
            return 0;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
