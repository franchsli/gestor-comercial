package gui;
import java.util.List;
import java.util.Map;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

import db.Modelo;

public class Panel extends JPanel {
   Modelo modelo = new Modelo();
   JTable tabla;
   DefaultTableModel modeloTabla;
   JButton btnNuevo, btnEditar, btnEliminar, btnFiltrar;
   JTextField campoBuscar;
   JSpinner campoFecha;
   JSpinner.DateEditor editorFecha;

    public Panel() {
        setLayout(new BorderLayout());
        // barra de botones
        JPanel barraBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnNuevo = new JButton("+ Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnFiltrar = new JButton("Filtrar");
        campoBuscar = new JTextField(20);
        campoFecha = new JSpinner(new SpinnerDateModel()); 
        editorFecha = new JSpinner.DateEditor(campoFecha, "yyyy-MM-dd"); 
        campoFecha.setEditor(editorFecha); 
        // fecha de hoy por defecto 
        campoFecha.setValue(new java.util.Date()); 
        campoBuscar.putClientProperty("JTextField.placeholderText", "Buscar...");

        barraBotones.add(btnNuevo);
        barraBotones.add(btnEditar);
        barraBotones.add(btnEliminar);
        barraBotones.add(btnFiltrar);
        barraBotones.add(campoBuscar);

        // tabla
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // no editable directamente
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        add(barraBotones, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // listeners compartidos
        btnEliminar.addActionListener(e -> manejarEliminar());
        btnEditar.addActionListener(e -> manejarEditar());
    }

    public void cargarDatos() {
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(0);

        List<Map<String, String>> filas = modelo.todos();
        if (filas.isEmpty()) return;

        // columnas desde la primera fila
        filas.get(0).keySet().forEach(modeloTabla::addColumn);

        // filas
        for (Map<String, String> fila : filas) {
            modeloTabla.addRow(fila.values().toArray());
        }
    }

    public void manejarEliminar() {
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
                String id = modeloTabla.getValueAt(filasSeleccionadas[i], 0).toString();
                modelo.borrarTodos("id=" + id);
            }
            cargarDatos();
        }
    }

    public void manejarEditar() {
        // pendiente de implementar
    }

    // subclases sobreescriben este método para mostrar su formulario
    public void mostrarFormularioNuevo() {}

    protected boolean formularioEsValido(JPanel form) {
        return formularioEsValido(form, null);
    }

    protected boolean formularioEsValido(JPanel form, String excepcion) {
        for (Component c : form.getComponents()) {
            String valor = null;
            String nombre = c.getName();

            if (c instanceof JTextField) {
                valor = ((JTextField) c).getText().trim();
            } else if (c instanceof JSpinner) {
                valor = ((JSpinner) c).getValue().toString().trim();
            }

            if (valor != null && valor.isEmpty()) {
                if (excepcion != null && excepcion.equals(nombre)) continue;
                JOptionPane.showMessageDialog(this,
                    "Por favor completa todos los campos",
                    "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }
}
