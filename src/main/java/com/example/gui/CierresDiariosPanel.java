package com.example.gui;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.example.db.CierreDiario;
import com.example.db.CierreDiarioDAO;
import com.example.db.DBException;

public class CierresDiariosPanel extends PanelLectura<CierreDiario> {
    private final CierreDiarioDAO cierreDiarioDAO;

    public CierresDiariosPanel() {
        this(new CierreDiarioDAO());
    }

    private CierresDiariosPanel(CierreDiarioDAO cierreDiarioDAO) {
        super(cierreDiarioDAO);
        this.cierreDiarioDAO = cierreDiarioDAO;

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

    @Override
    protected String[] columnas(){
        String[] columnas = {"fecha", "ventas_efectivo", "ventas_credito", "total_ventas", 
                            "presupuesto", "cumplimiento", "gastos"};
        return columnas;
    }

    @Override
    protected Object[] fila(CierreDiario cierreDiario){
        Object[] fila = {cierreDiario.getFecha(), cierreDiario.getVentasEfectivo(), cierreDiario.getVentasCredito(),
                            cierreDiario.getTotalVentas(), cierreDiario.getPresupuesto(), cierreDiario.getCumplimiento(),
                            cierreDiario.getGastos()};
        return fila;
    }

    /**
     * Carga la fila de los totales de los cierres diarios basados la condicion dada en la pestaña.
     * @param condicion La condición que deben cumplir los datos para ser considerados en el total.
     * @param params Los parámetros para construir la condición.
     */
    public void cargarTotales(String condicion, Object... params) {
        try {
            String[] columnas = {"ventas_efectivo", "ventas_credito", "total_ventas", "presupuesto", "cumplimiento", "gastos"};
            Object[] filaTotal = new Object[modeloTabla.getColumnCount()];
            
            filaTotal[0] = "TOTAL";
            for (int i = 1; i < columnas.length + 1; i++) {
                filaTotal[i] = cierreDiarioDAO.sumarColumna(columnas[i - 1], condicion, params);
            }
            // cumplimiento va en índice 5, se calcula aparte
            long totalVentas = cierreDiarioDAO.sumarColumna("total_ventas", condicion, params);
            long totalPresupuesto = cierreDiarioDAO.sumarColumna("presupuesto", condicion, params);
            filaTotal[5] = totalPresupuesto == 0 ? "0%" : Math.round((totalVentas * 100.0) / totalPresupuesto) + "%";

            modeloTabla.addRow(filaTotal);
        } catch (DBException e) {
            JOptionPane.showMessageDialog(this,
                "Error al calcular totales: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga la fila de los totales de los cierres diarios en la pestaña.
     */
    public void cargarTotales() {
        cargarTotales(null);
    }

    /**
     * Filtra los cierres diarios de la pestaña para mostrar solo los del mes y año dados.
     * @param mes El mes en el que deben situarse los cierres diarios.
     * @param año El AÑO en el que deben situarse los cierres diarios.
     */
    private void aplicarFiltro(int mes, int año) {
        try {
            boolean seFiltraPorAñoSolamente = mes == 0;
            List<CierreDiario> cierreDiarios = seFiltraPorAñoSolamente
                ? cierreDiarioDAO.filtrarPorAño(año)
                : cierreDiarioDAO.filtrarPorMes(año, mes);

            modeloTabla.setRowCount(0);
            modeloTabla.setColumnIdentifiers(columnas());
            if (cierreDiarios.isEmpty()) return;

            for (CierreDiario cierreDiario : cierreDiarios) {
                modeloTabla.addRow(fila(cierreDiario));
            }
            String condicion = "fecha >= ? AND fecha < ?";
            
            LocalDate inicio = LocalDate.of(año, seFiltraPorAñoSolamente ? 1 : mes, 1);
            LocalDate fin = seFiltraPorAñoSolamente ? inicio.plusYears(1) : inicio.plusMonths(1);
            
            cargarTotales(condicion, inicio, fin);
        } catch (DBException e) {
            JOptionPane.showMessageDialog(this,
                "Error al filtrar cierres: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
