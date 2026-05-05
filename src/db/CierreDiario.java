package db;

public class CierreDiario extends Modelo {
    public CierreDiario(){
        this.nombreTabla = "CIERRES_DIARIOS";
        this.columnas = "(fecha, ventas_efectivo, ventas_credito, total_ventas, presupuesto, cumplimiento, gastos)";
    }
}