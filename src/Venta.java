import java.time.LocalDate;
import java.util.Map;

public class Venta extends Modelo {
    String columnas = "(fecha, estado, valor)";
    VentaProducto ventasProductos = new VentaProducto();

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
     * Asocia el producto con el nombre dado a una venta en VENTAS_PRODUCTOS.
     * @param ventaId El id de la venta.
     * @param nombreProducto El nombre del producto.
     * @param cantidadProducto La cantidad del producto en la venta.
     */
    void vender(String ventaId, String nombreProducto, String cantidadProducto){
        Map<String, String> producto = unicoRegistro("nombre", nombreProducto);
        String productoId = producto.get("id");
        ventasProductos.crear(ventaId, productoId, cantidadProducto);
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
        tablaVenta.crear(valorPrevio);
        System.out.println(!tablaVenta.stringColumna("estado", "11").isEmpty());
        System.out.println("VENDER FUNCIONA:");
        tablaVenta.vender("11", "Bolsa para basura 70x90", "5");
        System.out.println(tablaVenta.ventasProductos.contarTodos() == 16);
        DBConnection.cerrar();



    }
}
