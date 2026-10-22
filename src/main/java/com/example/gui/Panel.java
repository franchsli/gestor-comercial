package com.example.gui;
import java.util.Date;
import java.util.Set;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import com.example.db.DAOCrud;
import com.example.db.DBException;

public abstract class Panel<T> extends PanelLectura<T> {
    DAOCrud<T> dao;
    JButton btnNuevo, btnEditar, btnEliminar, btnFiltrar;
    JTextField campoBuscar;
    JSpinner campoFecha;
    JSpinner.DateEditor editorFecha;

    public Panel() {
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

        add(barraBotones, BorderLayout.NORTH);

        // listeners compartidos
        btnEliminar.addActionListener(e -> manejarEliminar());
        btnEditar.addActionListener(e -> manejarEditar());
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
     * implementan a su modo si es necesario.
     */
    public void manejarEditar(){
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecciona una fila primero",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // construir formulario con los valores actuales
        int cols = modeloTabla.getColumnCount();
        JTextField[] campos = new JTextField[cols];
        JPanel form = new JPanel(new GridLayout(cols, 2, 5, 5));

        for (int i = 0; i < cols; i++) {
            String nombreColumna = modeloTabla.getColumnName(i);
            Object valorActual = modeloTabla.getValueAt(fila, i);
            campos[i] = new JTextField(valorActual != null ? valorActual.toString() : "");
            campos[i].setName(nombreColumna);
            form.add(new JLabel(nombreColumna + ":"));
            form.add(campos[i]);
        }

        // deshabilitar columna 0 (id o pk)
        campos[0].setEditable(false);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Editar registro", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String[] datos = new String[cols];
                for (int i = 0; i < cols; i++) {
                    datos[i] = campos[i].getText().trim();
                }
                aplicarCambios(datos);
                cargarDatos();
            } catch (DBException e) {
                JOptionPane.showMessageDialog(this,
                    "Error al actualizar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Muestra el formulario para la inserción de datos.
     * (subclases sobreescriben este método para mostrar su formulario)
     */
    public void mostrarFormularioNuevo() {}

    /**
     * Aplica los cambios en el formulario de edición al 
     * registro a editar con los valores dados.
     * Este método debe implementarse y usarse en los paneles
     * que no sobreescriban manejarEditar()
     * @param valores Los valores en la actualización.
     */
    public void aplicarCambios(String[] valores){
        throw new UnsupportedOperationException("Este panel no usa la edición genérica; sobrescribe manejarEditar()");
    }

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
     * Devuelve la fecha en el campo dado como LocalDate.
     * @param campoFecha El campo que tiene la fecha.
     * @return la fecha en el campo dado como LocalDate.
     */
    protected LocalDate fechaDeCampo(JSpinner campoFecha){
        Date fechaConTiempo = (Date) campoFecha.getValue();
        ZoneId zonaActual = ZoneId.systemDefault();
        LocalDate fecha = fechaConTiempo.toInstant()
            .atZone(zonaActual).toLocalDate();
        return fecha;
    }

    /**
     * Devuelve la fecha en el campo de texto como
     * LocalDate.
     * @param campoFecha El campo de texto que contiene la fecha.
     * @return la fecha en el campo dado como LocalDate.
     */
    protected LocalDate fechaDeCampo(JTextField campoFecha){
        String fecha = campoFecha.getText();
        if (fecha == null || fecha.isBlank()) return null;
        return LocalDate.parse(fecha.trim());
    }

    /**
     * Pone la fecha dada dentro del campo dado.
     * @param campoFecha El campo que deberá contener
     * la fecha.
     * @param fecha La fecha a poner en el campo.
     */
    protected void ponerFecha(JSpinner campoFecha, LocalDate fecha) {
        campoFecha.setValue(Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
}
