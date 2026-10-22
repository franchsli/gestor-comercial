package com.example.gui;
import java.util.List;
import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.example.db.DAOLectura;
import com.example.db.DBException;

public abstract class PanelLectura<T> extends JPanel {
    protected DAOLectura<T> daoLectura;
    protected JTable tabla;
    protected DefaultTableModel modeloTabla;

    public PanelLectura() {
        setLayout(new BorderLayout());

        // tabla
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // no editable directamente
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

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
     * @param params Los parámetros necesarios para rellenar la condición.
     */
    public void cargarDatos(String condicion, Object... params) {
        modeloTabla.setRowCount(0);

        try {
            // columnas
            modeloTabla.setColumnIdentifiers(columnas());

            // filas
            List<T> entidades = daoLectura.todos(condicion, params);
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
}
