package com.example.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductoTest {

    @Test
    @DisplayName("reducirCantidad() lanza error si se quiere reducir más de lo que hay en inventario")
    void reducirCantidadLanzaErrorSiSeReduceMasDeLoDisponible() {
        Producto producto = new Producto(1, "Gancho", 1500, 10);

        assertThrows(IllegalArgumentException.class, () -> producto.reducirCantidad(11));
    }

    @Test
    @DisplayName("reducirCantidad() permite que el inventario llegue exactamente a 0")
    void reducirCantidadPermiteLlegarACero() {
        Producto producto = new Producto(1, "Gancho", 1500, 10);

        producto.reducirCantidad(10);

        assertEquals(0, producto.getCantidad());
    }

    @Test
    @DisplayName("reducirCantidad() descuenta correctamente cuando queda inventario mayor a 0")
    void reducirCantidadDescuentaCuandoQuedaInventario() {
        Producto producto = new Producto(1, "Gancho", 1500, 10);

        producto.reducirCantidad(4);

        assertEquals(6, producto.getCantidad());
    }
}
