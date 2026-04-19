import java.time.LocalDate;
import java.util.Map;

public class Venta extends Modelo {
    Producto tablaProductos = new Producto();
    
    void actualizarFecha(String id, String nuevaFecha){
        actualizar("fecha", nuevaFecha, id);
    }

    void actualizarEstado(String id, String nuevoEstado){
        actualizar("estado", nuevoEstado, id);
    }

    void actualizarValor(String id, String nuevoValor){
        actualizar("valor", nuevoValor, id);
    }


    void crear(String fecha, String estado, String valor){
        String[] datos = {fecha, estado, valor};
        insertar(datos);
    }

    void crear(String estado, String valor){
        LocalDate fechahoy = LocalDate.now();
        String fechaHoyTexto = fechahoy.toString();
        crear(fechaHoyTexto, estado, valor);
    }

    void crear(String valor){
        crear("PENDIENTE", valor);
    }

    void crearVentaProducto(String ventaId, String productoId, String cantidadProducto){
        String[] datos = {ventaId, productoId};
        insertar(datos);
    }

    void vender(String ventaId, String nombreProducto, String cantidadProducto){
        Map<String, String> producto = unicoRegistro("nombre", nombreProducto);
        String productoId = producto.get("id");
        crearVentaProducto(ventaId, productoId, cantidadProducto);
    }
}
