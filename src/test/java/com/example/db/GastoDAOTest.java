package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import com.example.model.Gasto;

class GastoDAOTest {

    private GastoDAO gastoDAO;

    @BeforeEach
    void setUp() {
        DBDePrueba.preparar();
        gastoDAO = new GastoDAO();
    }

    @AfterEach
    void tearDown() {
        DBDePrueba.limpiar();
    }

    @Test
    @DisplayName("insertar() guarda el gasto con su estado (PENDIENTE/PAGADO) y fechas.")
    void insertarGuardaGastoConSuEstadoYFechas() {
        LocalDate fechaPresupuesto = LocalDate.of(2025, 3, 1);
        gastoDAO.ejecutar("INSERT INTO PRESUPUESTOS (fecha, valor) VALUES (?, ?)", fechaPresupuesto, 50000);

        LocalDate fechaGasto1 = LocalDate.of(2025, 3, 5);
        LocalDate fechaPago1 = LocalDate.of(2025, 3, 6);
        Gasto gastoPagado = new Gasto(fechaGasto1, Gasto.Estado.PAGADO, 12000, fechaPago1, "Compra papelería", fechaPresupuesto);

        LocalDate fechaGasto2 = LocalDate.of(2025, 3, 7);
        Gasto gastoPendiente = new Gasto(fechaGasto2, Gasto.Estado.PENDIENTE, 8000, null, "Servicio internet", fechaPresupuesto);

        gastoDAO.insertar(gastoPagado);
        gastoDAO.insertar(gastoPendiente);

        List<Gasto> gastos = gastoDAO.todos();
        assertEquals(2, gastos.size());

        Gasto guardado1 = gastos.get(0);
        assertEquals(fechaGasto1, guardado1.getFechaGasto());
        assertEquals(Gasto.Estado.PAGADO, guardado1.getEstado());
        assertEquals(12000, guardado1.getValor());
        assertEquals(fechaPago1, guardado1.getFechaPago());
        assertEquals("Compra papelería", guardado1.getDescripcion());
        assertEquals(fechaPresupuesto, guardado1.getFechaPresupuesto());

        Gasto guardado2 = gastos.get(1);
        assertEquals(fechaGasto2, guardado2.getFechaGasto());
        assertEquals(Gasto.Estado.PENDIENTE, guardado2.getEstado());
        assertEquals(8000, guardado2.getValor());
        assertNull(guardado2.getFechaPago());
        assertEquals("Servicio internet", guardado2.getDescripcion());
        assertEquals(fechaPresupuesto, guardado2.getFechaPresupuesto());
    }

    @Test
    @DisplayName("insertar() acepta fechaPago y fechaPresupuesto nulas en la entidad.")
    void insertarAceptaFechaPagoYFechaPresupuestoNulasEnLaEntidad() {
        LocalDate fechaGasto = LocalDate.of(2025, 3, 10);
        Gasto gasto = new Gasto(fechaGasto, Gasto.Estado.PENDIENTE, 5000, null, "Gasto sin fechas opcionales", null);

        gastoDAO.insertar(gasto);

        List<Gasto> gastos = gastoDAO.todos();
        assertEquals(1, gastos.size());
        Gasto guardado = gastos.get(0);
        assertNull(guardado.getFechaPago());
        assertNull(guardado.getFechaPresupuesto());
        assertEquals(fechaGasto, guardado.getFechaGasto());
        assertEquals(5000, guardado.getValor());
        assertEquals("Gasto sin fechas opcionales", guardado.getDescripcion());
        assertEquals(Gasto.Estado.PENDIENTE, guardado.getEstado());
    }

    @Test
    @DisplayName("actualizar() permite cambiar un gasto de PENDIENTE a PAGADO con su fecha de pago.")
    void actualizarPermiteCambiarGastoDePendienteAPagadoConSuFechaDePago() {
        LocalDate fechaGasto = LocalDate.of(2025, 3, 10);
        Gasto gastoPendiente = new Gasto(fechaGasto, Gasto.Estado.PENDIENTE, 15000, null, "Mantenimiento", null);
        gastoDAO.insertar(gastoPendiente);

        Gasto guardado = gastoDAO.todos().get(0);
        assertEquals(Gasto.Estado.PENDIENTE, guardado.getEstado());
        assertNull(guardado.getFechaPago());

        LocalDate fechaPago = LocalDate.of(2025, 3, 12);
        Gasto gastoActualizado = new Gasto(guardado.getId(), fechaGasto, Gasto.Estado.PAGADO, 15000, fechaPago, "Mantenimiento", null);
        gastoDAO.actualizar(gastoActualizado);

        Gasto recuperado = gastoDAO.unicoRegistro("id", guardado.getId());
        assertNotNull(recuperado);
        assertEquals(Gasto.Estado.PAGADO, recuperado.getEstado());
        assertEquals(fechaPago, recuperado.getFechaPago());
    }

