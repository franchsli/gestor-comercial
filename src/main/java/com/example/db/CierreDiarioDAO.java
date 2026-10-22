package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CierreDiarioDAO extends DAOLectura<CierreDiario> {

    public CierreDiarioDAO(){
        super("CIERRES_DIARIOS");
    }

    public CierreDiario mapear(ResultSet resultSet) throws SQLException {
        return new CierreDiario(fechaDeColumna(resultSet, "fecha"), resultSet.getInt("ventas_efectivo"), 
                    resultSet.getInt("ventas_credito"), resultSet.getInt("total_ventas"), resultSet.getInt("presupuesto"),
                    resultSet.getInt("cumplimiento"), resultSet.getInt("gastos"));
    }

    /**
     * Devuelve los cierres diarios en el mes del año dado.
     * @param año El año en el que deben estar los cierres diarios.
     * @param mes El mes en el que deben estar los cierres diarios.
     * @return Los cierres diarios en el mes del año dado.
     */
    public List<CierreDiario> filtrarPorMes(int año, int mes) {
        // primer día del mes
        LocalDate inicio = LocalDate.of(año, mes, 1);
        LocalDate fin = inicio.plusMonths(1);
        return todos("fecha >= ? AND fecha < ?", inicio, fin);
    }

    /**
     * Devuelve los cierres diarios en el año dado.
     * @param año El año en el que deben estar los cierres diarios.
     * @return Los cierres diarios en el año dado.
     */
    public List<CierreDiario> filtrarPorAño(int año) {
        // primer día del año
        LocalDate inicio = LocalDate.of(año, 1, 1);
        LocalDate fin = inicio.plusYears(1);
        return todos("fecha >= ? AND fecha < ?", inicio, fin);
    }
    
}
