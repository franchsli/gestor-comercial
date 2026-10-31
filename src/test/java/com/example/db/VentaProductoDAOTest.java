package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.Producto;
import com.example.model.VentaProducto;

class VentaProductoDAOTest {

    private VentaProductoDAO ventaProductoDAO;

    @BeforeEach
    void setUp() {
        DBDePrueba.preparar();
        ventaProductoDAO = new VentaProductoDAO();
    }

    @AfterEach
    void tearDown() {
        DBDePrueba.limpiar();
    }

    @Test
    @DisplayName("insertar() descuenta del inventario la cantidad vendida.")
    void insertarDescuentaDelInventarioLaCantidadVendida() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProducto = productoDAO.insertarFila("Libreta", 4000, 20);

        VentaDAO ventaDAO = new VentaDAO();
        int idVenta = ventaDAO.insertarFila(LocalDate.of(2025, 3, 10), 12000, "EFECTIVO");

        ventaProductoDAO.insertar(new VentaProducto(idVenta, idProducto, 3));

        List<VentaProducto> filas = ventaProductoDAO.todos("id_venta = ?", idVenta);
        assertEquals(1, filas.size());
        assertEquals(3, filas.get(0).getCantidadProducto());

        Producto productoActualizado = productoDAO.unicoRegistro("id", idProducto);
        assertEquals(17, productoActualizado.getCantidad());
    }

    @Test
    @DisplayName("insertar() lanza excepción si el inventario quedaría negativo, y no inserta la fila.")
    void insertarLanzaExcepcionSiInventarioQuedariaNegativoYNoInsertaLaFila() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProducto = productoDAO.insertarFila("Marcador", 2000, 5);

        VentaDAO ventaDAO = new VentaDAO();
        int idVenta = ventaDAO.insertarFila(LocalDate.of(2025, 3, 10), 0, "EFECTIVO");

        assertThrows(IllegalArgumentException.class,
                () -> ventaProductoDAO.insertar(new VentaProducto(idVenta, idProducto, 6)));

        List<VentaProducto> filas = ventaProductoDAO.todos("id_venta = ?", idVenta);
        assertTrue(filas.isEmpty());

        Producto productoActual = productoDAO.unicoRegistro("id", idProducto);
        assertEquals(5, productoActual.getCantidad());
    }

    @Test
    @DisplayName("Eliminar una venta elimina sus filas en VENTAS_PRODUCTOS (ON DELETE CASCADE).")
    void eliminarUnaVentaEliminaSusFilasEnVentasProductos() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProducto = productoDAO.insertarFila("Carpeta", 1500, 10);

        VentaDAO ventaDAO = new VentaDAO();
        int idVenta = ventaDAO.insertarFila(LocalDate.of(2025, 3, 10), 3000, "EFECTIVO");

        ventaProductoDAO.insertar(new VentaProducto(idVenta, idProducto, 2));
        assertEquals(1, ventaProductoDAO.todos("id_venta = ?", idVenta).size());

        ventaDAO.borrarTodos("id = ?", idVenta);

        assertNull(ventaDAO.unicoRegistro("id", idVenta));
        List<VentaProducto> filas = ventaProductoDAO.todos("id_venta = ?", idVenta);
        assertTrue(filas.isEmpty());
    }

    @Test
    @DisplayName("Eliminar un producto con filas en VENTAS_PRODUCTOS lanza DBException")
    void eliminarUnProductoConFilasEnVentasProductosLanzaDBException() {
        ProductoDAO productoDAO = new ProductoDAO();
        int idProducto = productoDAO.insertarFila("Borrador", 800, 15);

        VentaDAO ventaDAO = new VentaDAO();
        int idVenta = ventaDAO.insertarFila(LocalDate.of(2025, 3, 10), 1600, "EFECTIVO");

        ventaProductoDAO.insertar(new VentaProducto(idVenta, idProducto, 2));
        assertEquals(1, ventaProductoDAO.todos("id_producto = ?", idProducto).size());

        assertThrows(DBException.class,
                () -> productoDAO.ejecutar("DELETE FROM PRODUCTOS WHERE id = ?", idProducto));
    }
}
