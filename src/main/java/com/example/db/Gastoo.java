package com.example.db;

import java.time.LocalDate;

public class Gastoo {
    private int id;
    private LocalDate fechaGasto;
    public enum Estado {PAGADO, PENDIENTE};
    private Estado estado;
    private int valor;
    private LocalDate fechaPago;
    private String descripcion;
    private LocalDate fechaPresupuesto;

    public Gastoo(LocalDate fechaGasto, Estado estado, int valor, LocalDate fechaPago, String descripcion,
            LocalDate fechaPresupuesto) {
        this.fechaGasto = fechaGasto;
        if (fechaPago == null) {
            this.estado = Estado.PENDIENTE;
        }
        else this.estado = estado;
        this.valor = valor;
        this.fechaPago = fechaPago;
        this.descripcion = descripcion;
        this.fechaPresupuesto = fechaPresupuesto;
    }

    public Gastoo(int id, LocalDate fechaGasto, Estado estado, int valor, LocalDate fechaPago, String descripcion,
            LocalDate fechaPresupuesto){
        this(fechaGasto, estado, valor, fechaPago, descripcion, fechaPresupuesto);
        this.id = id;
            }

    public LocalDate getFechaGasto() {
        return fechaGasto;
    }
    public Estado getEstado() {
        return estado;
    }
    public int getValor() {
        return valor;
    }
    public LocalDate getFechaPago() {
        return fechaPago;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public LocalDate getFechaPresupuesto() {
        return fechaPresupuesto;
    }

}
