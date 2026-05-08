package db;

import java.util.List;
import java.util.Map;

public class CierreDiario extends Modelo {
    public CierreDiario(){
        this.nombreTabla = "CIERRES_DIARIOS";
        this.columnas = "(fecha, ventas_efectivo, ventas_credito, total_ventas, presupuesto, cumplimiento, gastos)";
    }

    public List<Map<String, String>> filtrarPorMes(int año, int mes) {
        // formatea la fecha del mes a 4 digitos en el año y 2 en el mes
        String mesStr = String.format("%04d-%02d", año, mes);
        return todos("fecha LIKE '" + mesStr + "%'");
    }

    public List<Map<String, String>> filtrarPorAño(int año) {
        return todos("fecha LIKE '" + año + "%'");
    }
}