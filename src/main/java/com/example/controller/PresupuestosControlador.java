package com.example.controller;

import java.time.LocalDate;

import com.example.db.PresupuestoDAO;
import com.example.model.Presupuesto;

public class PresupuestosControlador extends ControladorCrud<Presupuesto> {
    private final PresupuestoDAO presupuestoDAO;

    public PresupuestosControlador() {
        this(new PresupuestoDAO());
    }

    private PresupuestosControlador(PresupuestoDAO presupuestoDAO){
        super(presupuestoDAO);
        this.presupuestoDAO = presupuestoDAO;
        
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
        this.presupuestoDAO.actualizar(presupuesto, fechaAnterior);
    }

}