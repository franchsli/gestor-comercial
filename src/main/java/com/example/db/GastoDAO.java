package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.model.Gasto;

public class GastoDAO extends DAOCrud<Gasto> {

    public GastoDAO(){
        super("GASTOS", "(fecha_gasto, estado, valor, fecha_pago, descripcion, id_presupuesto)");
    }

    @Override
    public Gasto mapear(ResultSet resultSet) throws SQLException {
        int idPresupuestoRaw = resultSet.getInt("id_presupuesto");
        Integer idPresupuesto = resultSet.wasNull() ? null : idPresupuestoRaw;
        return new Gasto(resultSet.getInt("id"), fechaDeColumna(resultSet, "fecha_gasto"),
                    Gasto.Estado.valueOf(resultSet.getString("estado")), resultSet.getInt("valor"),
                    fechaDeColumna(resultSet, "fecha_pago"), resultSet.getString("descripcion"),
                    idPresupuesto);
    }

    @Override
    public void insertar(Gasto gasto){
        gasto.setId(insertarFila(gasto.getFechaGasto(), gasto.getEstado(), gasto.getValor(),
                        gasto.getFechaPago(), gasto.getDescripcion(), gasto.getIdPresupuesto()));
    }

    @Override
    public void actualizar(Gasto gasto){
        String sql = "UPDATE " + nombreTabla + " SET fecha_gasto = ?, estado = ?, valor = ?, fecha_pago = ?, descripcion = ?, id_presupuesto = ? WHERE id = ?";
        ejecutar(sql, gasto.getFechaGasto(), gasto.getEstado(), gasto.getValor(), gasto.getFechaPago(), 
                    gasto.getDescripcion(), gasto.getIdPresupuesto(), gasto.getId());
    }

}
