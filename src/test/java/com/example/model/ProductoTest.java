package com.example.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

    @Test
    @DisplayName("Instanciar un producto con precio unitario negativo lanza error.")
    void instanciarUnProductoConPrecioUnitarioNegativoLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> 
                        new Producto(1, "Envase plástico.", -1000, 1));
    }

    @Test
    @DisplayName("Instanciar un producto con precio unitario no negativo no lanza error.")
    void instanciarUnProductoConPrecioUnitarioNoNegativoNoLanzaError() {
        String nombre = "Envase plástico";

        assertDoesNotThrow(() -> new Producto(1, nombre, 0, 1));
        assertDoesNotThrow(() -> new Producto(2, nombre, 12345, 12345));
    }

    @Test
    @DisplayName("Instanciar un producto con cantidad negativa lanza error.")
    void instanciarUnProductoConCantidadNegativaLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> 
                        new Producto(1, "Envase plástico.", 1000, -1));
    }

    @Test
    @DisplayName("Instanciar un producto con cantidad no negativa no lanza error.")
    void instanciarUnProductoConCantidadNoNegativaNoLanzaError() {
        String nombre = "Envase plástico";

        assertDoesNotThrow(() -> new Producto(1, nombre, 1, 0));
        assertDoesNotThrow(() -> new Producto(2, nombre, 12345, 12345));
    }
}
