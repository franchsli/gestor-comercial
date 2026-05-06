package gui;

import db.Presupuesto;

public class PresupuestosPanel extends Panel{
    Presupuesto presupuestos = new Presupuesto();

    public PresupuestosPanel(){
        this.modelo = presupuestos;
        btnNuevo.setText("+ Nuevo presupuesto");
        btnNuevo.addActionListener(e -> mostrarFormularioNuevo());
        cargarDatos();
    }
    
}
