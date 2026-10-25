package com.example.model;

public class Producto {
    private int id;
    private String nombre;
    private int precioUnitario;
    private int cantidad;

    public Producto(String nombre, int precioUnitario, int cantidad){
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public Producto(int id, String nombre, int precioUnitario, int cantidad){
        this(nombre, precioUnitario, cantidad);
        this.id = id;
    }

    /**
     * Reduce la cantidad en inventario del producto.
     * @param numProductosSalientes El número de productos que salen del inventario.
     * @throws IllegalArgumentException Lanzada si se calcula que el inventario se torna menor a 0.
     */
    public void reducirCantidad(int numProductosSalientes) throws IllegalArgumentException{
        int inventarioActualizado = this.cantidad - numProductosSalientes;
        if (inventarioActualizado < 0) {
            throw new IllegalArgumentException("No hay suficientes productos en el inventario para realizar esta operación.");
        } else {
            this.cantidad -= numProductosSalientes;  
        }
    }

    @Override
    public String toString() {
        return this.nombre;
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
