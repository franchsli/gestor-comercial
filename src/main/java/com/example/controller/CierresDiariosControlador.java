package com.example.controller;

import java.util.List;

import com.example.db.CierreDiarioDAO;
import com.example.model.CierreDiario;

public class CierresDiariosControlador extends ControladorLectura<CierreDiario> {

    private final CierreDiarioDAO cierreDiarioDAO;

    public CierresDiariosControlador() {
        this(new CierreDiarioDAO());
    }

    private CierresDiariosControlador(CierreDiarioDAO cierreDiarioDAO){
        super(cierreDiarioDAO);
        this.cierreDiarioDAO = cierreDiarioDAO;
        
    }

    /**
     * Devuelve los cierres diarios en el mes del año dado.
     * @param año El año en el que deben estar los cierres diarios.
     * @param mes El mes en el que deben estar los cierres diarios.
     * @return Los cierres diarios en el mes del año dado.
     */
    public List<CierreDiario> filtrarPorMes(int año, int mes) {
        return this.cierreDiarioDAO.filtrarPorMes(año, mes);
    }

    /**
     * Devuelve los cierres diarios en el año dado.
     * @param año El año en el que deben estar los cierres diarios.
     * @return Los cierres diarios en el año dado.
     */
    public List<CierreDiario> filtrarPorAño(int año) {
        return this.cierreDiarioDAO.filtrarPorAño(año);
    }

    /**
     * Retorna la suma de los registros en la columna que cumplen la condicion dada.
     * @param columna El nombre de la columna a sumar.
     * @param condicion La condicion que deben cumplir los registros.
     * @param params Los parametros que rellenarán la condicion.
     * @return El valor total de sumar todos los registros que cumplen la condicion en la columna.
     */
    public long sumarColumna(String columna, String condicion, Object... params) {
        return this.cierreDiarioDAO.sumarColumna(columna, condicion, params);

    }
}
