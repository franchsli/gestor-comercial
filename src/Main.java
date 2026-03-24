import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // La ruta del archivo. Si no existe, SQLite lo creará automáticamente.
        String url = "jdbc:sqlite:mi_base_de_datos.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("¡Conexión establecida con SQLite!");
                
                // Ejemplo: Crear una tabla sencilla
                String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                             "id INTEGER PRIMARY KEY," +
                             "nombre TEXT NOT NULL" +
                             ");";
                
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                System.out.println("Tabla verificada/creada.");
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}