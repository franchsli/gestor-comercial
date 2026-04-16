public class Venta extends Modelo {
    
    void actualizarFecha(String id, String nuevaFecha){
        actualizar("fecha", nuevaFecha, id);
    }

    void actualizarEstado(String id, String nuevoEstado){
        actualizar("estado", nuevoEstado, id);
    }

    void actualizarValor(String id, String nuevoValor){
        actualizar("valor", nuevoValor, id);
    }
}
