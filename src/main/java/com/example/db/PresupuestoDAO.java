package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PresupuestoDAO extends DAO<Presupuestoo>{

    public PresupuestoDAO(){
        super("PRESUPUESTOS", "(fecha, valor)");
    }

    public Presupuestoo mapear(ResultSet resultSet) throws SQLException{
        return new Presupuestoo(fechaDeColumna(resultSet, "fecha"), resultSet.getInt("valor"));
    }

    public void actualizar(Presupuestoo presupuesto){
        String sql = "UPDATE " + nombreTabla + " SET fecha = ?, valor = ? WHERE fecha = ?";
        LocalDate fecha = presupuesto.getFecha();
        ejecutar(sql, fecha, presupuesto.getValor(), fecha);
    }

    public void insertar(Presupuestoo presupuesto){
        insertarFila(presupuesto.getFecha(), presupuesto.getValor());
    }
    
}
