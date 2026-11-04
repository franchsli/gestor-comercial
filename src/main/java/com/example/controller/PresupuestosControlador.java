package com.example.controller;


import com.example.db.PresupuestoDAO;
import com.example.model.Presupuesto;

public class PresupuestosControlador extends ControladorCrud<Presupuesto> {

    public PresupuestosControlador() {
        super(new PresupuestoDAO());
    }

}