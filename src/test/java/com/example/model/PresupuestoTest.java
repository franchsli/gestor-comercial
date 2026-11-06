package com.example.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PresupuestoTest {

    @Test
    @DisplayName("Instanciar un presupuesto con valor negativo lanza error.")
    void instanciarUnPresupuestoConValorNegativoLanzaError() {
        LocalDate fecha = LocalDate.of(2020, 12, 01);

        assertThrows(IllegalArgumentException.class, () -> 
                        new Presupuesto(1, fecha, Presupuesto.Tipo.INGRESO, -1));
    }

    @Test
    @DisplayName("Instanciar un presupuesto con valor no negativo no lanza error.")
    void instanciarUnPresupuestoConValorNoNegativoLanzaError() {
        LocalDate fecha = LocalDate.of(2020, 12, 01);

        assertDoesNotThrow(() -> new Presupuesto(1, fecha, Presupuesto.Tipo.INGRESO, 0));
        assertDoesNotThrow(() -> new Presupuesto(2, fecha, Presupuesto.Tipo.INGRESO, 12345));
    }
}