    @Test
    @DisplayName("Insertar un gasto con fechaPresupuesto inexistente en PRESUPUESTOS lanza excepción (llave foránea).")
    void insertarGastoConFechaPresupuestoInexistenteEnPresupuestosLanzaExcepcionLlaveForanea() {
        LocalDate fechaPresupuestoInexistente = LocalDate.of(2025, 9, 30);
        Gasto gasto = new Gasto(LocalDate.of(2025, 3, 10), Gasto.Estado.PENDIENTE, 20000, null, "Gasto con presupuesto inexistente", fechaPresupuestoInexistente);

        assertThrows(DBException.class, () -> gastoDAO.insertar(gasto));
    }

    @Test
    @DisplayName("Insertar un gasto con fecha pago es null, el estado es \"PENDIENTE\" también.")
    void insertarGastoConFechaPagoNullEstadoEsPendienteTambien() {
        LocalDate fechaGasto = LocalDate.of(2025, 3, 10);
        Gasto gasto = new Gasto(fechaGasto, Gasto.Estado.PAGADO, 10000, null, "Gasto con fecha de pago null", null);

        gastoDAO.insertar(gasto);

        Gasto guardado = gastoDAO.todos().get(0);
        assertNull(guardado.getFechaPago());
        assertEquals(Gasto.Estado.PENDIENTE, guardado.getEstado());
    }

    @Test
    @DisplayName("Insertar un gasto con fecha de pago le pone el estado que propuso el usuario.")
    void insertarGastoConFechaDePagoLePoneElEstadoQuePropusoElUsuario() {
        LocalDate fechaGasto = LocalDate.of(2025, 3, 10);
        LocalDate fechaPago = LocalDate.of(2025, 3, 11);
        Gasto gasto = new Gasto(fechaGasto, Gasto.Estado.PAGADO, 10000, fechaPago, "Gasto pagado", null);

        gastoDAO.insertar(gasto);

        Gasto guardado = gastoDAO.todos().get(0);
        assertEquals(fechaPago, guardado.getFechaPago());
        assertEquals(Gasto.Estado.PAGADO, guardado.getEstado());
    }

    @Test
    @DisplayName("mapear() convierte fechaPago y fechaPresupuesto nulas en null (no lanza excepción).")
    void mapearConvierteFechaPagoYFechaPresupuestoNulasEnNull() throws SQLException {
        gastoDAO.insertarFila(LocalDate.of(2025, 3, 10), Gasto.Estado.PENDIENTE, 5000, null, "Sin fechas opcionales", null);

        Connection conn = DBConnection.get();
        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM GASTOS")) {
            assertTrue(rs.next());
            Gasto gasto = gastoDAO.mapear(rs);
            assertNotNull(gasto);
            assertNull(gasto.getFechaPago());
            assertNull(gasto.getFechaPresupuesto());
        }
    }

    @Test
    @DisplayName("mapear() convierte el texto de estado al enum correcto (PENDIENTE/PAGADO).")
    void mapearConvierteTextoDeEstadoAlEnumCorrecto() throws SQLException {
        gastoDAO.insertarFila(LocalDate.of(2025, 3, 10), "PENDIENTE", 5000, null, "Gasto pendiente", null);
        gastoDAO.insertarFila(LocalDate.of(2025, 3, 10), "PAGADO", 7000, LocalDate.of(2025, 3, 11), "Gasto pagado", null);

        Connection conn = DBConnection.get();
        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM GASTOS ORDER BY id ASC")) {
            assertTrue(rs.next());
            Gasto pendiente = gastoDAO.mapear(rs);
            assertEquals(Gasto.Estado.PENDIENTE, pendiente.getEstado());

            assertTrue(rs.next());
            Gasto pagado = gastoDAO.mapear(rs);
            assertEquals(Gasto.Estado.PAGADO, pagado.getEstado());
        }
    }
}

