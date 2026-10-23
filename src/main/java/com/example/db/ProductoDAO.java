package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAO extends DAOCrud<Producto> {

    public ProductoDAO(){
        super("PRODUCTOS", "(nombre, precio_unitario, cantidad)");
    }

    @Override
    public Producto mapear(ResultSet resultSet) throws SQLException {
        return new Producto(resultSet.getInt("id"), resultSet.getString("nombre"),
            resultSet.getInt("precio_unitario"), resultSet.getInt("cantidad"));
    }

    @Override
    public void actualizar(Producto producto){
        String sql = "UPDATE " + nombreTabla + " SET nombre = ?, precio_unitario = ?, cantidad = ? WHERE id = ?";
        ejecutar(sql, producto.getNombre(), producto.getPrecioUnitario(), producto.getCantidad(), producto.getId());
    }

    @Override 
    public void insertar(Producto producto){
        insertarFila(producto.getNombre(), producto.getPrecioUnitario(), producto.getCantidad());
    }
    
}
