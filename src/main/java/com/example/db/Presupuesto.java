package com.example.db;

import java.time.LocalDate;

public class Presupuesto {
    private LocalDate fecha;
    private int valor;

    public Presupuesto(LocalDate fecha, int valor){
        this.fecha = fecha;
        this.valor = valor;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    public int getValor() {
        return valor;
    }
}
