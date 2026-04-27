package gui;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
        JTextField fechaGasto = new JTextField();
        JTextField valor = new JTextField();
        JTextField descripcion = new JTextField();
        JTextField fechaPago = new JTextField();
        String[] estados = {"PENDIENTE", "PAGADO"};
        JComboBox<String> estado = new JComboBox<>(estados);

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("Fecha gasto (YYYY-MM-DD):")); form.add(fechaGasto);
        form.add(new JLabel("Estado:")); form.add(estado);
        form.add(new JLabel("Valor:")); form.add(valor);
        form.add(new JLabel("Fecha pago (YYYY-MM-DD):")); form.add(fechaPago);
        form.add(new JLabel("Descripción:")); form.add(descripcion);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Nuevo gasto", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            gastos.crear(fechaGasto.getText(), estado.getSelectedItem().toString(), valor.getText(), fechaPago.getText(), descripcion.getText());
            cargarDatos();
        }
    }
}
