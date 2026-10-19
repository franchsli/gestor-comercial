package com.example.db;

import java.time.LocalDate;

public class Ventaa {
    private int id;
    private LocalDate fecha;
    private int valor;
    public enum Tipo {EFECTIVO, CREDITO};
    private Tipo tipo;

    public Ventaa(LocalDate fecha, int valor, Tipo tipo){
        this.fecha = fecha;
        this.valor = valor;
        this.tipo = tipo;    
    }

    public Ventaa(int id, LocalDate fecha, int valor, Tipo tipo){
        this(fecha, valor, tipo);
        this.id = id;
    }

    /**
     * Agrega el valor del precio unitario * cantidad
     * del producto en venta al valor total de la venta.
     * @param precioUnitario El precio unitario del producto
     * agregado a la venta.
     * @param cantidad La cantidad de ese producto en la venta.
     */
    public void agregarAlValor(int precioUnitario, int cantidad){
        this.valor += precioUnitario * cantidad;
    }
    
    public int getId() {
        return id;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public int getValor() {
        return valor;
    }
    public Tipo getTipo() {
        return tipo;
    }
    

}
