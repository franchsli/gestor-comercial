package com.example.gui;

import javax.swing.JComboBox;
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
        String[] columnas = {"id", "fecha", "tipo", "valor"};
        return columnas;
    }

    @Override
    protected Object[] fila(Presupuesto presupuesto){
        Object[] fila = {presupuesto.getId(), presupuesto.getFecha(), presupuesto.getTipo(), presupuesto.getValor()};
        return fila;
    }

    @Override
    public void mostrarFormularioNuevo() {
        JSpinner campoFechaPresupuesto = campoFecha; 
        campoFechaPresupuesto.setEditor(editorFecha); 
        JComboBox<Presupuesto.Tipo> campoTipo = new JComboBox<>(Presupuesto.Tipo.values());
        JTextField valor = new JTextField();

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Fecha presupuesto (YYYY-MM-DD):")); form.add(campoFechaPresupuesto);
        form.add(new JLabel("Tipo:")); form.add(campoTipo);
        form.add(new JLabel("Valor:")); form.add(valor);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Nuevo presupuesto", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && formularioEsValido(form)) {
            try {
                LocalDate fechaPresupuesto = fechaDeCampo(campoFechaPresupuesto);
                Presupuesto.Tipo tipoPresupuesto = (Presupuesto.Tipo) campoTipo.getSelectedItem();
                int valorPresupuesto = Integer.parseInt(valor.getText().trim()); 
                Presupuesto presupuesto = new Presupuesto(fechaPresupuesto, tipoPresupuesto, valorPresupuesto);
                controladorCrud.insertar(presupuesto);
                cargarDatos();
            } catch (DBException e) {
                mostrarError("Error al guardar: " + e.getMessage());
            } catch (NumberFormatException e) {
                mostrarError("Valor inválido");
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
        int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        LocalDate fechaActual = (LocalDate) modeloTabla.getValueAt(fila, 1);
        Presupuesto.Tipo tipoActual = (Presupuesto.Tipo) modeloTabla.getValueAt(fila, 2);
        JTextField campoValor = new JTextField(modeloTabla.getValueAt(fila, 3).toString());
        campoFecha.setEditor(editorFecha);
        ponerFecha(campoFecha, fechaActual);
        JComboBox<Presupuesto.Tipo> campoTipo = new JComboBox<>(Presupuesto.Tipo.values());
        campoTipo.setSelectedItem(tipoActual);

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Fecha:"));
        form.add(campoFecha);
        form.add(new JLabel("Tipo:"));
        form.add(campoTipo);
        form.add(new JLabel("Valor:"));
        form.add(campoValor);

        int result = JOptionPane.showConfirmDialog(this, form, "Editar presupuesto", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate fechaNueva = fechaDeCampo(campoFecha);
                Presupuesto.Tipo tipoNuevo = (Presupuesto.Tipo) campoTipo.getSelectedItem();
                int valorNuevo = Integer.parseInt(campoValor.getText().trim());
                Presupuesto presupuesto = new Presupuesto(id, fechaNueva, tipoNuevo, valorNuevo);
                presupuestosControlador.actualizar(presupuesto);
                cargarDatos();
            } catch (DBException e) {
                mostrarError("Error al actualizar: " + e.getMessage());
            } catch (NumberFormatException e) {
                mostrarError("Valor inválido");
            }
        }

    }
    
}
