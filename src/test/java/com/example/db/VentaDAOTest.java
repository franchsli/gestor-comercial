package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.Producto;
import com.example.model.Venta;
import com.example.model.VentaProducto;

class VentaDAOTest {

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

    @Test
    @DisplayName("insertar() guarda la venta con su fecha, valor y tipo (EFECTIVO/CREDITO).")
    void insertarGuardaVentaConFechaValorYTipo() {
        LocalDate fecha1 = LocalDate.of(2025, 3, 10);
        Venta ventaEfectivo = new Venta(fecha1, 5000, Venta.Tipo.EFECTIVO);
        ventaDAO.insertar(ventaEfectivo);

        LocalDate fecha2 = LocalDate.of(2025, 3, 11);
        Venta ventaCredito = new Venta(fecha2, 8000, Venta.Tipo.CREDITO);
        ventaDAO.insertar(ventaCredito);

        List<Venta> ventas = ventaDAO.todos();
        assertEquals(2, ventas.size());

        Venta guardada1 = ventas.get(0);
        assertTrue(guardada1.getId() > 0);
        assertEquals(fecha1, guardada1.getFecha());
        assertEquals(5000, guardada1.getValor());
        assertEquals(Venta.Tipo.EFECTIVO, guardada1.getTipo());

        Venta guardada2 = ventas.get(1);
        assertTrue(guardada2.getId() > 0);
        assertEquals(fecha2, guardada2.getFecha());
        assertEquals(8000, guardada2.getValor());
        assertEquals(Venta.Tipo.CREDITO, guardada2.getTipo());
    }

    @Test
    @DisplayName("actualizar() modifica los datos de la venta indicada.")
    void actualizarModificaDatosDeLaVentaIndicada() {
        Venta venta = new Venta(LocalDate.of(2025, 3, 10), 5000, Venta.Tipo.EFECTIVO);
        ventaDAO.insertar(venta);
        int id = venta.getId();

        LocalDate nuevaFecha = LocalDate.of(2025, 3, 15);
        Venta ventaModificada = new Venta(id, nuevaFecha, 12000, Venta.Tipo.CREDITO);
        ventaDAO.actualizar(ventaModificada);

        Venta recuperada = ventaDAO.unicoRegistro("id", id);
        assertNotNull(recuperada);
        assertEquals(id, recuperada.getId());
        assertEquals(nuevaFecha, recuperada.getFecha());
        assertEquals(12000, recuperada.getValor());
        assertEquals(Venta.Tipo.CREDITO, recuperada.getTipo());
    }

    @Test
    @DisplayName("vender() crea la fila en VENTAS_PRODUCTOS con la cantidad correcta.")
    void venderCreaFilaEnVentasProductosConCantidadCorrecta() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProducto = productoDAO.insertarFila("Cuaderno", 3000, 20);
        Producto producto = productoDAO.unicoRegistro("id", idProducto);

        Venta venta = new Venta(LocalDate.of(2025, 3, 10), 0, Venta.Tipo.EFECTIVO);
        ventaDAO.insertar(venta);

        ventaDAO.vender(venta, producto, 5);

        VentaProductoDAO ventaProductoDAO = new VentaProductoDAO();
        List<VentaProducto> detalles = ventaProductoDAO.todos("id_venta = ?", venta.getId());
        assertEquals(1, detalles.size());

        VentaProducto vp = detalles.get(0);
        assertEquals(venta.getId(), vp.getIdVenta());
        assertEquals(idProducto, vp.getIdProducto());
        assertEquals(5, vp.getCantidadProducto());

        Producto productoActualizado = productoDAO.unicoRegistro("id", idProducto);
        assertEquals(15, productoActualizado.getCantidad());
    }

    @Test
    @DisplayName("vender() actualiza el valor de la venta con precio x cantidad.")
    void venderActualizaElValorDeLaVentaConPrecioPorCantidad() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProducto = productoDAO.insertarFila("Lapicero", 2500, 10);
        Producto producto = productoDAO.unicoRegistro("id", idProducto);

        Venta venta = new Venta(LocalDate.of(2025, 3, 10), 0, Venta.Tipo.EFECTIVO);
        ventaDAO.insertar(venta);

        ventaDAO.vender(venta, producto, 4);

        assertEquals(10000, venta.getValor());

        Venta ventaEnDb = ventaDAO.unicoRegistro("id", venta.getId());
        assertNotNull(ventaEnDb);
        assertEquals(10000, ventaEnDb.getValor());
    }

    @Test
    @DisplayName("vender() con varios productos acumula el valor total correctamente.")
    void venderConVariosProductosAcumulaElValorTotalCorrectamente() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProd1 = productoDAO.insertarFila("Borrador", 1500, 10);
        int idProd2 = productoDAO.insertarFila("Regla", 4000, 5);
        Producto prod1 = productoDAO.unicoRegistro("id", idProd1);
        Producto prod2 = productoDAO.unicoRegistro("id", idProd2);

        Venta venta = new Venta(LocalDate.of(2025, 3, 10), 0, Venta.Tipo.EFECTIVO);
        ventaDAO.insertar(venta);

        ventaDAO.vender(venta, prod1, 3);
        ventaDAO.vender(venta, prod2, 2);

        int valorEsperado = (1500 * 3) + (4000 * 2); // 4500 + 8000 = 12500
        assertEquals(valorEsperado, venta.getValor());

        Venta ventaEnDb = ventaDAO.unicoRegistro("id", venta.getId());
        assertNotNull(ventaEnDb);
        assertEquals(valorEsperado, ventaEnDb.getValor());
    }

    @Test
    @DisplayName("vender() falla sin modificar la venta si el inventario es insuficiente.")
    void venderFallaSinModificarLaVentaSiElInventarioEsInsuficiente() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProducto = productoDAO.insertarFila("Tijeras", 5000, 2);
        Producto producto = productoDAO.unicoRegistro("id", idProducto);

        Venta venta = new Venta(LocalDate.of(2025, 3, 10), 1000, Venta.Tipo.EFECTIVO);
        ventaDAO.insertar(venta);

        assertThrows(IllegalArgumentException.class, () -> ventaDAO.vender(venta, producto, 5));

        assertEquals(1000, venta.getValor());

        Venta ventaEnDb = ventaDAO.unicoRegistro("id", venta.getId());
        assertNotNull(ventaEnDb);
        assertEquals(1000, ventaEnDb.getValor());

        VentaProductoDAO ventaProductoDAO = new VentaProductoDAO();
        assertTrue(ventaProductoDAO.todos("id_venta = ?", venta.getId()).isEmpty());

        Producto productoActual = productoDAO.unicoRegistro("id", idProducto);
        assertEquals(2, productoActual.getCantidad());
    }

    @Test
    @DisplayName("mapear() convierte tipo al enum correcto (EFECTIVO/CREDITO).")
    void mapearConvierteTipoAlEnumCorrecto() throws SQLException {
        ventaDAO.insertarFila(LocalDate.of(2025, 3, 10), 1000, "EFECTIVO");
        ventaDAO.insertarFila(LocalDate.of(2025, 3, 11), 2000, "CREDITO");

        Connection conn = DBConnection.get();
        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM VENTAS ORDER BY id ASC")) {
            assertTrue(rs.next());
            Venta ventaEfectivo = ventaDAO.mapear(rs);
            assertEquals(Venta.Tipo.EFECTIVO, ventaEfectivo.getTipo());

            assertTrue(rs.next());
            Venta ventaCredito = ventaDAO.mapear(rs);
            assertEquals(Venta.Tipo.CREDITO, ventaCredito.getTipo());
        }
    }
}
