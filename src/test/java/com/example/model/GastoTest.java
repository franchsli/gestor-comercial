package com.example.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GastoTest {

    @Test
    @DisplayName("Instanciar un gasto con valor negativo lanza excepción.")
    void instanciarUnGastoConValorNegativoLanzaExcepcion() {
        LocalDate fecha = LocalDate.of(2025, 3, 10);

        assertThrows(IllegalArgumentException.class, () -> 
                new Gasto(fecha, Gasto.Estado.PENDIENTE, -1, null, "Gasto de prueba", null));
    }

    @Test
    @DisplayName("Instanciar un gasto con valor no negativo no lanza excepción.")
    void instanciarUnGastoConValorNoNegativoNoLanzaExcepcion() {
        LocalDate fecha = LocalDate.of(2025, 3, 10);

        assertDoesNotThrow(() -> new Gasto(fecha, Gasto.Estado.PENDIENTE, 0, null, "Gasto de prueba", null));
        assertDoesNotThrow(() -> new Gasto(fecha, Gasto.Estado.PENDIENTE, 12345, null, "Gasto de prueba", null));
    }
}
