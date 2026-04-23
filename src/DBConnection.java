import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection = null;
    
    /**
     * Establece una conexión con la base de datos y la devuelve.
     * @return La conexión con la base de datos.
     * @throws SQLException Error si la conexión no fue posible.
     */
    static Connection get() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:sqlite:bd_de_prueba.db");
            // activa el soporte para que las claves foráneas funcionen
            PreparedStatement pStatement = connection.prepareStatement("PRAGMA foreign_keys = ON");
            pStatement.execute();
        }
        return connection;
    }

    /**
     * Ejecuta la sentencia INSERT/UPDATE/DELETE en la base de
     * datos
     * @param sql La sentencia INSERT/UPDATE/DELETE de SQL
     */
    static void ejecutar(String sql) {
        try {
            Connection conn = DBConnection.get();
            PreparedStatement pStatement = conn.prepareStatement(sql);
            pStatement.execute();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Consulta a la base de datos mediante una sentencia SELECT
     * y devuelve lo hallado
     * @param sql La sentencia SELECT de SQL
     * @return El resultado de la consulta
     * @throws SQLException Error de SQL si la consulta
     * u algo más falla
     */
    static ResultSet consultar(String sql) throws SQLException {
        Connection conn = DBConnection.get();
        PreparedStatement pStatement = conn.prepareStatement(sql);
        return pStatement.executeQuery();
    }

    /** Cierra la conexión a la base de datos 
    */
    static void cerrar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar: " + e.getMessage());
        }
    }
}
