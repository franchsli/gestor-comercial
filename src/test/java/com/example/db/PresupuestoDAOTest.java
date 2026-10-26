package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.Presupuesto;

class PresupuestoDAOTest {

    private PresupuestoDAO presupuestoDAO;

    @BeforeEach
    void setUp() {
        DBDePrueba.preparar();
        presupuestoDAO = new PresupuestoDAO();
    }

    @AfterEach
    void tearDown() {
        DBDePrueba.limpiar();
    }

    @Test
    @DisplayName("insertar() guarda el presupuesto con su fecha y valor.")
    void insertarGuardaPresupuestoConSuFechaYValor() {
        LocalDate fecha = LocalDate.of(2025, 3, 1);
        Presupuesto presupuesto = new Presupuesto(fecha, 50000);

        presupuestoDAO.insertar(presupuesto);

        List<Presupuesto> presupuestos = presupuestoDAO.todos();
        assertEquals(1, presupuestos.size());

        Presupuesto guardado = presupuestos.get(0);
        assertEquals(fecha, guardado.getFecha());
        assertEquals(50000, guardado.getValor());
    }

    @Test
    @DisplayName("insertar() lanza excepción si ya existe un presupuesto con la misma fecha (PK duplicada).")
    void insertarLanzaExcepcionSiYaExistePresupuestoConMismaFecha() {
        LocalDate fecha = LocalDate.of(2025, 3, 1);
        presupuestoDAO.insertar(new Presupuesto(fecha, 50000));

        assertThrows(DBException.class,
                () -> presupuestoDAO.insertar(new Presupuesto(fecha, 60000)));

        assertEquals(1, presupuestoDAO.todos().size());
    }

    @Test
    @DisplayName("actualizar(presupuesto, fechaAnterior) modifica el valor del registro correcto.")
    void actualizarConFechaAnteriorModificaElValorDelRegistroCorrecto() {
        LocalDate fecha1 = LocalDate.of(2025, 3, 1);
        LocalDate fecha2 = LocalDate.of(2025, 4, 1);
        presupuestoDAO.insertar(new Presupuesto(fecha1, 50000));
        presupuestoDAO.insertar(new Presupuesto(fecha2, 75000));

        Presupuesto modificado = new Presupuesto(fecha1, 65000);
        presupuestoDAO.actualizar(modificado, fecha1);

        Presupuesto recuperado1 = presupuestoDAO.unicoRegistro("fecha", fecha1);
        assertNotNull(recuperado1);
        assertEquals(65000, recuperado1.getValor());

        Presupuesto recuperado2 = presupuestoDAO.unicoRegistro("fecha", fecha2);
        assertNotNull(recuperado2);
        assertEquals(75000, recuperado2.getValor());
    }

    @Test
    @DisplayName("actualizar(presupuesto, fechaAnterior) cambia la fecha (PK) del registro sin duplicarlo.")
    void actualizarConFechaAnteriorCambiaLaFechaSinDuplicarlo() {
        LocalDate fechaAnterior = LocalDate.of(2025, 3, 1);
        LocalDate fechaNueva = LocalDate.of(2025, 3, 15);
        presupuestoDAO.insertar(new Presupuesto(fechaAnterior, 50000));

        Presupuesto actualizado = new Presupuesto(fechaNueva, 55000);
        presupuestoDAO.actualizar(actualizado, fechaAnterior);

        assertEquals(1, presupuestoDAO.todos().size());
        assertNull(presupuestoDAO.unicoRegistro("fecha", fechaAnterior));

        Presupuesto recuperado = presupuestoDAO.unicoRegistro("fecha", fechaNueva);
        assertNotNull(recuperado);
        assertEquals(fechaNueva, recuperado.getFecha());
        assertEquals(55000, recuperado.getValor());
    }

    @Test
    @DisplayName("actualizar(presupuesto) solo cambia el valor del presupuesto.")
    void actualizarSoloCambiaElValorDelPresupuesto() {
        LocalDate fecha = LocalDate.of(2025, 3, 1);
        presupuestoDAO.insertar(new Presupuesto(fecha, 50000));

        Presupuesto actualizado = new Presupuesto(fecha, 80000);
        presupuestoDAO.actualizar(actualizado);

        Presupuesto recuperado = presupuestoDAO.unicoRegistro("fecha", fecha);
        assertNotNull(recuperado);
        assertEquals(fecha, recuperado.getFecha());
        assertEquals(80000, recuperado.getValor());
        assertEquals(1, presupuestoDAO.todos().size());
    }
}
