package com.example.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class DAO<T> {
    String nombreTabla;

    public abstract T mapear(ResultSet resultSet);

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
     * Ejecuta la sentencia INSERT/UPDATE/DELETE en la base de
     * datos
     * @param sql La sentencia INSERT/UPDATE/DELETE de SQL
     * @param params Los parámetros necesarios.
     */
    public void ejecutar(String sql, Object... params) {
        try {
            Connection conn = DBConnection.get();
            PreparedStatement pStatement = conn.prepareStatement(sql);
            asignar(pStatement, params);
            pStatement.execute();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Consulta a la base de datos mediante una sentencia SELECT
     * y devuelve lo hallado
     * @param sql La sentencia SELECT de SQL
     * @return El resultado de la consulta
     */
    public List<T> consultar(String sql, Object... params) {
        List<T> resultados = new ArrayList<>();
        try {
            Connection conn = DBConnection.get();
            PreparedStatement pStatement = conn.prepareStatement(sql);
            asignar(pStatement, params);
            ResultSet resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                resultados.add(mapear(resultSet));
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return resultados;
    }

    /**
     * Asigna los valores dados en la sentencia SQL dada.
     * @param preparedStatement La sentencia SQL.
     * @param params Los valores a llenar en la sentencia.
     * @throws SQLException
     */
    protected void asignar(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof LocalDate) {
                param = param.toString();
            }
            preparedStatement.setObject(i + 1, param);
        }
    }

    /**
     * Devuelve todos los datos de la tabla.
     * @return Todos los datos de la tabla.
     */
    public List<T> todos(){
        return todos("");
    }

    /**
     * Devuelve todos los datos de la tabla que cumplen
     * la condición dada.
     * @param condicion La condición que los datos deben cumplir.
     * @return Los datos que cumplen con la condición.
     */
    public List<T> todos(String condicion, Object... params){
        String sql = "SELECT * FROM " + nombreTabla;
        if (condicion != null && !condicion.isBlank()) {
            sql += " WHERE " + condicion;
        }
        return consultar(sql, params);
    }

    /**
     * Devuelve el único registro de la tabla cuya columna tiene el valor dado.
     * Este método debe usarse en columnas cuyo valores sean únicos.
     * @param columna La columna a la que se le va a revisar el valor.
     * @param valor El valor que debe tener la columna.
     * @return El único registro de la tabla cuya columna tiene el valor dado.
     */
    public T unicoRegistro(String columna, String valor){
        String sql = "SELECT * FROM " + nombreTabla + " WHERE " + columna + "=" + valor;
        try {
            ResultSet resultSet = consultar(sql);
            if (resultSet.next()) {
                return mapear(resultSet);
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
            ResultSet resultSet = consultar(sql);
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
            ResultSet resultSet = consultar(sql);
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
            ResultSet resultSet = consultar(sql);
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
     * @param params Los parámetros necesarios
     * para rellenar la condición propuesta.
     */
    public void borrarTodos(String condicion, Object... params){
        String sql = "DELETE FROM " + nombreTabla;
        if (condicion != null && !condicion.isBlank()) {
            sql += " WHERE " + condicion;
        }
        ejecutar(sql, params);
    }

    /**
     * Devuelve el ultimo id en la tabla.
     * @return El ultimo id en la tabla.
     */
    public String ultimoId(){
        String sql = "SELECT MAX(id) FROM " + nombreTabla;
        try {
            ResultSet resultSet = consultar(sql);
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
            ResultSet rs = consultar(sql);
            if (rs.next()) return rs.getInt(1);
            return 0;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
