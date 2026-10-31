package com.example.db;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.model.Producto;
import com.example.model.VentaProducto;

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

    /**
     * Retorna todos los productos cuyos nombres
     * sean parecidos al nombre proporcionado.
     * @param nombre El nombre a usar en la busqueda.
     * @return Los productos con un nombre parecido.
     */
    public List<Producto> buscarPorNombre(String nombre){
        String condicion = "nombre LIKE '%' || ? || '%'";
        return todos(condicion, nombre);
    }

    /**
     * Devuelve si el producto con el id dado tiene ventas.
     * @param id El id del producto.
     * @return Si tiene ventas o no.
     */
    public boolean tieneVentas(String id){
        VentaProductoDAO ventaProductoDAO = new VentaProductoDAO();
        List<VentaProducto> ventas = ventaProductoDAO.todos("id_producto = ?", id);
        return !ventas.isEmpty();

    }

    @Override
    public void borrarTodos(String condicion, Object... params){
        String idProducto = (String) params[0];
        if (tieneVentas(idProducto)){
            throw new IllegalStateException("No se puede borrar el producto al tener ventas asociadas.");
        }
        else super.borrarTodos(condicion, params);
    }
    
}
