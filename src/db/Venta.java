package db;
import java.util.Map;

public class Venta extends Modelo {
    private VentaProducto ventasProductos = new VentaProducto();
    private Producto productos = new Producto();

    public Venta(){
        this.columnas = "(fecha, estado, valor, tipo)";
    }

    /**
     * Actualiza el valor de una venta.
     * @param id El id de la venta a actualizar.
     * @param nuevoValor El nuevo valor de la venta.
     */
    public void actualizarValor(String id, String nuevoValor){
        String condicion = "id=" + id;
        actualizar("valor", nuevoValor, condicion);
    }

    /**
     * Incrementa el valor de una venta.
     * @param id El id de la venta a la cual incrementar el valor.
     * @param valor El valor a sumarle a la venta.
     */
    public void incrementarValor(String id, String valor){
        int valorActual = intColumna("valor", id);
        int valorIncrementado = valorActual + Integer.parseInt(valor);
        String valorIncrementadoString = Integer.toString(valorIncrementado);
        actualizarValor(id, valorIncrementadoString);
    }

    /**
     * Crea una venta en la base de datos.
     * @param fecha La fecha de la venta en formato 'AAAA-MM-DD'.
     * @param estado El estado de la venta.
     * @param valor El valor total de la venta.
     * @param tipo El tipo de la venta ('EFECTIVO' o 'CREDITO')
     */
    public void crear(String fecha, String estado, String valor, String tipo){
        String[] datos = {fecha, estado, valor, tipo};
        insertar(datos);
    }

    /**
     * Asocia el producto con el nombre dado a una venta en VENTAS_PRODUCTOS.
     * @param ventaId El id de la venta.
     * @param nombreProducto El nombre del producto.
     * @param cantidadProducto La cantidad del producto en la venta.
     */
    public void vender(String ventaId, String nombreProducto, String cantidadProducto){
        Map<String, String> producto = productos.unicoRegistro("nombre", nombreProducto);
        String productoId = producto.get("id");
        ventasProductos.crear(ventaId, productoId, cantidadProducto);
        int precioProducto = Integer.parseInt(producto.get("precio_unitario"));
        int cantidadProductoInt = Integer.parseInt(cantidadProducto);
        String totalVentaProducto = Integer.toString(precioProducto * cantidadProductoInt);
        incrementarValor(ventaId, totalVentaProducto);
    }

}
