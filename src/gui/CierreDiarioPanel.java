package gui;

import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import db.CierreDiario;

public class CierreDiarioPanel extends Panel {
    CierreDiario cierresDiarios = new CierreDiario();

    public CierreDiarioPanel() {
        this.modelo = cierresDiarios;
        // borra los botones ya que esta pestaña es solo lectura
        this.btnEditar.setVisible(false);
        this.btnEliminar.setVisible(false);
        this.btnNuevo.setVisible(false);
        this.campoBuscar.setVisible(false);
        // controles de filtro
        String[] meses = {"Todos", "Enero", "Febrero", "Marzo", "Abril", "Mayo",
            "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        JComboBox<String> comboMes = new JComboBox<>(meses);

        int añoActual = java.time.Year.now().getValue();
        Integer[] años = {añoActual - 2, añoActual - 1, añoActual};
        JComboBox<Integer> comboAño = new JComboBox<>(años);
        comboAño.setSelectedItem(añoActual);

        JButton btnAplicar = new JButton("Filtrar");
        btnAplicar.addActionListener(e -> {
            int mes = comboMes.getSelectedIndex(); // 0 = Todos
            // toma el item seleccionado como un int
            int año = (int) comboAño.getSelectedItem();
            aplicarFiltro(mes, año);
            //cargarTotales();
        });

        JPanel panelFiltro = new JPanel();
        panelFiltro.add(new JLabel("Mes:"));
        panelFiltro.add(comboMes);
        panelFiltro.add(new JLabel("Año:"));
        panelFiltro.add(comboAño);
        panelFiltro.add(btnAplicar);
        add(panelFiltro, java.awt.BorderLayout.SOUTH);
        cargarDatos();
        cargarTotales();
    }

    public void cargarTotales(String condicion) {
        String[] columnas = {"ventas_efectivo", "ventas_credito", "total_ventas", "presupuesto", "cumplimiento", "gastos"};
        Object[] filaTotal = new Object[modeloTabla.getColumnCount()];
        
        filaTotal[0] = "TOTAL";
        for (int i = 1; i < columnas.length + 1; i++) {
            filaTotal[i] = cierresDiarios.sumarColumna(columnas[i - 1], condicion);
        }
        // cumplimiento va en índice 5, se calcula aparte
        int totalVentas = cierresDiarios.sumarColumna("total_ventas", condicion);
        int totalPresupuesto = cierresDiarios.sumarColumna("presupuesto", condicion);
        filaTotal[5] = totalPresupuesto == 0 ? "0%" : Math.round((totalVentas * 100.0) / totalPresupuesto) + "%";

        modeloTabla.addRow(filaTotal);
    }

    public void cargarTotales() {
        cargarTotales(null);
    }

    private void aplicarFiltro(int mes, int año) {
        List<Map<String, String>> filas = mes == 0
            ? cierresDiarios.filtrarPorAño(año)
            : cierresDiarios.filtrarPorMes(año, mes);

        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(0);
        if (filas.isEmpty()) return;

        filas.get(0).keySet().forEach(modeloTabla::addColumn);
        for (Map<String, String> fila : filas) {
            modeloTabla.addRow(fila.values().toArray());
        }
        String condicion;
        if (mes == 0) {
            condicion = "fecha LIKE '" + año + "%'";
        } else {
            String mesStr = String.format("%04d-%02d", año, mes);
            condicion = "fecha LIKE '" + mesStr + "%'";
            
        }
        cargarTotales(condicion);
    }
}
