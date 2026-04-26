package db;
public class VentaProducto extends Modelo {
    private Producto productos = new Producto();

    public VentaProducto(){
        this.nombreTabla = "VENTAS_PRODUCTOS";
        this.columnas = "(id_venta, id_producto, cantidad_producto)";
    }

    /**
     * Crea un nuevo registro en la tabla VENTAS_PRODUCTOS y
     * reduce el inventario de PRODUCTOS.
     * @param ventaId El id de la venta.
     * @param productoId El id del producto.
     * @param cantidadProducto La cantidad del producto en la venta.
     */
    public void crear(String ventaId, String productoId, String cantidadProducto){
        int cantidad = Integer.parseInt(cantidadProducto);
        productos.reducirInventario(productoId, cantidad);
        String[] datos = {ventaId, productoId, cantidadProducto};
        insertar(datos);
    }
}
