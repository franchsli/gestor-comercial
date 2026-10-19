package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VentaDAO extends DAO<Ventaa> {
    
    public VentaDAO(){
        super("VENTAS", "(fecha, valor, tipo)");
    }

    @Override
    public Ventaa mapear(ResultSet resultSet) throws SQLException {
        return new Ventaa(fechaDeColumna(resultSet, "fecha"), resultSet.getInt("valor"), 
                            Ventaa.Tipo.valueOf(resultSet.getString("tipo")));
    }

    @Override
    public void actualizar(Ventaa venta){
        String sql = "UPDATE " + nombreTabla + " SET fecha = ?, valor = ?, tipo = ? WHERE id = ?";
        ejecutar(sql, venta.getFecha(), venta.getValor(), venta.getTipo(), venta.getId());
    }

    @Override
    public void insertar(Ventaa venta){
        insertarFila(venta.getFecha(), venta.getValor(), venta.getTipo());
    }
}
