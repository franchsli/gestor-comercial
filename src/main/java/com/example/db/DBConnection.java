package com.example.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection = null;
    
    /**
     * Establece una conexión con la base de datos y la devuelve.
     * @return La conexión con la base de datos.
     * @throws SQLException Error si la conexión no fue posible.
     */
    public static Connection get() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:sqlite:bd_de_prueba.db");
            // activa el soporte para que las claves foráneas funcionen
            PreparedStatement pStatement = connection.prepareStatement("PRAGMA foreign_keys = ON");
            pStatement.execute();
        }
        return connection;
    }

    /** Cierra la conexión a la base de datos 
    */
    public static void cerrar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
