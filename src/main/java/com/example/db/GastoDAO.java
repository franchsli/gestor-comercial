package com.example.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.model.Gasto;
import com.example.model.Presupuesto;

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
        Integer idPresupuesto = gasto.getIdPresupuesto();
        verificarTipoPresupuestoEgreso(idPresupuesto);
        gasto.setId(insertarFila(gasto.getFechaGasto(), gasto.getEstado(), gasto.getValor(),
                        gasto.getFechaPago(), gasto.getDescripcion(), idPresupuesto));
    }

    @Override
    public void actualizar(Gasto gasto){
        Integer idPresupuesto = gasto.getIdPresupuesto();
        verificarTipoPresupuestoEgreso(idPresupuesto);
        String sql = "UPDATE " + nombreTabla + " SET fecha_gasto = ?, estado = ?, valor = ?, fecha_pago = ?, descripcion = ?, id_presupuesto = ? WHERE id = ?";
        ejecutar(sql, gasto.getFechaGasto(), gasto.getEstado(), gasto.getValor(), gasto.getFechaPago(), 
                    gasto.getDescripcion(), idPresupuesto, gasto.getId());
    }

    /**
     * Verifica que el presupuesto con el id dado es de tipo EGRESO.
     * Esto es así ya que no tiene sentido que se le asigne un presupuesto
     * para prever ingresos a un gasto.
     * @param idPresupuesto El id del presupuesto a verificar.
     */
    private void verificarTipoPresupuestoEgreso(Integer idPresupuesto) {
        if (idPresupuesto == null) return;
        PresupuestoDAO presupuestoDAO = new PresupuestoDAO();
        Presupuesto presupuesto = presupuestoDAO.unicoRegistro("id", idPresupuesto);
        if (presupuesto == null) {
            throw new IllegalArgumentException("El presupuesto no existe.");
        }
        if (presupuesto.getTipo() != Presupuesto.Tipo.EGRESO) {
            throw new IllegalArgumentException("El presupuesto debe ser de tipo EGRESO.");
        }
    }

}
