package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.Producto;

class ProductoDAOTest {
    private ProductoDAO productoDAO;

    @BeforeEach
    void setUp() {
        DBDePrueba.preparar();
        productoDAO = new ProductoDAO();
    }

    @AfterEach
    void tearDown() {
        DBDePrueba.limpiar();
    }

    @Test
    @DisplayName("insertar() guarda el producto y se recupera con todos sus campos correctos")
    void insertarGuardaProductoConTodosSusCampos() {
        productoDAO.insertar(new Producto(0, "Gancho plástico", 1500, 40));

        List<Producto> productos = productoDAO.todos();

        assertEquals(1, productos.size());
        Producto guardado = productos.get(0);
        assertEquals(1, guardado.getId());
        assertEquals("Gancho plástico", guardado.getNombre());
        assertEquals(1500, guardado.getPrecioUnitario());
        assertEquals(40, guardado.getCantidad());
    }

    @Test
    @DisplayName("actualizar() modifica nombre, precio y cantidad del producto indicado")
    void actualizarModificaNombrePrecioYCantidad() {
        productoDAO.insertar(new Producto(0, "Plato plástico", 2000, 10));
        int id = productoDAO.todos().get(0).getId();

        productoDAO.actualizar(new Producto(id, "Platón grande", 3500, 25));

        List<Producto> productos = productoDAO.todos();
        assertEquals(1, productos.size());
        Producto actualizado = productos.get(0);
        assertEquals(id, actualizado.getId());
        assertEquals("Platón grande", actualizado.getNombre());
        assertEquals(3500, actualizado.getPrecioUnitario());
        assertEquals(25, actualizado.getCantidad());
    }
}