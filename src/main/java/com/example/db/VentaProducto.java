package com.example.db;

public class VentaProducto {
    private int idVenta;
    private int idProducto;
    private int cantidadProducto;

    public VentaProducto(int idVenta, int idProducto, int cantidadProducto){
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidadProducto = cantidadProducto;
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
