package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.model.Presupuesto;

public class PresupuestoDAO extends DAOCrud<Presupuesto>{

    public PresupuestoDAO(){
        super("PRESUPUESTOS", "(fecha, tipo, valor)");
    }

    @Override
    public Presupuesto mapear(ResultSet resultSet) throws SQLException{
        return new Presupuesto(resultSet.getInt("id"), fechaDeColumna(resultSet, "fecha"),
                Presupuesto.Tipo.valueOf(resultSet.getString("tipo")), resultSet.getInt("valor"));
    }

    @Override
    public void actualizar(Presupuesto presupuesto){
        String sql = "UPDATE " + nombreTabla + " SET fecha = ?, tipo = ?, valor = ? WHERE id = ?";
        ejecutar(sql, presupuesto.getFecha(), presupuesto.getTipo(), presupuesto.getValor(), presupuesto.getId());
    }

    @Override
    public void insertar(Presupuesto presupuesto){
        presupuesto.setId(insertarFila(presupuesto.getFecha(), presupuesto.getTipo(), presupuesto.getValor()));
    }
    
}
