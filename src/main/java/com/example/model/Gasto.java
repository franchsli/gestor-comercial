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
    private Integer idPresupuesto;

    public Gasto(LocalDate fechaGasto, Estado estado, int valor, LocalDate fechaPago, String descripcion,
            Integer idPresupuesto) {
        this.fechaGasto = fechaGasto;
        if (fechaPago == null) {
            this.estado = Estado.PENDIENTE;
        }
        else this.estado = estado;
        this.valor = valor;
        this.fechaPago = fechaPago;
        this.descripcion = descripcion;
        this.idPresupuesto = idPresupuesto;
    }

    public Gasto(int id, LocalDate fechaGasto, Estado estado, int valor, LocalDate fechaPago, String descripcion,
            Integer idPresupuesto){
        this(fechaGasto, estado, valor, fechaPago, descripcion, idPresupuesto);
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
    public Integer getIdPresupuesto() {
        return idPresupuesto;
    }

}
