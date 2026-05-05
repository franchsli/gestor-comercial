package gui;

import db.CierreDiario;

public class CierreDiarioPanel extends Panel {
    CierreDiario cierresDiarios = new CierreDiario();

    public CierreDiarioPanel() {
        this.modelo = cierresDiarios;
        cargarDatos();
    }
}
