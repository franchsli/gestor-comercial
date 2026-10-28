package com.example.controller;

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
     * Asocia el producto con el nombre dado a una venta en VENTAS_PRODUCTOS.
     * @param venta El objeto de la entidad venta.
     * @param producto El objeto de la entidad producto.
     * @param cantidadProducto La cantidad del producto en la venta.
     */
    public void vender(Venta venta, Producto producto, int cantidadProducto){
        this.ventaDAO.vender(venta, producto, cantidadProducto);
    }

}
