package com.example.db;

import java.time.LocalDate;

public class Presupuestoo {
    private LocalDate fecha;
    private int valor;

    public Presupuestoo(LocalDate fecha, int valor){
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
