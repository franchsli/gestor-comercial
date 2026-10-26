package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.Venta;

/**
 * Como DAOLectura es abstracta, se prueba mediante VentaDAO,
 * la tabla más simple que tiene varios tipos de datos (int, fecha, enum).
 */
class DAOLecturaTest {

    private VentaDAO ventaDAO;

    @BeforeEach
    void setUp() {
        DBDePrueba.preparar();
        ventaDAO = new VentaDAO();
    }

    @AfterEach
    void tearDown() {
        DBDePrueba.limpiar();
    }

    /***
     *  Inserta una venta directamente con SQL y devuelve su id.
     * Se usa esto en lugar de VentaDAO.insertar() para asegurarse
     * que no falle por lógica de VentaDAO. 
     * @param fecha
     * @param valor
     * @param tipo
     * */
    private int insertarVenta(String fecha, int valor, String tipo) {
        String sql = "INSERT INTO VENTAS (fecha, valor, tipo) VALUES (?, ?, ?)";
        Connection conn = DBConnection.get();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, fecha);
            ps.setInt(2, valor);
            ps.setString(3, tipo);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Pruebas para todos()

    @Test
    @DisplayName("todos() devuelve todos los registros normalmente")
    void todosDevuelveTodosLosRegistros() {
        insertarVenta("2025-03-01", 1000, "EFECTIVO");
        insertarVenta("2025-03-02", 2000, "CREDITO");
        insertarVenta("2025-03-03", 3000, "EFECTIVO");

        List<Venta> ventas = ventaDAO.todos();

        assertEquals(3, ventas.size());
    }

    @Test
    @DisplayName("todos() devuelve una lista vacía si la tabla no tiene registros")
    void todosDevuelveListaVaciaSiTablaVacia() {
        List<Venta> ventas = ventaDAO.todos();

        assertNotNull(ventas);
        assertTrue(ventas.isEmpty());
    }

    @Test
    @DisplayName("todos(condicion, params) devuelve solo los registros que cumplen la condición")
    void todosConCondicionDevuelveSoloLosQueCumplen() {
        insertarVenta("2025-03-01", 1000, "EFECTIVO");
        insertarVenta("2025-03-02", 2000, "CREDITO");
        insertarVenta("2025-03-03", 3000, "EFECTIVO");

        List<Venta> ventas = ventaDAO.todos("tipo = ?", Venta.Tipo.EFECTIVO);

        assertEquals(2, ventas.size());
        for (Venta venta : ventas) {
            assertEquals(Venta.Tipo.EFECTIVO, venta.getTipo());
        }
    }

    @Test
    @DisplayName("todos(condicion, params) devuelve una lista vacía si ningún registro cumple la condición")
    void todosConCondicionDevuelveListaVaciaSiNadieCumple() {
        insertarVenta("2025-03-01", 1000, "EFECTIVO");
        insertarVenta("2025-03-02", 2000, "EFECTIVO");

        List<Venta> ventas = ventaDAO.todos("tipo = ?", Venta.Tipo.CREDITO);

        assertTrue(ventas.isEmpty());
    }

    // Pruebas para consultar()

    @Test
    @DisplayName("consultar() ejecuta un SELECT parametrizado y mapea cada fila a su entidad")
    void consultarEjecutaSelectParametrizadoYMapeaFilas() {
        insertarVenta("2025-03-01", 1000, "EFECTIVO");
        int idEsperado = insertarVenta("2025-03-02", 5000, "CREDITO");
        insertarVenta("2025-03-03", 7000, "EFECTIVO");

        List<Venta> ventas = ventaDAO.consultar(
                "SELECT * FROM VENTAS WHERE valor BETWEEN ? AND ?", 4000, 6000);

        assertEquals(1, ventas.size());
        Venta venta = ventas.get(0);
        assertEquals(idEsperado, venta.getId());
        assertEquals(LocalDate.of(2025, 3, 2), venta.getFecha());
        assertEquals(5000, venta.getValor());
        assertEquals(Venta.Tipo.CREDITO, venta.getTipo());
    }

    // Pruebas para unicoRegistro()

    @Test
    @DisplayName("unicoRegistro() devuelve el registro correcto cuando existe")
    void unicoRegistroDevuelveElRegistroCorrectoCuandoExiste() {
        insertarVenta("2025-03-01", 1000, "EFECTIVO");
        int id = insertarVenta("2025-03-02", 2000, "CREDITO");

        Venta venta = ventaDAO.unicoRegistro("id", id);

        assertNotNull(venta);
        assertEquals(id, venta.getId());
        assertEquals(2000, venta.getValor());
    }

    @Test
    @DisplayName("unicoRegistro() devuelve null cuando no existe")
    void unicoRegistroDevuelveNullCuandoNoExiste() {
        insertarVenta("2025-03-01", 1000, "EFECTIVO");

        Venta venta = ventaDAO.unicoRegistro("id", 9999);

        assertNull(venta);
    }

    // Pruebas para consultarUno()

