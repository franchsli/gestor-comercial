package com.example.db;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DBConnectionTest {

    @TempDir
    Path directorioTemporal;

    @BeforeEach
    void configurarBaseDeDatosDePrueba() throws Exception {
        String url = urlDe("prueba.db");
        crearBaseDeDatos(url);
        DBConnection.setUrl(url);
    }

    @AfterEach
    void restaurarEstado() {
        DBConnection.cerrar();
        DBConnection.setUrl(DBConnection.URL_POR_DEFECTO);
    }

    @Test
    @DisplayName("get() devuelve la misma conexión en llamadas consecutivas")
    void getDevuelveLaMismaConexionEnLlamadasConsecutivas() {
        Connection primera = DBConnection.get();
        Connection segunda = DBConnection.get();

        assertSame(primera, segunda);
    }

    @Test
    @DisplayName("Luego de cerrar(), get() crea una nueva conexión")
    void getCreaNuevaConexionLuegoDeCerrar() throws SQLException {
        Connection primera = DBConnection.get();
        DBConnection.cerrar();
        Connection segunda = DBConnection.get();

        assertNotSame(primera, segunda);
        assertFalse(segunda.isClosed());
    }

    @Test
    @DisplayName("setUrl() hace que get() se conecte a la nueva base de datos")
    void setUrlHaceQueGetSeConecteALaNuevaBaseDeDatos() throws Exception {
        // Se deja un dato en la base de datos original
        Connection original = DBConnection.get();
        try (Statement st = original.createStatement()) {
            st.executeUpdate("INSERT INTO PRESUPUESTOS (fecha, valor) VALUES ('2026-01-01', 1000)");
        }

        // Se cambia a una segunda base de datos, vacía
        String urlNueva = urlDe("nueva.db");
        crearBaseDeDatos(urlNueva);
        DBConnection.setUrl(urlNueva);
        Connection nueva = DBConnection.get();

        assertNotSame(original, nueva);
        try (Statement st = nueva.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM PRESUPUESTOS")) {
            assertEquals(0, rs.getInt(1));
        }
    }

    @Test
    @DisplayName("cerrar() se puede llamar varias veces seguidas sin lanzar error")
    void cerrarSePuedeLlamarVariasVecesSinError() {
        DBConnection.get();

        assertDoesNotThrow(() -> {
            DBConnection.cerrar();
            DBConnection.cerrar();
            DBConnection.cerrar();
        });
    }

    @Test
    @DisplayName("get() deja habilitadas las llaves foráneas")
    void getDejaHabilitadasLasLlavesForaneas() throws SQLException {
        Connection conexion = DBConnection.get();

        try (Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery("PRAGMA foreign_keys")) {
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    @DisplayName("cerrar() cierra la conexión correctamente")
    void cerrarCierraLaConexion() throws SQLException {
        Connection conexion = DBConnection.get();
        assertFalse(conexion.isClosed());

        DBConnection.cerrar();

        assertTrue(conexion.isClosed());
    }

    // ---------- utilidades ----------

    private String urlDe(String nombreArchivo) {
        return "jdbc:sqlite:" + directorioTemporal.resolve(nombreArchivo);
    }

    /** Crea una base de datos nueva ejecutando schema.sql, sin usar DBConnection. */
    private void crearBaseDeDatos(String url) throws SQLException, IOException {
        String script;
        try (InputStream in = getClass().getResourceAsStream("/schema.sql")) {
            script = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
        try (Connection conexion = DriverManager.getConnection(url);
            Statement st = conexion.createStatement()) {
            for (String sentencia : script.split(";")) {
                if (!sentencia.isBlank()) {
                    st.execute(sentencia);
                }
            }
        }
    }
}