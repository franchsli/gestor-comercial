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
        Presupuesto presupuesto = new Presupuesto(fecha, Presupuesto.Tipo.INGRESO, 50000);

        presupuestoDAO.insertar(presupuesto);

        List<Presupuesto> presupuestos = presupuestoDAO.todos();
        assertEquals(1, presupuestos.size());

        Presupuesto guardado = presupuestos.get(0);
        assertEquals(fecha, guardado.getFecha());
        assertEquals(Presupuesto.Tipo.INGRESO, guardado.getTipo());
        assertEquals(50000, guardado.getValor());
    }

    @Test
    @DisplayName("insertar() lanza excepción si ya existe un presupuesto con la misma fecha y tipo")
    void insertarLanzaExcepcionSiYaExistePresupuestoConMismaFechaYTipo() {
        LocalDate fecha = LocalDate.of(2025, 3, 1);
        presupuestoDAO.insertar(new Presupuesto(fecha, Presupuesto.Tipo.INGRESO, 50000));

        assertThrows(DBException.class,
                () -> presupuestoDAO.insertar(new Presupuesto(fecha, Presupuesto.Tipo.INGRESO, 60000)));

        assertEquals(1, presupuestoDAO.todos().size());
    }

    @Test
    @DisplayName("actualizar(presupuesto) cambia la fecha, tipo y valor.")
    void actualizarCambiaLaFechaElTipoYElValor() {
        LocalDate fechaAnterior = LocalDate.of(2025, 3, 1);
        LocalDate fechaNueva = LocalDate.of(2025, 3, 15);
        presupuestoDAO.insertar(new Presupuesto(fechaAnterior, Presupuesto.Tipo.INGRESO, 50000));

        int id = presupuestoDAO.unicoRegistro("fecha", fechaAnterior).getId();
        Presupuesto actualizado = new Presupuesto(id, fechaNueva, Presupuesto.Tipo.EGRESO, 80000);
        presupuestoDAO.actualizar(actualizado);

        assertEquals(1, presupuestoDAO.todos().size());
        assertNull(presupuestoDAO.unicoRegistro("fecha", fechaAnterior));

        Presupuesto recuperado = presupuestoDAO.unicoRegistro("id", id);
        assertNotNull(recuperado);
        assertEquals(id, recuperado.getId());
        assertEquals(fechaNueva, recuperado.getFecha());
        assertEquals(Presupuesto.Tipo.EGRESO, recuperado.getTipo());
        assertEquals(80000, recuperado.getValor());
    }
}
