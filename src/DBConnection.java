import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection = null;
    
    public static Connection get() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:sqlite:mi_base_de_datos.db");
        }
        return connection;
    }

    // Para INSERT/UPDATE/DELETE
    protected void ejecutar(String sql) {
        try (Connection conn = DBConnection.get();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {
            pStatement.execute();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
