package com.example.model;

import java.time.LocalDate;

public class CierreDiario {
    private LocalDate fecha;
    // totales diarios como int ya que no se han visto
    // valores que se acerquen al límite de int individualmente
    private int ventasEfectivo;
    private int ventasCredito;
    private int totalVentas;
    private int presupuesto;
    private int cumplimiento;
    private int gastos;

    public CierreDiario(LocalDate fecha, int ventasEfectivo, int ventasCredito, int totalVentas, int presupuesto,
            int cumplimiento, int gastos){
        this.fecha = fecha;
        this.ventasEfectivo = ventasEfectivo;
        this.ventasCredito = ventasCredito;
        this.totalVentas = totalVentas;
        this.presupuesto = presupuesto;
        this.cumplimiento = cumplimiento;
        this.gastos = gastos;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getVentasEfectivo() {
        return ventasEfectivo;
    }

    public int getVentasCredito() {
        return ventasCredito;
    }

    public int getTotalVentas() {
        return totalVentas;
    }

    public int getPresupuesto() {
        return presupuesto;
    }

    public int getCumplimiento() {
        return cumplimiento;
    }

    public int getGastos() {
        return gastos;
    }
}
