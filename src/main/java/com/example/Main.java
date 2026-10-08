package com.example;
import javax.swing.JTabbedPane;

import com.example.db.DBConnection;
import com.example.gui.CierreDiarioPanel;
import com.example.gui.GastosPanel;
import com.example.gui.Panel;
import com.example.gui.PresupuestosPanel;
import com.example.gui.ProductosPanel;
import com.example.gui.Ventana;
import com.example.gui.VentasPanel;

public class Main {
    public static void main(String[] args) {
        JTabbedPane pestañas = new JTabbedPane();
        ProductosPanel pestañaProductos = new ProductosPanel();
        GastosPanel pestañaGastos = new GastosPanel();
        VentasPanel pestañaVentas = new VentasPanel();
        PresupuestosPanel pestañaPresupuestos = new PresupuestosPanel();
        CierreDiarioPanel pestañaCierresDiarios = new CierreDiarioPanel();
        // se añaden las pestañas
        pestañas.addTab("Productos", pestañaProductos);
        pestañas.addTab("Ventas", pestañaVentas);
        pestañas.addTab("Gastos", pestañaGastos);
        pestañas.addTab("Presupuestos", pestañaPresupuestos);
        pestañas.addTab("Cierres diarios", pestañaCierresDiarios);
        // se recarga la pestaña seleccionada
        pestañas.addChangeListener(e -> {
            Panel pestañaSeleccionada = (Panel) pestañas.getSelectedComponent();
            pestañaSeleccionada.cargarDatos();
            // si la pestaña es la de Cierres diarios, recarga los totales
            if (pestañaSeleccionada instanceof CierreDiarioPanel){
                ((CierreDiarioPanel)pestañaSeleccionada).cargarTotales();
            }
        });
        Ventana ventana = new Ventana();
        ventana.add(pestañas);
        DBConnection.cerrar();
    }
}