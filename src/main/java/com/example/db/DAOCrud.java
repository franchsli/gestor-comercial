package com.example.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

public abstract class DAOCrud<T> extends DAOLectura<T> {
    protected final String columnas;

    protected DAOCrud(String nombreTabla, String columnas){
        super(nombreTabla);
        this.columnas = columnas;
    }

    /**
     * Actualiza la fila cuya clave primaria coincida con
     * la de la entidad. La fila es actualizada
     * con los datos de la entidad ingresada.
     */
    public abstract void actualizar(T entidad);

    /**
     * Inserta la entidad dada con sus valores en la tabla.
     * @param entidad La entidad a guardar en la tabla.
     */
    public abstract void insertar(T entidad);

    /**
     * Inserta los parametros dados en una nueva fila
     * en la tabla y retorna el id generado.
     * @param valores Los valores que debe tener cada
     * columna de la tabla.
     * @return El id generado.
     */
    public int insertarFila(Object... valores){
        String sql = "INSERT INTO " + nombreTabla + " " + columnas + " VALUES " + marcadores(valores.length);
        return insertarYObtenerId(sql, valores);
    }

    /**
     * Devuelve una String de la lista de marcadores para
     * una sentencia INSERT SQL.
     * Ej: (?, ?, ?)
     * @param numMarcadores El número de marcadores.
     * @return La lista de marcadores para la sentencia.
     */
    protected String marcadores(int numMarcadores){
        StringJoiner tupla = new StringJoiner(", ", "(", ")");
        for (int i = 0; i < numMarcadores; i++) {
            tupla.add("?");
        }
        return tupla.toString();
    }

    /**
     * Ejecuta la sentencia INSERT/UPDATE/DELETE en la base de
     * datos
     * @param sql La sentencia INSERT/UPDATE/DELETE de SQL
     * @param params Los parámetros necesarios.
     */
    public void ejecutar(String sql, Object... params) {
        Connection conn = DBConnection.get();
        try (PreparedStatement pStatement = conn.prepareStatement(sql)){
            asignar(pStatement, params);
            pStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * Inserta los datos dados en la tabla y
     * devuelve el id generado.
     * @param sql La sentencia SQL.
     * @param datos Los datos a insertar.
     * @return El id generado. -1 si no fue
     * generado.
     */
    public int insertarYObtenerId(String sql, Object... datos){
        Connection conn = DBConnection.get();
        try (PreparedStatement pStatement = conn.prepareStatement(sql)){
            asignar(pStatement, datos);
            pStatement.executeUpdate();
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

}
