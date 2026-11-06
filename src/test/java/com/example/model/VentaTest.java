package com.example.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VentaTest {

    @Test
    @DisplayName("agregarAlValor() suma precioUnitario × cantidad al valor actual de la venta")
    void agregarAlValorSumaPrecioPorCantidadAlValorActual() {
        Venta venta = new Venta(1, LocalDate.of(2025, 3, 10), 1000, Venta.Tipo.EFECTIVO);

        venta.agregarAlValor(1500, 2); // 1500 * 2 = 3000

        assertEquals(4000, venta.getValor());
    }

    @Test
    @DisplayName("agregarAlValor() acumula correctamente cuando se llama varias veces")
    void agregarAlValorAcumulaEnVariasLlamadas() {
        Venta venta = new Venta(1, LocalDate.of(2025, 3, 10), 0, Venta.Tipo.EFECTIVO);

        venta.agregarAlValor(1000, 2); // 2000
        venta.agregarAlValor(500, 3);  // 1500
        venta.agregarAlValor(2500, 1); // 2500

        assertEquals(6000, venta.getValor());
    }

    @Test
    @DisplayName("Instanciar venta con valor negativo lanza error.")
    void instanciarVentaConValorNegativoLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> 
                        new Venta(1, LocalDate.of(2020, 1, 1), -1000, Venta.Tipo.EFECTIVO));
    }

    @Test
    @DisplayName("Instanciar venta con valor no negativo no lanza error.")
    void instanciarVentaConValorNoNegativoNoLanzaError() {
        assertDoesNotThrow(() -> 
                            new Venta(1, LocalDate.of(2020, 1, 1), 0, Venta.Tipo.EFECTIVO));
        assertDoesNotThrow(() -> 
                            new Venta(2, LocalDate.of(2020, 1, 1), 12345, Venta.Tipo.EFECTIVO));
    }
}
