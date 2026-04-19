public class VentaProducto extends Modelo {

    VentaProducto(){
        this.nombreTabla = "VENTAS_PRODUCTOS";
        this.columnas = "(id_venta, id_producto, cantidad_producto)";
    }

    /**
     * Crea un nuevo registro en la tabla VENTAS_PRODUCTOS.
     * @param ventaId El id de la venta.
     * @param productoId El id del producto.
     * @param cantidadProducto La cantidad del producto en la venta.
     */
    void crear(String ventaId, String productoId, String cantidadProducto){
        String[] datos = {ventaId, productoId, cantidadProducto};
        insertar(datos);
    }
}
