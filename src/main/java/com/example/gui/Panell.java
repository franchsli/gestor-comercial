package com.example.gui;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

import com.example.db.DAO;
import com.example.db.DBException;
import com.example.db.Modelo;

// TODO: Reemplazar Panel con esta clase cuando la refactorización acabe.
public abstract class Panell<T> extends JPanel {
   Modelo modelo = new Modelo();
   DAO<T> dao;
   T entidad;
   JTable tabla;
   DefaultTableModel modeloTabla;
   JButton btnNuevo, btnEditar, btnEliminar, btnFiltrar;
   JTextField campoBuscar;
   JSpinner campoFecha;
   JSpinner.DateEditor editorFecha;

    public Panell() {
        setLayout(new BorderLayout());
        // barra de botones
        JPanel barraBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnNuevo = new JButton("+ Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
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

    /**
     * Devuelve el nombre de las columnas
     * del DAO de la entidad referenciada en Panel.
     * @return El nombre de las columnas.
     */
    protected abstract String[] columnas();

    /**
     * Devuelve un Array de los valores
     * en fila de la entidad dada, en el mismo
     * orden en el que se visualizan en las
     * columnas.
     * @param entidad La entidad que contiene
     * los valores.
     * @return Un arreglo de los valores en fila. 
     */
    protected abstract Object[] fila(T entidad);

    /**
     * Carga los datos del modelo de la clase que cumplan la condicion dada en la pestaña.
     * @param condicion La condición que deben cumplir los datos del modelo de la clase.
     */
    public void cargarDatos(String condicion) {
        modeloTabla.setRowCount(0);

        try {
            // columnas
            modeloTabla.setColumnIdentifiers(columnas());

            // filas
            List<T> entidades = dao.todos(condicion);
            for (T entidad : entidades) {
                modeloTabla.addRow(fila(entidad));
            }
        } catch (DBException e) {
            JOptionPane.showMessageDialog(this,
                "Error en la base de datos: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga todos los datos del modelo de la clase en la pestaña.
     */
    public void cargarDatos() {
        cargarDatos("");
    }

    /**
     * Maneja el borrado de datos en la pestaña.
     */
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
            try {
                for (int i = filasSeleccionadas.length - 1; i >= 0; i--) {
                    String pk = modeloTabla.getValueAt(filasSeleccionadas[i], 0).toString();
                    dao.borrarTodos(columnas()[0] + " = ?", pk);
                }
                cargarDatos();
            } catch (DBException e) {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Maneja la edición de datos en la pestaña.
     * Las clases hijas que lo necesiten lo
     * implementan.
     */
    public void manejarEditar() {
    }

    /**
     * Muestra el formulario para la inserción de datos.
     * (subclases sobreescriben este método para mostrar su formulario)
     */
    public void mostrarFormularioNuevo() {}

    /**
     * Devuelve si un formulario es válido o no.
     * Un formulario es válido si todos sus campos fueron rellenados.
     * @param form El formulario a validar.
     * @return Si el formulario es válido o no.
     */
    protected boolean formularioEsValido(JPanel form) {
        return formularioEsValido(form, null);
    }

    /**
     * Devuelve si un formulario es válido o no.
     * Un formulario es válido si todos sus campos 
     * (excluyendo la excepcion dada) fueron rellenados.
     * @param form El formulario a validar.
     * @param excepciones Los nombres de los campos a ser 
     * excluidos de la validación.
     * @return Si el formulario es válido o no.
     */
    protected boolean formularioEsValido(JPanel form, Set<String> excepciones) {
        for (Component c : form.getComponents()) {
            String valor = null;
            String nombre = c.getName();

            if (c instanceof JTextField) {
                valor = ((JTextField) c).getText().trim();
            } else if (c instanceof JSpinner) {
                valor = ((JSpinner) c).getValue().toString().trim();
            }

            if (valor != null && valor.isEmpty()) {
                if (excepciones != null && excepciones.contains(nombre)) continue;
                JOptionPane.showMessageDialog(this,
                    "Por favor completa todos los campos",
                    "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    /**
     * TODO: Reemplazar esto por fechaDeCampo
     * para devolver LocalDate de un campoFech (JSpinner)
     * Devuelve la fecha en el campo dado como String.
     * @param editorFecha El editor del campo que tiene la fecha.
     * @param campoFecha El campo que tiene la fecha.
     * @return la fecha en el campo dado como String.
     */
    protected String fechaATexto(JSpinner.DateEditor editorFecha, JSpinner campoFecha){
        return editorFecha.getFormat().format(campoFecha.getValue());
    }
}
