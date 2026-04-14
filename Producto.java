public class Producto extends Modelo {

    void actualizarNombre(String id, String nuevoNombre){
        actualizar("nombre", nuevoNombre, "id=" + id);
    }

    void actualizarPrecioUnitario(String id, String nuevoPrecio){
        actualizar("precio_unitario", nuevoPrecio, "id=" + id);
    }

    void actualizarInventario(String id, String nuevoInventario){
        actualizar("cantidad", nuevoInventario, "id=" + id);
    }

    void reducirInventario(String id, int numProductosSalientes) throws ArithmeticException{
        int inventarioActual = intColumna("cantidad", id);
        int inventarioActualizado = inventarioActual - numProductosSalientes;
        if (inventarioActualizado < 0) {
            throw new ArithmeticException("No hay suficientes productos en el inventario para realizar esta operación.");
        } else {
            String inventarioActualizadoTexto = Integer.toString(inventarioActualizado);
            actualizarInventario(id, inventarioActualizadoTexto);   
        }
    }

    void aumentarInventario(String id, int numProductosEntrantes){
        int inventarioActual = intColumna("cantidad", id);
        String inventarioActualizado = Integer.toString(inventarioActual + numProductosEntrantes);
        actualizarInventario(id, inventarioActualizado);
    }

    public static void main(String[] args) {
        Producto producto = new Producto();
        System.out.println(producto.stringColumna("nombre", "1"));
        System.out.println(producto.intColumna("cantidad", "1"));
        DBConnection.cerrar();
    }
}
