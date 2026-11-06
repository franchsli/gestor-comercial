package com.example.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class VentaProductoTest {

    @Test
    @DisplayName("Instanciar VentaProducto con cantidadProducto menor o igual a cero lanza error.")
    void instanciarVentaProductoConMenorOIgualACeroLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> 
                        new VentaProducto(1, 1, -1000));
        assertThrows(IllegalArgumentException.class, () -> 
                        new VentaProducto(1, 1, 0));
    }

    @Test
    @DisplayName("Instanciar VentaProducto con cantidadProducto mayor a cero no lanza error.")
    void instanciarVentaConMayorACeroNoLanzaError() {
        assertDoesNotThrow(() -> 
                            new VentaProducto(1, 1, 12345));
        assertDoesNotThrow(() -> 
                            new VentaProducto(2, 2, 1));
    }
}
