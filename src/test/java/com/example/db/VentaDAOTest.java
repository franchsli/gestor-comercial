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
    @DisplayName("venderProductos() crea la fila en VENTAS_PRODUCTOS con la cantidad correcta.")
    void venderProductosCreaFilaEnVentasProductosConCantidadCorrecta() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProducto = productoDAO.insertarFila("Cuaderno", 3000, 20);
        Producto producto = productoDAO.unicoRegistro("id", idProducto);

        Venta venta = new Venta(LocalDate.of(2025, 3, 10), 0, Venta.Tipo.EFECTIVO);

        ventaDAO.venderProductos(venta, List.of(producto), List.of(5));

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
    @DisplayName("venderProductos() con un producto asigna el valor total correctamente.")
    void venderProductosConUnProductoAsignaElValorTotalCorrectamente() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProducto = productoDAO.insertarFila("Lapicero", 2500, 10);
        Producto producto = productoDAO.unicoRegistro("id", idProducto);

        Venta venta = new Venta(LocalDate.of(2025, 3, 10), 0, Venta.Tipo.EFECTIVO);

        ventaDAO.venderProductos(venta, List.of(producto), List.of(4));

        assertEquals(10000, venta.getValor());

        Venta ventaEnDb = ventaDAO.unicoRegistro("id", venta.getId());
        assertNotNull(ventaEnDb);
        assertEquals(10000, ventaEnDb.getValor());
    }

    @Test
    @DisplayName("venderProductos() con varios productos acumula el valor total correctamente.")
    void venderProductosConVariosProductosAcumulaElValorTotalCorrectamente() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProd1 = productoDAO.insertarFila("Borrador", 1500, 10);
        int idProd2 = productoDAO.insertarFila("Regla", 4000, 5);
        Producto prod1 = productoDAO.unicoRegistro("id", idProd1);
        Producto prod2 = productoDAO.unicoRegistro("id", idProd2);

        Venta venta = new Venta(LocalDate.of(2025, 3, 10), 0, Venta.Tipo.EFECTIVO);

        ventaDAO.venderProductos(venta, List.of(prod1, prod2), List.of(3, 2));

        int valorEsperado = (1500 * 3) + (4000 * 2); // 4500 + 8000 = 12500
        assertEquals(valorEsperado, venta.getValor());

        Venta ventaEnDb = ventaDAO.unicoRegistro("id", venta.getId());
        assertNotNull(ventaEnDb);
        assertEquals(valorEsperado, ventaEnDb.getValor());
    }

    @Test
    @DisplayName("venderProductos crea las filas en VENTAS_PRODUCTOS con las cantidades correctas por producto.")
    void venderProductosCreaLasFilasEnVentasProductosConLasCantidadesCorrectasPorProducto() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProd1 = productoDAO.insertarFila("Cuaderno", 3000, 20);
        int idProd2 = productoDAO.insertarFila("Lapicero", 1500, 10);
        Producto prod1 = productoDAO.unicoRegistro("id", idProd1);
        Producto prod2 = productoDAO.unicoRegistro("id", idProd2);

        Venta venta = new Venta(LocalDate.of(2025, 3, 10), 0, Venta.Tipo.EFECTIVO);

        ventaDAO.venderProductos(venta, List.of(prod1, prod2), List.of(4, 7));

        VentaProductoDAO ventaProductoDAO = new VentaProductoDAO();
        List<VentaProducto> detalles = ventaProductoDAO.todos("id_venta = ?", venta.getId());
        assertEquals(2, detalles.size());

        VentaProducto vp1 = detalles.stream()
                .filter(vp -> vp.getIdProducto() == idProd1)
                .findFirst()
                .orElse(null);
        assertNotNull(vp1);
        assertEquals(4, vp1.getCantidadProducto());

        VentaProducto vp2 = detalles.stream()
                .filter(vp -> vp.getIdProducto() == idProd2)
                .findFirst()
                .orElse(null);
        assertNotNull(vp2);
        assertEquals(7, vp2.getCantidadProducto());

        Producto prod1Actualizado = productoDAO.unicoRegistro("id", idProd1);
        assertEquals(16, prod1Actualizado.getCantidad());

        Producto prod2Actualizado = productoDAO.unicoRegistro("id", idProd2);
        assertEquals(3, prod2Actualizado.getCantidad());
    }

    @Test
    @DisplayName("venderProductos() falla sin crear la venta si el inventario de algún producto es insuficiente.")
    void venderProductosFallaSinCrearLaVentaSiElInventarioDeAlgunProductoEsInsuficiente() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProd1 = productoDAO.insertarFila("Tijeras", 5000, 10);
        int idProd2 = productoDAO.insertarFila("Pegante", 3000, 2);
        Producto prod1 = productoDAO.unicoRegistro("id", idProd1);
        Producto prod2 = productoDAO.unicoRegistro("id", idProd2);

        Venta venta = new Venta(LocalDate.of(2025, 3, 10), 0, Venta.Tipo.EFECTIVO);

        assertThrows(IllegalArgumentException.class, () ->
            ventaDAO.venderProductos(venta, List.of(prod1, prod2), List.of(3, 5))
        );

        assertTrue(ventaDAO.todos().isEmpty());

        VentaProductoDAO ventaProductoDAO = new VentaProductoDAO();
        assertTrue(ventaProductoDAO.todos().isEmpty());

        Producto prod1Actual = productoDAO.unicoRegistro("id", idProd1);
        assertEquals(10, prod1Actual.getCantidad());

        Producto prod2Actual = productoDAO.unicoRegistro("id", idProd2);
        assertEquals(2, prod2Actual.getCantidad());
    }

    @Test
    @DisplayName("venderProductos() falla sin crear la venta bajo cualquier otro error.")
    void venderProductosFallaSinCrearLaVentaBajoCualquierOtroError() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProducto = productoDAO.insertarFila("Cuaderno", 3000, 20);
        Producto producto = productoDAO.unicoRegistro("id", idProducto);

        Venta ventaInvalida = new Venta(null, 0, Venta.Tipo.EFECTIVO);

        assertThrows(DBException.class, () ->
            ventaDAO.venderProductos(ventaInvalida, List.of(producto), List.of(5))
        );

        assertTrue(ventaDAO.todos().isEmpty());

        VentaProductoDAO ventaProductoDAO = new VentaProductoDAO();
        assertTrue(ventaProductoDAO.todos().isEmpty());

        Producto productoActualizado = productoDAO.unicoRegistro("id", idProducto);
        assertEquals(20, productoActualizado.getCantidad());
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
