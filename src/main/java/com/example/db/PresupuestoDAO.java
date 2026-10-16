package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PresupuestoDAO extends DAO<Presupuestoo>{

    public PresupuestoDAO(){
        super("PRESUPUESTOS", "(fecha, valor)");
    }

    @Override
    public Presupuestoo mapear(ResultSet resultSet) throws SQLException{
        return new Presupuestoo(fechaDeColumna(resultSet, "fecha"), resultSet.getInt("valor"));
    }

    @Override
    public void actualizar(Presupuestoo presupuesto){
        String sql = "UPDATE " + nombreTabla + " SET valor = ? WHERE fecha = ?";
        LocalDate fecha = presupuesto.getFecha();
        ejecutar(sql, fecha, presupuesto.getValor(), fecha);
    }

    /**
     * Actualiza el presupuesto en la tabla con la fecha
     * dada y le asigna la nueva fecha y valor que están
     * en el objeto de la entidad.
     * @param presupuesto El objeto de la entidad.
     * @param fechaAnterior La fecha que tenía el presupuesto
     * antes de la actualización.
     */
    public void actualizar(Presupuestoo presupuesto, LocalDate fechaAnterior){
        String sql = "UPDATE " + nombreTabla + " SET fecha = ?, valor = ? WHERE fecha = ?";
        ejecutar(sql, presupuesto.getFecha(), presupuesto.getValor(), fechaAnterior);
    }

    @Override
    public void insertar(Presupuestoo presupuesto){
        insertarFila(presupuesto.getFecha(), presupuesto.getValor());
    }
    
}
