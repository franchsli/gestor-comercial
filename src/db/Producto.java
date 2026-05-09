package db;
public class Producto extends Modelo {

    public Producto(){
        this.columnas = "(nombre, precio_unitario, cantidad)";
    }

    /**
     * Actualiza el inventario del producto con el id dado.
     * @param id El id del producto a cambiar la cantidad en stock.
     * @param nuevoInventario El nuevo inventario del producto.
     */
    public void actualizarInventario(String id, String nuevoInventario){
        actualizar("cantidad", nuevoInventario, "id=" + id);
    }

    /**
     * Reduce el inventario del producto con el id dado.
     * @param id El id del producto.
     * @param numProductosSalientes El número de productos que salen del inventario.
     * @throws ArithmeticException Lanzada si el inventario se torna menor a 0.
     */
    public void reducirInventario(String id, int numProductosSalientes) throws ArithmeticException{
        int inventarioActual = intColumna("cantidad", id);
        int inventarioActualizado = inventarioActual - numProductosSalientes;
        if (inventarioActualizado < 0) {
            throw new ArithmeticException("No hay suficientes productos en el inventario para realizar esta operación.");
        } else {
            String inventarioActualizadoTexto = Integer.toString(inventarioActualizado);
            actualizarInventario(id, inventarioActualizadoTexto);   
        }
    }

    /**
     * Crea un nuevo producto.
     * @param nombre El nombre del producto.
     * @param precioUnitario El precio unitario del producto.
     * @param cantidad La cantidad en inventario del producto.
     */
    public void crear(String nombre, String precioUnitario, String cantidad){
        String[] datos = {nombre, precioUnitario, cantidad};
        insertar(datos);
    }

}
