package com.example.db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Helper para las pruebas de los DAO. Apunta DBConnection a una base de datos
 * en memoria y le aplica schema.sql, de modo que cada prueba parte de una base limpia.
 */
public class DBDePrueba {

    private static final String URL_EN_MEMORIA = "jdbc:sqlite::memory:";

    /** Se usa en @BeforeEach*/
    public static void preparar() {
        DBConnection.setUrl(URL_EN_MEMORIA);
        try {
            crearDB();
        } catch (SQLException | IOException e) {
            throw new RuntimeException("No se pudo preparar la base de datos de prueba", e);
        }
    }

    /** Se usa en @AfterEach*/
    public static void limpiar() {
        DBConnection.cerrar();
        DBConnection.setUrl(DBConnection.URL_POR_DEFECTO);
    }

    /** Crea las tablas ejecutando schema.sql */
    private static void crearDB() throws SQLException, IOException {
        String script;
        try (InputStream in = DBDePrueba.class.getResourceAsStream("/schema.sql")) {
            if (in == null) {
                throw new IOException("No se encontró /schema.sql en el classpath");
            }
            script = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }

        Connection connection = DBConnection.get();
        try (Statement st = connection.createStatement()) {
            for (String sentencia : script.split(";")) {
                if (!sentencia.isBlank()) {
                    st.execute(sentencia);
                }
            }
        }
    }
}