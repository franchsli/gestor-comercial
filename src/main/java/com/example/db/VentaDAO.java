package com.example.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.example.model.Producto;
import com.example.model.Venta;
import com.example.model.VentaProducto;

public class VentaDAO extends DAOCrud<Venta> {
    
    public VentaDAO(){
        super("VENTAS", "(fecha, valor, tipo)");
    }

    @Override
    public Venta mapear(ResultSet resultSet) throws SQLException {
        return new Venta(resultSet.getInt("id"), fechaDeColumna(resultSet, "fecha"), resultSet.getInt("valor"), 
                            Venta.Tipo.valueOf(resultSet.getString("tipo")));
    }

    @Override
    public void actualizar(Venta venta){
        String sql = "UPDATE " + nombreTabla + " SET fecha = ?, valor = ?, tipo = ? WHERE id = ?";
        ejecutar(sql, venta.getFecha(), venta.getValor(), venta.getTipo(), venta.getId());
    }

    @Override
    public void insertar(Venta venta){
        venta.setId(insertarFila(venta.getFecha(), venta.getValor(), venta.getTipo()));   
    }

    /**
     * Asocia el producto con el nombre dado a una venta en VENTAS_PRODUCTOS.
     * @param venta El objeto de la entidad venta.
     * @param producto El objeto de la entidad producto.
     * @param cantidadProducto La cantidad del producto en la venta.
     */
    public void vender(Venta venta, Producto producto, int cantidadProducto){
        VentaProductoDAO ventaProductoDAO = new VentaProductoDAO();

        VentaProducto ventaProducto = new VentaProducto(venta.getId(), producto.getId(), cantidadProducto);
        ventaProductoDAO.insertar(ventaProducto);

        venta.agregarAlValor(producto.getPrecioUnitario(), cantidadProducto);
        actualizar(venta);
    }

    /**
     * Vende los productos dados con las cantidades dadas
     * en una sola transacción.
     * @param venta El objeto de la entidad venta.
     * @param productos La lista de productos a vender.
     * @param cantidadesPorProducto La lista de cantidades por producto a vender.
     */
    public void venderProductos(Venta venta, List<Producto> productos, List<Integer> cantidadesPorProducto) throws SQLException {
        Connection connection = DBConnection.get();
        boolean valorAutoCommitPrevio = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            for (int i = 0; i < productos.size(); i++) {
                Producto producto = productos.get(i);
                int cantidad = cantidadesPorProducto.get(i);
                this.vender(venta, producto, cantidad);
            }
            connection.commit();
        // Hace rollback sin importar que excepción surgió.
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(valorAutoCommitPrevio);
        }
    }
}