    @Test
    @DisplayName("consultarUno() devuelve el valor escalar esperado usando el Lector")
    void consultarUnoDevuelveElValorEscalarEsperado() {
        insertarVenta("2025-03-01", 1000, "EFECTIVO");
        insertarVenta("2025-03-02", 2000, "CREDITO");
        insertarVenta("2025-03-03", 3000, "EFECTIVO");

        Integer cantidad = ventaDAO.consultarUno(
                "SELECT COUNT(*) FROM VENTAS WHERE tipo = ?",
                resultSet -> resultSet.getInt(1),
                "EFECTIVO");

        assertEquals(2, cantidad);
    }

    // Pruebas para sumarColumna()

    @Test
    @DisplayName("sumarColumna() devuelve la suma correcta de la columna")
    void sumarColumnaDevuelveLaSumaCorrecta() {
        insertarVenta("2025-03-01", 1000, "EFECTIVO");
        insertarVenta("2025-03-02", 2000, "CREDITO");
        insertarVenta("2025-03-03", 3000, "EFECTIVO");

        assertEquals(6000L, ventaDAO.sumarColumna("valor", ""));
        assertEquals(4000L, ventaDAO.sumarColumna("valor", "tipo = ?", Venta.Tipo.EFECTIVO));
    }

    @Test
    @DisplayName("sumarColumna() devuelve 0 cuando la tabla está vacía o ninguna fila cumple la condición")
    void sumarColumnaDevuelveCeroSiTablaVaciaONadieCumple() {
        // Tabla vacía
        assertEquals(0L, ventaDAO.sumarColumna("valor", ""));

        // Tabla con datos pero ninguna fila cumple la condición
        insertarVenta("2025-03-01", 1000, "EFECTIVO");
        assertEquals(0L, ventaDAO.sumarColumna("valor", "tipo = ?", Venta.Tipo.CREDITO));
    }

    // Pruebas para asignar()

    @Test
    @DisplayName("asignar() guarda correctamente un LocalDate y se recupera con el mismo valor")
    void asignarGuardaLocalDateCorrectamente() throws SQLException {
        LocalDate fecha = LocalDate.of(2025, 3, 15);
        Connection conn = DBConnection.get();

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO VENTAS (fecha, valor, tipo) VALUES (?, ?, ?)")) {
            DAOLectura.asignar(ps, new Object[] { fecha, 5000, "EFECTIVO" });
            ps.executeUpdate();
        }

        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT fecha FROM VENTAS")) {
            assertTrue(rs.next());
            assertEquals(fecha.toString(), rs.getString("fecha"));
            assertEquals(fecha, LocalDate.parse(rs.getString("fecha")));
        }
    }

    @Test
    @DisplayName("asignar() guarda correctamente un Enum y se recupera con el mismo valor")
    void asignarGuardaEnumCorrectamente() throws SQLException {
        Connection conn = DBConnection.get();

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO VENTAS (fecha, valor, tipo) VALUES (?, ?, ?)")) {
            DAOLectura.asignar(ps, new Object[] { "2025-03-15", 5000, Venta.Tipo.CREDITO });
            ps.executeUpdate();
        }

        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT tipo FROM VENTAS")) {
            assertTrue(rs.next());
            assertEquals(Venta.Tipo.CREDITO.name(), rs.getString("tipo"));
            assertEquals(Venta.Tipo.CREDITO, Venta.Tipo.valueOf(rs.getString("tipo")));
        }
    }

    // Pruebas para fechaDeColumna()

    @Test
    @DisplayName("fechaDeColumna() convierte un texto de fecha válido en LocalDate")
    void fechaDeColumnaConvierteTextoValidoEnLocalDate() throws SQLException {
        insertarVenta("2025-03-15", 1000, "EFECTIVO");
        Connection conn = DBConnection.get();

        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT fecha FROM VENTAS")) {
            assertTrue(rs.next());
            assertEquals(LocalDate.of(2025, 3, 15), DAOLectura.fechaDeColumna(rs, "fecha"));
        }
    }

    @Test
    @DisplayName("fechaDeColumna() devuelve null cuando la columna es nula")
    void fechaDeColumnaDevuelveNullCuandoColumnaEsNula() throws SQLException {
        Connection conn = DBConnection.get();

        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT NULL AS fecha")) {
            assertTrue(rs.next());
            assertNull(DAOLectura.fechaDeColumna(rs, "fecha"));
        }
    }

    // Pruebas de seguridad

    @Test
    @DisplayName("Una consulta con parámetros maliciosos se trata como texto y no altera el resultado")
    void consultaConParametrosMaliciososSeTrataComoTexto() {
        insertarVenta("2025-03-01", 1000, "EFECTIVO");
        insertarVenta("2025-03-02", 2000, "CREDITO");

        List<Venta> ventas = ventaDAO.todos("tipo = ?", "' OR 1=1 --");

        // Si se hubiera inyectado, devolvería las 2 filas
        assertTrue(ventas.isEmpty());
        // La tabla sigue intacta
        assertEquals(2, ventaDAO.todos().size());
    }
}