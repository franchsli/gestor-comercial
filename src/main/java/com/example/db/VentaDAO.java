package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VentaDAO extends DAO<Ventaa> {
    
    public VentaDAO(){
        super("VENTAS", "(fecha, valor, tipo)");
    }

    @Override
    public Ventaa mapear(ResultSet resultSet) throws SQLException {
        return new Ventaa(fechaDeColumna(resultSet, "fecha"), resultSet.getInt("valor"), 
                            Ventaa.Tipo.valueOf(resultSet.getString("tipo")));
    }

    @Override
    public void actualizar(Ventaa venta){
        String sql = "UPDATE " + nombreTabla + " SET fecha = ?, valor = ?, tipo = ? WHERE id = ?";
        ejecutar(sql, venta.getFecha(), venta.getValor(), venta.getTipo(), venta.getId());
    }

    @Override
    public void insertar(Ventaa venta){
        insertarFila(venta.getFecha(), venta.getValor(), venta.getTipo());
    }

    /**
     * Asocia el producto con el nombre dado a una venta en VENTAS_PRODUCTOS.
     * @param venta El objeto de la entidad venta.
     * @param nombreProducto El nombre del producto.
     * @param cantidadProducto La cantidad del producto en la venta.
     */
    public void vender(Ventaa venta, String nombreProducto, int cantidadProducto){
        ProductoDAO productoDAO = new ProductoDAO();
        VentaProductoDAO ventaProductoDAO = new VentaProductoDAO();

        Productoo productoAVender = productoDAO.unicoRegistro("nombre", nombreProducto);
        VentaProductoo ventaProducto = new VentaProductoo(venta.getId(), productoAVender.getId(), cantidadProducto);
        ventaProductoDAO.insertar(ventaProducto);

        venta.agregarAlValor(productoAVender.getPrecioUnitario(), cantidadProducto);
        actualizar(venta);
    }
}
