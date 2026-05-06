package db;

import java.time.LocalDate;
import java.time.YearMonth;

public class Presupuesto extends Modelo {
    public Presupuesto() {
        this.nombreTabla = "PRESUPUESTOS";
        this.columnas = "(fecha, valor)";
    }

    /**
     * Crea un presupuesto en la base de datos.
     * @param fecha La fecha para la que se destina el presupuesto.
     * @param valor El mnto del presupuesto.
     */
    public void crear(String fecha, String valor) {
        String datos[] = {fecha, valor};
        insertar(datos);
    }

    /**
     * Fija el presupuesto mensual del mes dado en el año dado.
     * @param año El año en el que se fija el presupuesto.
     * @param mes El mes al que se le va a fijar el presupuesto.
     * @param presupuestoMensual El monto del presupuesto mensual.
     */
    public void fijarPresupuestoMensual(String año, String mes, String presupuestoMensual) {
        DiaNoLaborable diasNoLaborables = new DiaNoLaborable();
        int añoEntero = Integer.parseInt(año);
        int mesEntero = Integer.parseInt(mes);
        int presupuestoMensualInt = Integer.parseInt(presupuestoMensual);
        YearMonth yearMonth = YearMonth.of(añoEntero, mesEntero);
        LocalDate inicio = yearMonth.atDay(1);
        LocalDate fin = yearMonth.atEndOfMonth();

        // obtener fechas no laborables del mes
        String condicion = "fecha BETWEEN '" + inicio + "' AND '" + fin + "'";
        int totalNoLaborables = diasNoLaborables.contarTodos(condicion);

        int diasHabiles = yearMonth.lengthOfMonth() - totalNoLaborables;
        if (diasHabiles == 0) return;

        int valorDiario = presupuestoMensualInt / diasHabiles;
        

        // insertar un presupuesto por cada día hábil
        for (LocalDate fecha = inicio; !fecha.isAfter(fin); fecha = fecha.plusDays(1)) {
            String fechaStr = fecha.toString();
            int esNoLaboral = diasNoLaborables.contarTodos("fecha='" + fechaStr + "'");
            if (esNoLaboral > 0) continue;
            crear(fechaStr, String.valueOf(valorDiario));
        }
    }
}