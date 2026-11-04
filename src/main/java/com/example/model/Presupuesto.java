package com.example.model;

import java.time.LocalDate;

public class Presupuesto {
    private int id;
    private LocalDate fecha;
    public enum Tipo {INGRESO, EGRESO};
    private Tipo tipo;
    private int valor;

    public Presupuesto(LocalDate fecha, Tipo tipo, int valor) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.valor = valor;
    }

    public Presupuesto(int id, LocalDate fecha, Tipo tipo, int valor) {
        this(fecha, tipo, valor);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getValor() {
        return valor;
    }
}
