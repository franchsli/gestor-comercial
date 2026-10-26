package com.example.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    public static final String URL_POR_DEFECTO = "jdbc:sqlite:bd_de_prueba.db";

    private static String url = URL_POR_DEFECTO;
    private static Connection connection = null;
    
    /**
     * Establece una conexión con la base de datos y la devuelve.
     * @return La conexión con la base de datos.
     */
    public static Connection get() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url);
                // activa el soporte para que las claves foráneas funcionen
                PreparedStatement pStatement = connection.prepareStatement("PRAGMA foreign_keys = ON");
                pStatement.execute();
            }
            return connection; 
        } catch (SQLException e) {
            throw new DBException(e);
        }

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

    /**
     * Cambia la URL de la base de datos. Cierra la conexión actual (si existe)
     * para que la próxima llamada a get() se conecte a la nueva URL.
     * @param nuevaUrl URL JDBC de la nueva base de datos.
     */
    public static void setUrl(String nuevaUrl) {
        cerrar();
        url = nuevaUrl;
    }
}
