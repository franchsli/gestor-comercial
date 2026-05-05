package gui;

import db.CierreDiario;

public class CierreDiarioPanel extends Panel {
    CierreDiario cierresDiarios = new CierreDiario();

    public CierreDiarioPanel() {
        this.modelo = cierresDiarios;
        // borra los botones ya que esta pestaña es solo lectura
        this.btnEditar.setVisible(false);
        this.btnEliminar.setVisible(false);
        this.btnFiltrar.setVisible(false);
        this.btnNuevo.setVisible(false);
        this.campoBuscar.setVisible(false);
        cargarDatos();
    }
}
