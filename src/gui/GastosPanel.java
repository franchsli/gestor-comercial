package gui;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import java.awt.GridLayout;

import db.Gasto;

public class GastosPanel extends Panel {
    Gasto gastos = new Gasto();

    public GastosPanel() {
        this.modelo = gastos;
        btnNuevo.setText("+ Nuevo gasto");
        btnNuevo.addActionListener(e -> mostrarFormularioNuevo());
        cargarDatos();
    }

    @Override
    public void mostrarFormularioNuevo() {
        JTextField campoFechaPago = new JTextField();
        JSpinner campoFechaGasto = campoFecha; 
        JSpinner.DateEditor editorFechaGasto = new JSpinner.DateEditor(campoFechaGasto, "yyyy-MM-dd"); 
        campoFechaGasto.setEditor(editorFechaGasto); 
        JTextField valor = new JTextField();
        JTextField descripcion = new JTextField();
        String[] estados = {"PENDIENTE", "PAGADO"};
        JComboBox<String> estado = new JComboBox<>(estados);

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("Fecha gasto (YYYY-MM-DD):")); form.add(campoFechaGasto);
        form.add(new JLabel("Estado:")); form.add(estado);
        form.add(new JLabel("Valor:")); form.add(valor);
        form.add(new JLabel("Fecha pago (YYYY-MM-DD):")); form.add(campoFechaPago);
        form.add(new JLabel("Descripción:")); form.add(descripcion);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Nuevo gasto", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && formularioEsValido(form)) {
            String fechaGastoStr = fechaATexto(editorFecha, campoFechaGasto);
            String fechaPagoStr = campoFechaPago.getText();
            gastos.crear(fechaGastoStr, estado.getSelectedItem().toString(), valor.getText(), fechaPagoStr, descripcion.getText());
            cargarDatos();
        }
    }
}
