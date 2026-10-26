package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import com.example.model.CierreDiario;
import com.example.model.Gasto;
import com.example.model.Presupuesto;
import com.example.model.Venta;

class CierreDiarioDAOTest {

    private CierreDiarioDAO cierreDiarioDAO;
    private VentaDAO ventaDAO;
    private PresupuestoDAO presupuestoDAO;
    private GastoDAO gastoDAO;

    @BeforeEach
    void setUp() {
        DBDePrueba.preparar();
        cierreDiarioDAO = new CierreDiarioDAO();
        ventaDAO = new VentaDAO();
        presupuestoDAO = new PresupuestoDAO();
        gastoDAO = new GastoDAO();
    }

    @AfterEach
    void tearDown() {
        DBDePrueba.limpiar();
    }

    @Test
    @DisplayName("filtrarPorMes(año, mes) devuelve solo los cierres de ese mes y año.")
    void filtrarPorMesDevuelveSoloLosCierresDeEseMesYAño() {
        ventaDAO.insertar(new Venta(LocalDate.of(2025, 3, 10), 1000, Venta.Tipo.EFECTIVO));
        ventaDAO.insertar(new Venta(LocalDate.of(2025, 3, 25), 2000, Venta.Tipo.CREDITO));
        ventaDAO.insertar(new Venta(LocalDate.of(2025, 4, 5), 3000, Venta.Tipo.EFECTIVO));
        ventaDAO.insertar(new Venta(LocalDate.of(2024, 3, 10), 4000, Venta.Tipo.EFECTIVO));

        List<CierreDiario> cierres = cierreDiarioDAO.filtrarPorMes(2025, 3);

        assertEquals(2, cierres.size());
        assertEquals(LocalDate.of(2025, 3, 10), cierres.get(0).getFecha());
        assertEquals(LocalDate.of(2025, 3, 25), cierres.get(1).getFecha());
    }

    @Test
    @DisplayName("filtrarPorAño(año) devuelve solo los cierres de ese año.")
    void filtrarPorAñoDevuelveSoloLosCierresDeEseAño() {
        ventaDAO.insertar(new Venta(LocalDate.of(2024, 12, 31), 1000, Venta.Tipo.EFECTIVO));
        ventaDAO.insertar(new Venta(LocalDate.of(2025, 1, 15), 2000, Venta.Tipo.CREDITO));
        ventaDAO.insertar(new Venta(LocalDate.of(2025, 6, 20), 3000, Venta.Tipo.EFECTIVO));
        ventaDAO.insertar(new Venta(LocalDate.of(2026, 1, 1), 4000, Venta.Tipo.EFECTIVO));

        List<CierreDiario> cierres = cierreDiarioDAO.filtrarPorAño(2025);

        assertEquals(2, cierres.size());
        assertEquals(LocalDate.of(2025, 1, 15), cierres.get(0).getFecha());
        assertEquals(LocalDate.of(2025, 6, 20), cierres.get(1).getFecha());
    }

    @Test
    @DisplayName("filtrarPorAño(año) devuelve lista vacía cuando no hay datos en el período.")
    void filtrarPorAñoDevuelveListaVaciaCuandoNoHayDatosEnElPeriodo() {
        ventaDAO.insertar(new Venta(LocalDate.of(2024, 5, 10), 1000, Venta.Tipo.EFECTIVO));

        List<CierreDiario> cierres = cierreDiarioDAO.filtrarPorAño(2025);

        assertNotNull(cierres);
        assertTrue(cierres.isEmpty());
    }

    @Test
    @DisplayName("filtrarPorMes(año, mes) devuelve lista vacía cuando no hay datos en el período.")
    void filtrarPorMesDevuelveListaVaciaCuandoNoHayDatosEnElPeriodo() {
        ventaDAO.insertar(new Venta(LocalDate.of(2025, 3, 10), 1000, Venta.Tipo.EFECTIVO));

        List<CierreDiario> cierres = cierreDiarioDAO.filtrarPorMes(2025, 4);

        assertNotNull(cierres);
        assertTrue(cierres.isEmpty());
    }

