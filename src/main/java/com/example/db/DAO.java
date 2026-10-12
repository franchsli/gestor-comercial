package com.example.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class DAO<T> {
    protected final String nombreTabla;

    protected DAO(String nombreTabla){
        this.nombreTabla = nombreTabla;
    }

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
     * Inserta los datos dados en la tabla y
     * devuelve el id generado.
     * @param sql La sentencia SQL.
     * @param params Los datos a insertar.
     * @return El id generado. -1 si no fue
     * generado.
     */
    public int insertarYObtenerId(String sql, Object... params){
        try {
            Connection conn = DBConnection.get();
            PreparedStatement pStatement = conn.prepareStatement(sql);
            asignar(pStatement, params);
            pStatement.execute();
            ResultSet keys = pStatement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new DBException(e);
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
        String sql = "SELECT * FROM " + nombreTabla + " WHERE " + columna + " = ?";

        List<T> registros = consultar(sql, valor);
        if (!registros.isEmpty()) {
            return registros.get(0);
        }
        else return null;
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
     * 
     * Lector de resultados de tipo 'R'
     * @param <R>
     */
    @FunctionalInterface
    protected interface Lector<R> {
        R leer(ResultSet resultSet) throws SQLException;
    }

    /**
     * Consulta un solo dato
     * de la sentencia sql ejecutada y lo retorna
     * en el tipo propuesto.
     * Este método debe usarse en sentencias
     * que devuelvan solo un dato.
     * @param <R> Resultado de tipo R.
     * @param sql La sentencia a ejecutar.
     * @param lector El lector del resultado.
     * @param params Parámetros para rellenar la
     * sentencia (si es necesario)
     * @return El resultado de tipo R
     */
    protected <R> R consultarUno(String sql, Lector<R> lector, Object... params) {
        try {
            Connection conn = DBConnection.get();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            asignar(preparedStatement, params);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? lector.leer(resultSet) : null;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Retorna la suma de los registros en la columna que cumplen la condicion dada.
     * @param columna El nombre de la columna a sumar.
     * @param condicion La condicion que deben cumplir los registros.
     * @param params Los parametros que rellenarán la condicion.
     * @return El valor total de sumar todos los registros que cumplen la condicion en la columna.
     */
    public int sumarColumna(String columna, String condicion, Object... params) {
        String sql = "SELECT SUM(" + columna + ") FROM " + nombreTabla;
        if (condicion != null && !condicion.isBlank()) {
            sql += " WHERE " + condicion;
        }
        // Implementa leer() del Lector para que devuelva entero
        int suma = consultarUno(sql, resultSet -> resultSet.getInt(1));
        return suma;

    }
}
