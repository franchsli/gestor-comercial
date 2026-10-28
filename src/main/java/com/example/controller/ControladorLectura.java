package com.example.controller;

import java.util.List;

import com.example.db.DAOLectura;

public class ControladorLectura<T> {
    private DAOLectura<T> daoLectura;

    public ControladorLectura(DAOLectura<T> daoLectura) {
        this.daoLectura = daoLectura;
    }

    /**
     *Devuelve todos los datos de la tabla que cumplen la condición dada.
     * @param condicion La condición que los datos deben cumplir.
     * @param params Los parámetros necesarios pra la condicion.
     * @return Los datos que cumplen con la condición.
     */
    public List<T> todos(String condicion, Object... params){
        return daoLectura.todos(condicion, params);
    }
}
