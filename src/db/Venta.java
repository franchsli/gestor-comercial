package db;
import java.util.Map;

public class Venta extends Modelo {
    private VentaProducto ventasProductos = new VentaProducto();
    private Producto productos = new Producto();

    public Venta(){
        this.columnas = "(fecha, estado, valor, tipo)";
    }

    /**
     * Actualiza la fecha de una venta.
     * @param id El id de la venta a actualizar.
     * @param nuevaFecha La nueva fecha de la venta.
     */
    public void actualizarFecha(String id, String nuevaFecha){
        String condicion = "id=" + id;
        actualizar("fecha", nuevaFecha, condicion);
    }

    /**
     * Actualiza el estado de una venta.
     * @param id El id de la venta a actualizar.
     * @param nuevoEstado El nuevo estado de la venta.
     */
    public void actualizarEstado(String id, String nuevoEstado){
        String condicion = "id=" + id;
        actualizar("estado", nuevoEstado, condicion);
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
     * Reduce el valor de una venta.
     * @param id El id de la venta a la cual reducir el valor.
     * @param valor El valor a restarle a la venta.
     */
    public void reducirValor(String id, String valor){
        int valorActual = intColumna("valor", id);
        int valorReducido = valorActual - Integer.parseInt(valor);
        String valorReducidoString = Integer.toString(valorReducido);
        actualizarValor(id, valorReducidoString);
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

    public static void main(String[] args) {
        Venta tablaVenta = new Venta();
        System.out.println("ACTUALIZAR FECHA FUNCIONA:");
        String fechaPrevia = tablaVenta.stringColumna("fecha", "1");
        tablaVenta.actualizarFecha("1", "2000-11-02");
        System.out.println(fechaPrevia != tablaVenta.stringColumna("fecha", "1"));
        System.out.println("ACTUALIZAR ESTADO FUNCIONA:");
        String estadoPrevio = tablaVenta.stringColumna("estado", "1");
        tablaVenta.actualizarEstado("1", "PENDIENTE");
        System.out.println(estadoPrevio != tablaVenta.stringColumna("estado", "1"));
        System.out.println("ACTUALIZAR VALOR FUNCIONA");
        String valorPrevio = tablaVenta.stringColumna("valor", "1");
        tablaVenta.actualizarValor("1", "1");
        System.out.println(valorPrevio != tablaVenta.stringColumna("valor", "1"));
        System.out.println("CREAR VENTA FUNCIONA:");
        tablaVenta.crear("2026-02-02", "PENDIENTE", valorPrevio, "EFECTIVO");
        System.out.println(!tablaVenta.stringColumna("estado", "11").isEmpty());
        System.out.println("VENDER FUNCIONA:");
        tablaVenta.vender("11", "Bolsa para basura 70x90", "5");
        // TODO: Añade más pruebas...
        DBConnection.cerrar();



    }
}
