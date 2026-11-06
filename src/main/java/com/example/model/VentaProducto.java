package com.example.model;

public class VentaProducto {
    private int idVenta;
    private int idProducto;
    private int cantidadProducto;

    public VentaProducto(int idVenta, int idProducto, int cantidadProducto){
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        /* 
        Esto es asi ya que crear un registro en VentaProducto implica
        implicitamente la reducción de inventario por venta.
        Además, actualmente no hay manera de editar los productos
        ligados a ventas, así que ponerle un 0 es tener una inconsistencia
        que no puede ser corregida.
        */
        if (cantidadProducto <= 0) {
            throw new IllegalArgumentException("La cantidad no puede ser menor o igual a cero.");
        }
        else this.cantidadProducto = cantidadProducto;
    }

    public int getIdVenta() {
        return idVenta;
    }
    public int getIdProducto() {
        return idProducto;
    }
    public int getCantidadProducto() {
        return cantidadProducto;
    }
}
