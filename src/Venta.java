import java.time.LocalDate;
import java.util.Map;

public class Venta extends Modelo {
    
    /**
     * Actualiza la fecha de una venta.
     * @param id El id de la venta a actualizar.
     * @param nuevaFecha La nueva fecha de la venta.
     */
    void actualizarFecha(String id, String nuevaFecha){
        actualizar("fecha", nuevaFecha, id);
    }

    /**
     * Actualiza el estado de una venta.
     * @param id El id de la venta a actualizar.
     * @param nuevaFecha El nuevo estado de la venta.
     */
    void actualizarEstado(String id, String nuevoEstado){
        actualizar("estado", nuevoEstado, id);
    }

    /**
     * Actualiza el valor de una venta.
     * @param id El id de la venta a actualizar.
     * @param nuevaFecha El nuevo valor de la venta.
     */
    void actualizarValor(String id, String nuevoValor){
        actualizar("valor", nuevoValor, id);
    }


    /**
     * Crea una venta en la base de datos.
     * @param fecha La fecha de la venta en formato 'AAAA-MM-DD'.
     * @param estado El estado de la venta.
     * @param valor El valor total de la venta.
     */
    void crear(String fecha, String estado, String valor){
        String[] datos = {fecha, estado, valor};
        insertar(datos);
    }

    /**
     * Crea una venta en la base de datos.
     * @param estado El estado de la venta.
     * @param valor El valor total de la venta.
     */
    void crear(String estado, String valor){
        LocalDate fechahoy = LocalDate.now();
        String fechaHoyTexto = fechahoy.toString();
        crear(fechaHoyTexto, estado, valor);
    }

    /**
     * Crea una venta en la base de datos.
     * @param valor El valor total de la venta.
     */
    void crear(String valor){
        crear("PENDIENTE", valor);
    }

    /**
     * Crea un nuevo registro en la tabla intermedia VENTAS_PRODUCTOS.
     * @param ventaId El id de la venta.
     * @param productoId El id del producto.
     * @param cantidadProducto La cantidad del producto en la venta.
     */
    void crearVentaProducto(String ventaId, String productoId, String cantidadProducto){
        String[] datos = {ventaId, productoId, cantidadProducto};
        insertar("VENTAS_PRODUCTOS",datos);
    }

    /**
     * Asocia el producto con el nombre dado a una venta en VENTAS_PRODUCTOS.
     * @param ventaId El id de la venta.
     * @param nombreProducto El nombre del producto.
     * @param cantidadProducto La cantidad del producto en la venta.
     */
    void vender(String ventaId, String nombreProducto, String cantidadProducto){
        Map<String, String> producto = unicoRegistro("nombre", nombreProducto);
        String productoId = producto.get("id");
        crearVentaProducto(ventaId, productoId, cantidadProducto);
    }

    public static void main(String[] args) {
        // TODO: PROBAR QUE LOS MÉTODOS FUNCIONEN
    }
}
