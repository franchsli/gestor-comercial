package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.example.model.Venta;

/**
 * Como es abstracta, se prueba mediante VentaDAO.
 */
class DAOCrudTest {

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

    /** Cuenta los registros de una tabla con SQL directo. */
    private int contar(String tabla) {
        int total = ventaDAO.consultarUno(
                "SELECT COUNT(*) FROM " + tabla,
                resultSet -> resultSet.getInt(1));
        return total;
    }

    @Test
    @DisplayName("insertarFila() inserta el registro y devuelve el id generado")
    void insertarFilaInsertaRegistroYDevuelveIdGenerado() {
        int id = ventaDAO.insertarFila(LocalDate.of(2025, 3, 10), 4500, Venta.Tipo.EFECTIVO);

        assertTrue(id > 0);
        assertEquals(1, contar("VENTAS"));

        Venta venta = ventaDAO.unicoRegistro("id", id);
        assertNotNull(venta);
        assertEquals(id, venta.getId());
        assertEquals(LocalDate.of(2025, 3, 10), venta.getFecha());
        assertEquals(4500, venta.getValor());
        assertEquals(Venta.Tipo.EFECTIVO, venta.getTipo());
    }

    @Test
    @DisplayName("insertarFila() genera ids distintos y crecientes en inserciones consecutivas")
    void insertarFilaGeneraIdsDistintosYCrecientes() {
        int id1 = ventaDAO.insertarFila(LocalDate.of(2025, 3, 1), 1000, Venta.Tipo.EFECTIVO);
        int id2 = ventaDAO.insertarFila(LocalDate.of(2025, 3, 2), 2000, Venta.Tipo.CREDITO);
        int id3 = ventaDAO.insertarFila(LocalDate.of(2025, 3, 3), 3000, Venta.Tipo.EFECTIVO);

        assertTrue(id2 > id1);
        assertTrue(id3 > id2);
        assertEquals(3, contar("VENTAS"));
    }

    @Test
    @DisplayName("insertarYObtenerId() devuelve el id generado del nuevo registro")
    void insertarYObtenerIdDevuelveIdGenerado() {
        int id = ventaDAO.insertarYObtenerId(
                "INSERT INTO VENTAS (fecha, valor, tipo) VALUES (?, ?, ?)",
                LocalDate.of(2025, 3, 10), 4500, Venta.Tipo.CREDITO);

        assertTrue(id > 0);

        Venta venta = ventaDAO.unicoRegistro("id", id);
        assertNotNull(venta);
        assertEquals(4500, venta.getValor());
        assertEquals(Venta.Tipo.CREDITO, venta.getTipo());
    }

    @Test
    @DisplayName("ejecutar() realiza correctamente un INSERT, UPDATE y DELETE parametrizados")
    void ejecutarRealizaInsertUpdateYDelete() {
        // INSERT
        ventaDAO.ejecutar(
                "INSERT INTO VENTAS (fecha, valor, tipo) VALUES (?, ?, ?)",
                LocalDate.of(2025, 3, 10), 1000, Venta.Tipo.EFECTIVO);
        List<Venta> ventas = ventaDAO.todos();
        assertEquals(1, ventas.size());
        int id = ventas.get(0).getId();
        assertEquals(1000, ventas.get(0).getValor());

        // UPDATE
        ventaDAO.ejecutar(
                "UPDATE VENTAS SET valor = ?, tipo = ? WHERE id = ?",
                2500, Venta.Tipo.CREDITO, id);
        Venta actualizada = ventaDAO.unicoRegistro("id", id);
        assertEquals(2500, actualizada.getValor());
        assertEquals(Venta.Tipo.CREDITO, actualizada.getTipo());

        // DELETE
        ventaDAO.ejecutar("DELETE FROM VENTAS WHERE id = ?", id);
        assertNull(ventaDAO.unicoRegistro("id", id));
        assertEquals(0, contar("VENTAS"));
    }

    @Test
    @DisplayName("borrarTodos(condicion, params) elimina solo los registros que cumplen la condición")
    void borrarTodosEliminaSoloLosQueCumplenLaCondicion() {
        ventaDAO.insertarFila(LocalDate.of(2025, 3, 1), 1000, Venta.Tipo.EFECTIVO);
        ventaDAO.insertarFila(LocalDate.of(2025, 3, 2), 2000, Venta.Tipo.CREDITO);
        ventaDAO.insertarFila(LocalDate.of(2025, 3, 3), 3000, Venta.Tipo.EFECTIVO);

        ventaDAO.borrarTodos("tipo = ?", Venta.Tipo.EFECTIVO);

        List<Venta> restantes = ventaDAO.todos();
        assertEquals(1, restantes.size());
        assertEquals(Venta.Tipo.CREDITO, restantes.get(0).getTipo());
    }

    @Test
    @DisplayName("borrarTodos() no elimina nada si ningún registro cumple la condición")
    void borrarTodosNoEliminaNadaSiNadieCumple() {
        ventaDAO.insertarFila(LocalDate.of(2025, 3, 1), 1000, Venta.Tipo.EFECTIVO);
        ventaDAO.insertarFila(LocalDate.of(2025, 3, 2), 2000, Venta.Tipo.CREDITO);

        ventaDAO.borrarTodos("valor > ?", 99999);

        assertEquals(2, contar("VENTAS"));
    }

    @Test
    @DisplayName("Violar restricción de llave foránea lanza excepción y no deja datos a medias")
    void violarRestriccionDeLlaveForaneaLanzaExcepcionYNoDejaDatosAMedias() {
        ventaDAO.insertarFila(LocalDate.of(2025, 3, 1), 1000, Venta.Tipo.EFECTIVO);
        int ventasAntes = contar("VENTAS");

        // Viola la llave foránea: no existe la venta 999 ni el producto 999
        assertThrows(DBException.class, () -> ventaDAO.ejecutar(
                "INSERT INTO VENTAS_PRODUCTOS (id_venta, id_producto, cantidad_producto) VALUES (?, ?, ?)",
                999, 999, 1));

        assertEquals(0, contar("VENTAS_PRODUCTOS"));
        assertEquals(ventasAntes, contar("VENTAS"));
    }

    @Test
    @DisplayName("Violar restricción de NOT NULL lanza excepción y no deja datos a medias")
    void violarRestriccionDeNotNullLanzaExcepcionYNoDejaDatosAMedias() {
        int ventasAntes = contar("VENTAS");

        // Viola la llave restricción al tener la fecha nula.
        assertThrows(DBException.class, () -> ventaDAO.insertarFila(null, 1000, Venta.Tipo.EFECTIVO));
        assertEquals(ventasAntes, contar("VENTAS"));
    }
}