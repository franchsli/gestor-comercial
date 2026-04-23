package db;
public class Producto extends Modelo {
    // TODO: AÑADIR MÉTODO PARA CREAR PRODUCTO

    Producto(){
        this.columnas = "(nombre, precio_unitario, cantidad)";
    }

    /**
     * Actualiza el nombre del producto con el id dado.
     * @param id El id del producto a cambiar el nombre.
     * @param nuevoNombre El nuevo nombre del producto.
     */
    void actualizarNombre(String id, String nuevoNombre){
        actualizar("nombre", nuevoNombre, "id=" + id);
    }

    /**
     * Actualiza el precio unitario del producto con el id dado.
     * @param id El id del producto a cambiar el precio unitario.
     * @param nuevoPrecio El nuevo precio unitario del producto.
     */
    void actualizarPrecioUnitario(String id, String nuevoPrecio){
        actualizar("precio_unitario", nuevoPrecio, "id=" + id);
    }

    /**
     * Actualiza el inventario del producto con el id dado.
     * @param id El id del producto a cambiar la cantidad en stock.
     * @param nuevoInventario El nuevo inventario del producto.
     */
    void actualizarInventario(String id, String nuevoInventario){
        actualizar("cantidad", nuevoInventario, "id=" + id);
    }

    /**
     * Reduce el inventario del producto con el id dado.
     * @param id El id del producto.
     * @param numProductosSalientes El número de productos que salen del inventario.
     * @throws ArithmeticException Lanzada si el inventario se torna menor a 0.
     */
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

    /**
     * Aumenta el inventario del producto con el id dado.
     * @param id El id del producto.
     * @param numProductosEntrantes El número de productos que entran al inventario.
     */
    void aumentarInventario(String id, int numProductosEntrantes){
        int inventarioActual = intColumna("cantidad", id);
        String inventarioActualizado = Integer.toString(inventarioActual + numProductosEntrantes);
        actualizarInventario(id, inventarioActualizado);
    }

    public static void main(String[] args) {
        Producto producto = new Producto();
        System.out.println(producto.intColumna("cantidad", "1"));
        // Pruebas:
        System.out.println("----------");
        String nombreAntes = producto.stringColumna("nombre", "1");
        System.out.println("Nombre antes: "+ nombreAntes);
        producto.actualizarNombre("1", "El pepito quince");
        String nombreDespues = producto.stringColumna("nombre", "1");
        System.out.println("Nombre despues: " + nombreDespues);
        System.out.println("Son diferentes?:");
        System.out.println(nombreAntes != nombreDespues);
        System.out.println("----------");
        double precioAntes = producto.doubleColumna("precio_unitario", "1");
        System.out.println("Precio antes: "+ precioAntes);
        producto.actualizarPrecioUnitario("1", "150520");
        double precioDespues = producto.doubleColumna("precio_unitario", "1");
        System.out.println("Precio despues: " + precioDespues);
        System.out.println("Son diferentes?:");
        System.out.println(precioAntes != precioDespues);
        System.out.println("----------");
        int inventarioAntes = producto.intColumna("cantidad", "2");
        System.out.println("Inventario antes: " + inventarioAntes);
        producto.aumentarInventario("2", 1000);
        int inventarioDespues = producto.intColumna("cantidad", "2");
        System.out.println("Inventario despues: " + inventarioDespues);
        System.out.println("Son diferentes?:");
        System.out.println(inventarioAntes != inventarioDespues);
        System.out.println("----------");
        inventarioAntes = producto.intColumna("cantidad", "2");
        System.out.println("Inventario antes: " + inventarioAntes);
        producto.reducirInventario("2", 1000);
        inventarioDespues = producto.intColumna("cantidad", "2");
        System.out.println("Inventario despues: " + inventarioDespues);
        System.out.println("Son diferentes?:");
        System.out.println(inventarioAntes != inventarioDespues);
        DBConnection.cerrar();
    }
}
