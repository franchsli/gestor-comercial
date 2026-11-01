package com.example.gui;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.time.LocalDate;

import com.example.controller.PresupuestosControlador;
import com.example.db.DBException;
import com.example.model.Presupuesto;

public class PresupuestosPanel extends PanelCrud<Presupuesto>{

    private final PresupuestosControlador presupuestosControlador;

    public PresupuestosPanel(){
        this(new PresupuestosControlador());
    }

    private PresupuestosPanel(PresupuestosControlador presupuestosControlador){
        super(presupuestosControlador);
        this.presupuestosControlador = presupuestosControlador;
        btnNuevo.setText("+ Nuevo presupuesto");
        this.campoBuscar.setVisible(false);
        btnNuevo.addActionListener(e -> mostrarFormularioNuevo());
        cargarDatos();
    }

    @Override 
    protected String[] columnas(){
        String[] columnas = {"fecha", "valor"};
        return columnas;
    }

    @Override
    protected Object[] fila(Presupuesto presupuesto){
        Object[] fila = {presupuesto.getFecha(), presupuesto.getValor()};
        return fila;
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
            try {
                LocalDate fechaPresupuesto = fechaDeCampo(campoFechaPresupuesto);
                int valorPresupuesto = Integer.parseInt(valor.getText()); 
                Presupuesto presupuesto = new Presupuesto(fechaPresupuesto, valorPresupuesto);
                controladorCrud.insertar(presupuesto);
                cargarDatos();
            } catch (DBException e) {
                mostrarError("Error al guardar: " + e.getMessage());
            }
        }
    }

    @Override
    public void manejarEditar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            mostrarAviso("Selecciona una fila primero");
            return;
        }
        LocalDate fechaActual = (LocalDate) modeloTabla.getValueAt(fila, 0);
        JTextField campoValor = new JTextField(modeloTabla.getValueAt(fila, 1).toString());
        campoFecha.setEditor(editorFecha);
        ponerFecha(campoFecha, fechaActual);

        JPanel form = new JPanel(new GridLayout(1, 2, 5, 5));
        form.add(new JLabel("Fecha:"));
        form.add(campoFecha);
        form.add(new JLabel("Valor:"));
        form.add(campoValor);

        int result = JOptionPane.showConfirmDialog(this, form, "Editar presupuesto", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate fechaNueva = fechaDeCampo(campoFecha);
                int valorNuevo = Integer.parseInt(campoValor.getText());
                Presupuesto presupuesto = new Presupuesto(fechaNueva, valorNuevo);
                presupuestosControlador.actualizar(presupuesto, fechaActual);
                cargarDatos();
            } catch (DBException e) {
                mostrarError("Error al actualizar: " + e.getMessage());
            }
        }

    }
    
}