    @Test
    @DisplayName("mapear() asigna correctamente todas las columnas de la vista.")
    void mapearAsignaCorrectamenteTodasLasColumnasDeLaVista() throws SQLException {
        LocalDate fecha = LocalDate.of(2025, 3, 10);
        presupuestoDAO.insertar(new Presupuesto(fecha, 10000));
        ventaDAO.insertar(new Venta(fecha, 8000, Venta.Tipo.EFECTIVO));
        gastoDAO.insertar(new Gasto(fecha, Gasto.Estado.PAGADO, 2000, fecha, "Insumos", fecha));

        Connection conn = DBConnection.get();
        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM CIERRES_DIARIOS WHERE fecha = '" + fecha + "'")) {
            assertTrue(rs.next());
            CierreDiario cd = cierreDiarioDAO.mapear(rs);

            assertEquals(fecha, cd.getFecha());
            assertEquals(8000, cd.getVentasEfectivo());
            assertEquals(0, cd.getVentasCredito());
            assertEquals(8000, cd.getTotalVentas());
            assertEquals(10000, cd.getPresupuesto());
            assertEquals(80, cd.getCumplimiento());
            assertEquals(2000, cd.getGastos());
        }
    }

    @Test
    @DisplayName("La vista calcula correctamente ventasEfectivo y ventasCredito de un día con ventas de ambos tipos.")
    void laVistaCalculaCorrectamenteVentasEfectivoYVentasCreditoDeUnDiaConVentasDeAmbosTipos() {
        LocalDate fecha = LocalDate.of(2025, 3, 10);
        ventaDAO.insertar(new Venta(fecha, 4000, Venta.Tipo.EFECTIVO));
        ventaDAO.insertar(new Venta(fecha, 1500, Venta.Tipo.EFECTIVO));
        ventaDAO.insertar(new Venta(fecha, 6000, Venta.Tipo.CREDITO));

        CierreDiario cierre = cierreDiarioDAO.unicoRegistro("fecha", fecha);

        assertNotNull(cierre);
        assertEquals(5500, cierre.getVentasEfectivo());
        assertEquals(6000, cierre.getVentasCredito());
    }

    @Test
    @DisplayName("totalVentas es igual a la suma de efectivo y crédito.")
    void totalVentasEsIgualALaSumaDeEfectivoYCredito() {
        LocalDate fecha = LocalDate.of(2025, 3, 10);
        ventaDAO.insertar(new Venta(fecha, 3500, Venta.Tipo.EFECTIVO));
        ventaDAO.insertar(new Venta(fecha, 6500, Venta.Tipo.CREDITO));

        CierreDiario cierre = cierreDiarioDAO.unicoRegistro("fecha", fecha);

        assertNotNull(cierre);
        assertEquals(10000, cierre.getTotalVentas());
        assertEquals(cierre.getVentasEfectivo() + cierre.getVentasCredito(), cierre.getTotalVentas());
    }

    @Test
    @DisplayName("cumplimiento se calcula correctamente respecto al presupuesto del día.")
    void cumplimientoSeCalculaCorrectamenteRespectoAlPresupuestoDelDia() {
        LocalDate fecha = LocalDate.of(2025, 3, 10);
        presupuestoDAO.insertar(new Presupuesto(fecha, 20000));
        ventaDAO.insertar(new Venta(fecha, 15000, Venta.Tipo.EFECTIVO));

        CierreDiario cierre = cierreDiarioDAO.unicoRegistro("fecha", fecha);

        assertNotNull(cierre);
        assertEquals(75, cierre.getCumplimiento());
    }

    @Test
    @DisplayName("gastos refleja la suma de los gastos de ese día.")
    void gastosReflejaLaSumaDeLosGastosDeEseDia() {
        LocalDate fecha = LocalDate.of(2025, 3, 10);
        ventaDAO.insertar(new Venta(fecha, 5000, Venta.Tipo.EFECTIVO));
        gastoDAO.insertar(new Gasto(fecha, Gasto.Estado.PAGADO, 1200, fecha, "Papelería", null));
        gastoDAO.insertar(new Gasto(fecha, Gasto.Estado.PENDIENTE, 800, null, "Transporte", null));

        CierreDiario cierre = cierreDiarioDAO.unicoRegistro("fecha", fecha);

        assertNotNull(cierre);
        assertEquals(2000, cierre.getGastos());
    }

    @Test
    @DisplayName("Un día sin ventas no aparece.")
    void unDiaSinVentasNoAparece() {
        LocalDate fechaSinVentas = LocalDate.of(2025, 3, 10);
        LocalDate fechaConVentas = LocalDate.of(2025, 3, 11);

        presupuestoDAO.insertar(new Presupuesto(fechaSinVentas, 50000));
        gastoDAO.insertar(new Gasto(fechaSinVentas, Gasto.Estado.PENDIENTE, 10000, null, "Gasto", null));

        ventaDAO.insertar(new Venta(fechaConVentas, 20000, Venta.Tipo.EFECTIVO));

        CierreDiario cierreSinVentas = cierreDiarioDAO.unicoRegistro("fecha", fechaSinVentas);
        assertNull(cierreSinVentas);

        List<CierreDiario> todos = cierreDiarioDAO.todos();
        assertEquals(1, todos.size());
        assertEquals(fechaConVentas, todos.get(0).getFecha());
    }
}
