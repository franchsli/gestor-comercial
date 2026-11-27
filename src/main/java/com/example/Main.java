package com.example;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.example.db.DBConnection;
import com.example.gui.CierresDiariosPanel;
import com.example.gui.GastosPanel;
import com.example.gui.PanelLectura;
import com.example.gui.PresupuestosPanel;
import com.example.gui.ProductosPanel;
import com.example.gui.Ventana;
import com.example.gui.VentasPanel;

public class Main {
    public static void main(String[] args) {
        // Se específica el texto de los botones para que estén en español.
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");
        JTabbedPane pestañas = new JTabbedPane();
        ProductosPanel pestañaProductos = new ProductosPanel();
        GastosPanel pestañaGastos = new GastosPanel();
        VentasPanel pestañaVentas = new VentasPanel();
        PresupuestosPanel pestañaPresupuestos = new PresupuestosPanel();
        CierresDiariosPanel pestañaCierresDiarios = new CierresDiariosPanel();
        // se añaden las pestañas
        pestañas.addTab("Productos", pestañaProductos);
        pestañas.addTab("Ventas", pestañaVentas);
        pestañas.addTab("Gastos", pestañaGastos);
        pestañas.addTab("Presupuestos", pestañaPresupuestos);
        pestañas.addTab("Cierres diarios", pestañaCierresDiarios);
        // se recarga la pestaña seleccionada
        pestañas.addChangeListener(e -> {
            PanelLectura<?> pestañaSeleccionada = (PanelLectura<?>) pestañas.getSelectedComponent();
            pestañaSeleccionada.cargarDatos();
            // si la pestaña es la de Cierres diarios, recarga los totales
            if (pestañaSeleccionada instanceof CierresDiariosPanel){
                ((CierresDiariosPanel)pestañaSeleccionada).cargarTotales();
            }
        });
        Ventana ventana = new Ventana();
        ventana.add(pestañas);
        DBConnection.cerrar();
    }
}