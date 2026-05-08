package gui;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;

import db.Presupuesto;

public class PresupuestosPanel extends Panel{
    Presupuesto presupuestos = new Presupuesto();

    public PresupuestosPanel(){
        this.modelo = presupuestos;
        btnNuevo.setText("+ Nuevo presupuesto");
        btnNuevo.addActionListener(e -> mostrarFormularioNuevo());
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

    @Override
    public void manejarEliminar(){
        int[] filasSeleccionadas = tabla.getSelectedRows();
        if (filasSeleccionadas.length == 0) {
            JOptionPane.showMessageDialog(this,
                "Selecciona filas primero",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "Confirma el borrado de los registros",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            for (int i = filasSeleccionadas.length - 1; i >= 0; i--) {
                String fecha = modeloTabla.getValueAt(filasSeleccionadas[i], 0).toString();
                modelo.borrarTodos("fecha='" + fecha + "'");
            }
            cargarDatos();
        }
    }

    @Override
    public void manejarEditar() {
        try {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una fila primero", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String fechaActual = modeloTabla.getValueAt(fila, 0).toString();
            JTextField campoValor = new JTextField(modeloTabla.getValueAt(fila, 1).toString());
            JSpinner campoFechaPresupuesto = campoFecha; 
            campoFechaPresupuesto.setEditor(editorFecha);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            campoFechaPresupuesto.setValue(simpleDateFormat.parse(fechaActual)); 

            JPanel form = new JPanel(new GridLayout(1, 2, 5, 5));
            form.add(new JLabel("Fecha:"));
            form.add(campoFechaPresupuesto);
            form.add(new JLabel("Valor:"));
            form.add(campoValor);

            int result = JOptionPane.showConfirmDialog(this, form, "Editar presupuesto", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                LinkedHashMap<String, String> datos = new LinkedHashMap<>();
                datos.put("fecha", fechaATexto(editorFecha, campoFechaPresupuesto));
                datos.put("valor", campoValor.getText()); 
                presupuestos.actualizar(datos, "fecha='" + fechaActual + "'");
                cargarDatos();
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Fecha actual inválida", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }
    
}
