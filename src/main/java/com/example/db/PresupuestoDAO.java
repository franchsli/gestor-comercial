package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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

    /**
     * Actualiza el presupuesto en la tabla con la fecha
     * dada y le asigna la nueva fecha y valor que están
     * en el objeto de la entidad.
     * @param presupuesto El objeto de la entidad.
     * @param fechaAnterior La fecha que tenía el presupuesto
     * antes de la actualización.
     */
    public void actualizar(Presupuesto presupuesto, LocalDate fechaAnterior){
        String sql = "UPDATE " + nombreTabla + " SET fecha = ?, valor = ?, tipo = ? WHERE fecha = ? AND tipo = ?";
        ejecutar(sql, presupuesto.getFecha(), presupuesto.getValor(), presupuesto.getTipo(), fechaAnterior, presupuesto.getTipo());

    }

    @Override
    public void insertar(Presupuesto presupuesto){
        insertarFila(presupuesto.getFecha(), presupuesto.getTipo(), presupuesto.getValor());
    }
    
}
