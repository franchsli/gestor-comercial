package com.example.gui;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.time.LocalDate;

import com.example.db.DBException;
import com.example.db.Gasto;
import com.example.db.GastoDAO;

public class GastosPanel extends Panell<Gasto> {

    public GastosPanel() {
        this.dao = new GastoDAO();
        btnNuevo.setText("+ Nuevo gasto");
        this.campoBuscar.setVisible(false);
        btnNuevo.addActionListener(e -> mostrarFormularioNuevo());
        cargarDatos();
    }

    @Override 
    protected String[] columnas(){
        String[] columnas = {"id", "fecha_gasto", "estado", "valor", 
        "fecha_pago", "descripcion", "fecha_presupuesto"};
        return columnas;
    }

    @Override
    protected Object[] fila(Gasto gasto){
        Object[] fila = {gasto.getId(), gasto.getFechaGasto(),
            gasto.getEstado(), gasto.getValor(), gasto.getFechaPago(),
            gasto.getDescripcion(), gasto.getFechaPresupuesto()
        };
        return fila;
    }

    @Override
    public void mostrarFormularioNuevo() {
        JTextField campoFechaPago = new JTextField();
        JTextField campoFechaPresupuesto = new JTextField();
        campoFechaPago.setName("fecha_pago");
        campoFechaPresupuesto.setName("fecha_presupuesto");
        Set<String> excepciones = new HashSet<>();
        excepciones.add("fecha_pago");
        excepciones.add("fecha_presupuesto");
        JSpinner campoFechaGasto = campoFecha; 
        JSpinner.DateEditor editorFechaGasto = new JSpinner.DateEditor(campoFechaGasto, "yyyy-MM-dd"); 
        campoFechaGasto.setEditor(editorFechaGasto); 
        JTextField valor = new JTextField();
        JTextField descripcion = new JTextField();
        Gasto.Estado[] estados = Gasto.Estado.values();
        JComboBox<Gasto.Estado> estado = new JComboBox<>(estados);

        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.add(new JLabel("Fecha gasto (YYYY-MM-DD):")); 
        form.add(campoFechaGasto);
        form.add(new JLabel("Estado:")); 
        form.add(estado);
        form.add(new JLabel("Valor:")); 
        form.add(valor);
        form.add(new JLabel("Fecha pago (YYYY-MM-DD):")); 
        form.add(campoFechaPago);
        form.add(new JLabel("Descripción:")); 
        form.add(descripcion);
        form.add(new JLabel("Fecha presupuesto: (YYYY-MM-DD):"));
        form.add(campoFechaPresupuesto);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Nuevo gasto", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && formularioEsValido(form, excepciones)) {
            try {
                LocalDate fechaGasto = fechaDeCampo(campoFechaGasto);
                Gasto.Estado estadoSeleccionado = (Gasto.Estado) estado.getSelectedItem();
                LocalDate fechaPago = fechaDeCampo(campoFechaPago);
                LocalDate fechaPresupuesto = fechaDeCampo(campoFechaPresupuesto);
                int valorSeleccionado = Integer.parseInt(valor.getText());
                Gasto nuevoGasto = new Gasto(fechaGasto, estadoSeleccionado, valorSeleccionado, fechaPago, 
                                                descripcion.getText(), fechaPresupuesto);
                dao.insertar(nuevoGasto);
                cargarDatos();
            } catch (DBException e) {
                JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
