package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

class ProductoDAOTest {
    private ProductoDAO productoDAO;

    @BeforeEach
    void setUp() {
        DBDePrueba.preparar();
        productoDAO = new ProductoDAO();
    }

    @AfterEach
    void tearDown() {
        DBDePrueba.limpiar();
    }

    @Test
    @DisplayName("insertar() guarda el producto y se recupera con todos sus campos correctos")
    void insertarGuardaProductoConTodosSusCampos() {
        productoDAO.insertar(new Producto(0, "Gancho plástico", 1500, 40));

        List<Producto> productos = productoDAO.todos();

        assertEquals(1, productos.size());
        Producto guardado = productos.get(0);
        assertEquals(1, guardado.getId());
        assertEquals("Gancho plástico", guardado.getNombre());
        assertEquals(1500, guardado.getPrecioUnitario());
        assertEquals(40, guardado.getCantidad());
    }

    @Test
    @DisplayName("actualizar() modifica nombre, precio y cantidad del producto indicado")
    void actualizarModificaNombrePrecioYCantidad() {
        productoDAO.insertar(new Producto(0, "Plato plástico", 2000, 10));
        int id = productoDAO.todos().get(0).getId();

        productoDAO.actualizar(new Producto(id, "Platón grande", 3500, 25));

        List<Producto> productos = productoDAO.todos();
        assertEquals(1, productos.size());
        Producto actualizado = productos.get(0);
        assertEquals(id, actualizado.getId());
        assertEquals("Platón grande", actualizado.getNombre());
        assertEquals(3500, actualizado.getPrecioUnitario());
        assertEquals(25, actualizado.getCantidad());
    }

    @Test
    @DisplayName("buscarPorNombre() retorna los productos cuyo nombre coincide con el texto dado")
    void buscarPorNombreRetornaProductosConNombreSimilar() {
        productoDAO.insertar(new Producto(0, "Gancho plástico", 1500, 40));
        productoDAO.insertar(new Producto(0, "Plato plástico", 2000, 10));
        productoDAO.insertar(new Producto(0, "Vaso de vidrio", 1200, 15));

        List<Producto> productos = productoDAO.buscarPorNombre("plástico");

        assertEquals(2, productos.size());
        for (Producto producto : productos) {
            assertTrue(producto.getNombre().contains("plástico"));
        }
    }

    @Test
    @DisplayName("tieneVentas() devuelve true si el producto tiene ventas asociadas")
    void tieneVentasDevuelveTrueSiElProductoTieneVentasAsociadas() {
        int idProducto = productoDAO.insertarFila("Libreta", 4000, 20);

        VentaDAO ventaDAO = new VentaDAO();
        int idVenta = ventaDAO.insertarFila(LocalDate.of(2025, 3, 10), 12000, "EFECTIVO");

        VentaProductoDAO ventaProductoDAO = new VentaProductoDAO();
        ventaProductoDAO.insertar(new VentaProducto(idVenta, idProducto, 3));

        assertTrue(productoDAO.tieneVentas(String.valueOf(idProducto)));
    }

    @Test
    @DisplayName("tieneVentas() devuelve false si no tiene ventas asociadas")
    void tieneVentasDevuelveFalseSiNoTieneVentasAsociadas() {
        int idProducto = productoDAO.insertarFila("Libreta", 4000, 20);

        assertFalse(productoDAO.tieneVentas(String.valueOf(idProducto)));
    }

    @Test
    @DisplayName("borrarTodos() solo borra productos sin ventas")
    void borrarTodosSoloBorraProductosSinVentas() {
        int idConVentas = productoDAO.insertarFila("Libreta", 4000, 20);
        int idSinVentas = productoDAO.insertarFila("Borrador", 800, 15);

        VentaDAO ventaDAO = new VentaDAO();
        int idVenta = ventaDAO.insertarFila(LocalDate.of(2025, 3, 10), 12000, "EFECTIVO");

        VentaProductoDAO ventaProductoDAO = new VentaProductoDAO();
        ventaProductoDAO.insertar(new VentaProducto(idVenta, idConVentas, 3));

        assertThrows(IllegalStateException.class,
                () -> productoDAO.borrarTodos("id = ?", String.valueOf(idConVentas)));
        assertNotNull(productoDAO.unicoRegistro("id", idConVentas));

        productoDAO.borrarTodos("id = ?", String.valueOf(idSinVentas));
        assertNull(productoDAO.unicoRegistro("id", idSinVentas));
    }
}
