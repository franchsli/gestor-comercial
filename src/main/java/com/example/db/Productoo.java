package com.example.db;

// TODO: Reemplaza Producto con esta clase cuando la migracion termine
public class Productoo {
    private int id;
    private String nombre;
    private int precioUnitario;
    private int cantidad;

    public Productoo(String nombre, int precioUnitario, int cantidad){
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public Productoo(int id, String nombre, int precioUnitario, int cantidad){
        this(nombre, precioUnitario, cantidad);
        this.id = id;
    }

    /**
     * Reduce la cantidad en inventario del producto.
     * @param numProductosSalientes El número de productos que salen del inventario.
     * @throws ArithmeticException Lanzada si se calcula que el inventario se torna menor a 0.
     */
    public void reducirCantidad(int numProductosSalientes) throws ArithmeticException{
        int inventarioActualizado = this.cantidad - numProductosSalientes;
        if (inventarioActualizado < 0) {
            throw new ArithmeticException("No hay suficientes productos en el inventario para realizar esta operación.");
        } else {
            this.cantidad -= numProductosSalientes;  
        }
    }

    public int getId(){
        return this.id;
    }

    public String getNombre(){
        return this.nombre;
    }

    public int getPrecioUnitario(){
        return this.precioUnitario;
    }

    public int getCantidad(){
        return this.cantidad;
    }

}
