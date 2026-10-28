package com.example.controller;

import com.example.db.GastoDAO;
import com.example.model.Gasto;

public class GastosControlador extends ControladorCrud<Gasto> {
    public GastosControlador() {
        super(new GastoDAO());
    }
}
