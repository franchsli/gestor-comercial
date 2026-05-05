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

    public void cargarTotales() {
        String[] columnas = {"ventas_efectivo", "ventas_credito", "total_ventas", "presupuesto", "gastos"};
        Object[] filaTotal = new Object[modeloTabla.getColumnCount()];
        
        filaTotal[0] = "TOTAL";
        for (int i = 1; i < columnas.length + 1; i++) {
            filaTotal[i] = cierresDiarios.sumarColumna(columnas[i - 1]);
        }
        // cumplimiento va en índice 5, se calcula aparte
        int totalVentas = cierresDiarios.sumarColumna("total_ventas");
        int totalPresupuesto = cierresDiarios.sumarColumna("presupuesto");
        filaTotal[5] = totalPresupuesto == 0 ? "0%" : Math.round((totalVentas * 100.0) / totalPresupuesto) + "%";

        modeloTabla.addRow(filaTotal);
    }
}
