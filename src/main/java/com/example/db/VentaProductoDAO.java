package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VentaProductoDAO extends DAO<VentaProductoo> {

    public VentaProductoDAO(){
        super("VENTAS_PRODUCTOS", "(id_venta, id_producto, cantidad_producto)");
    }

    @Override
    public VentaProductoo mapear(ResultSet resultSet) throws SQLException {
        return new VentaProductoo(resultSet.getInt("id_venta"), resultSet.getInt("id_producto"), 
                                     resultSet.getInt("cantidad_producto"));
    }

    @Override
    public void actualizar(VentaProductoo ventaProducto){
        String sql = "UPDATE " + nombreTabla + " SET cantidad_producto = ? WHERE id_venta = ? AND id_producto = ?";
        ejecutar(sql, ventaProducto.getCantidadProducto(), ventaProducto.getIdVenta(), ventaProducto.getIdProducto());
    }

    @Override
    public void insertar(VentaProductoo ventaProducto){
        int numProductosEnVenta = ventaProducto.getCantidadProducto();
        int idProducto = ventaProducto.getIdProducto();

        ProductoDAO productoDAO = new ProductoDAO();
        Productoo productoEnVenta = productoDAO.unicoRegistro("id", idProducto);
        productoEnVenta.reducirCantidad(numProductosEnVenta);
        productoDAO.actualizar(productoEnVenta);

        insertarFila(ventaProducto.getIdVenta(), idProducto, numProductosEnVenta);

    }
    
}
