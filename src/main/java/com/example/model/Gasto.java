package com.example.model;

import java.time.LocalDate;

public class Gasto {
    private int id;
    private LocalDate fechaGasto;
    public enum Estado {PENDIENTE, PAGADO};
    private Estado estado;
    private int valor;
    private LocalDate fechaPago;
    private String descripcion;
    private LocalDate fechaPresupuesto;

    public Gasto(LocalDate fechaGasto, Estado estado, int valor, LocalDate fechaPago, String descripcion,
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

    public Gasto(int id, LocalDate fechaGasto, Estado estado, int valor, LocalDate fechaPago, String descripcion,
            LocalDate fechaPresupuesto){
        this(fechaGasto, estado, valor, fechaPago, descripcion, fechaPresupuesto);
        this.id = id;
    }
    
    public int getId(){
        return id;
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
