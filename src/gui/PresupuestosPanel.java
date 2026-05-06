package gui;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import java.awt.GridLayout;

import db.Presupuesto;

public class PresupuestosPanel extends Panel{
    Presupuesto presupuestos = new Presupuesto();

    public PresupuestosPanel(){
        this.modelo = presupuestos;
        btnNuevo.setText("+ Nuevo presupuesto");
        btnNuevo.addActionListener(e -> mostrarFormularioNuevo());
        btnEliminar.addActionListener(e -> manejarEliminar("fecha"));
        cargarDatos();
    }

    @Override
    public void mostrarFormularioNuevo() {
        JSpinner campoFechaPresupuesto = campoFecha; 
        campoFechaPresupuesto.setEditor(editorFecha); 
        JTextField valor = new JTextField();

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("Fecha presupuesto (YYYY-MM-DD):")); form.add(campoFechaPresupuesto);
        form.add(new JLabel("Valor:")); form.add(valor);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Nuevo presupuesto", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && formularioEsValido(form)) {
            String fechaGastoStr = fechaATexto(editorFecha, campoFechaPresupuesto);
            presupuestos.crear(fechaGastoStr, valor.getText());
            cargarDatos();
        }
    }
    
}
