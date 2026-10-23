package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GastoDAO extends DAOCrud<Gasto> {

    public GastoDAO(){
        super("GASTOS", "(fecha_gasto, estado, valor, fecha_pago, descripcion, fecha_presupuesto)");
    }

    @Override
    public Gasto mapear(ResultSet resultSet) throws SQLException {
        return new Gasto(resultSet.getInt("id"), fechaDeColumna(resultSet, "fecha_gasto"),
                    Gasto.Estado.valueOf(resultSet.getString("estado")), resultSet.getInt("valor"),
                    fechaDeColumna(resultSet, "fecha_pago"), resultSet.getString("descripcion"),
                    fechaDeColumna(resultSet, "fecha_presupuesto"));
    }

    @Override
    public void insertar(Gasto gasto){
        insertarFila(gasto.getFechaGasto(), gasto.getEstado(), gasto.getValor(),
                        gasto.getFechaPago(), gasto.getDescripcion(), gasto.getFechaPresupuesto());
    }

    @Override
    public void actualizar(Gasto gasto){
        String sql = "UPDATE " + nombreTabla + " SET fecha_gasto = ?, estado = ?, valor = ?, fecha_pago = ?, descripcion = ?, fecha_presupuesto = ? WHERE id = ?";
        ejecutar(sql, gasto.getFechaGasto(), gasto.getEstado(), gasto.getValor(), gasto.getFechaPago(), 
                    gasto.getDescripcion(), gasto.getFechaPresupuesto(), gasto.getId());
    }

    
}
