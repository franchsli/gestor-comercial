import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection = null;
    
    public static Connection get() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:sqlite:mi_base_de_datos.db");
        }
        return connection;
    }
}
