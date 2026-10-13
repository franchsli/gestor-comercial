package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAO extends DAO<Productoo> {

    public ProductoDAO(){
        super("PRODUCTOS", "(nombre, precio_unitario, cantidad)");
    }

    @Override
    public Productoo mapear(ResultSet resultSet) throws SQLException {
        return new Productoo(resultSet.getInt("id"), resultSet.getString("nombre"),
            resultSet.getInt("precio_unitario"), resultSet.getInt("cantidad"));
    }

    @Override
    public void actualizar(Productoo producto){
        String sql = "UPDATE " + nombreTabla + " SET nombre = ?, precio_unitario = ?, cantidad = ? WHERE id = ?";
        ejecutar(sql, producto.getNombre(), producto.getPrecioUnitario(), producto.getCantidad(), producto.getId());
    }
    
}
