package com.example.controller;

import java.util.List;

import com.example.db.VentaDAO;
import com.example.model.Producto;
import com.example.model.Venta;

public class VentasControlador extends ControladorCrud<Venta> {
    private final VentaDAO ventaDAO;

    public VentasControlador(){
        this(new VentaDAO());
    }

    private VentasControlador(VentaDAO ventaDAO){
        super(ventaDAO);
        this.ventaDAO = ventaDAO;
    }

    /**
     * Vende los productos dados con las cantidades dadas
     * en una sola transacción.
     * @param venta El objeto de la entidad venta.
     * @param productos La lista de productos a vender.
     * @param cantidadesPorProducto La lista de cantidades por producto a vender.
     */
    public void venderProductos(Venta venta, List<Producto> productos, List<Integer> cantidadesPorProducto) {
        this.ventaDAO.venderProductos(venta, productos, cantidadesPorProducto);
    }

}
