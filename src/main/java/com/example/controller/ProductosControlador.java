package com.example.controller;

import java.util.List;

import com.example.db.ProductoDAO;
import com.example.model.Producto;

public class ProductosControlador extends ControladorCrud<Producto> {
    private final ProductoDAO productoDAO;

    public ProductosControlador() {
        this(new ProductoDAO());
    }

    private ProductosControlador(ProductoDAO productoDAO){
        super(productoDAO);
        this.productoDAO = productoDAO;
        
    }

    /**
     * Retorna todos los productos cuyos nombres
     * sean parecidos al nombre proporcionado.
     * @param nombre El nombre a usar en la busqueda.
     * @return Los productos con un nombre parecido.
     */
    public List<Producto> buscarPorNombre(String nombre){
        return this.productoDAO.buscarPorNombre(nombre);
    }
    
}
