package com.example.controller;

import com.example.db.DAOCrud;

public abstract class ControladorCrud<T> extends ControladorLectura<T> {
    private DAOCrud<T> daoCrud;

    public ControladorCrud(DAOCrud<T> daoCrud) {
        super(daoCrud);
        this.daoCrud = daoCrud;
    }

    /**
     * Inserta la entidad dada con sus valores en la tabla.
     * @param entidad La entidad a guardar en la tabla.
     */
    public void insertar(T entidad){
        daoCrud.insertar(entidad);
    }

    /**
     * Actualiza la fila cuya clave primaria coincida con
     * la de la entidad. La fila es actualizada
     * con los datos de la entidad ingresada.
     */
    public void actualizar(T entidad){
        daoCrud.actualizar(entidad);
    }

    /**
     * Borra todos los datos de una tabla que
     * cumplan la condición dada.
     * @param condicion La condición que deben
     * cumplir los datos para ser borrados.
     * @param params Los parámetros necesarios
     * para rellenar la condición propuesta.
     */
    public void borrarTodos(String condicion, Object... params){
        daoCrud.borrarTodos(condicion, params);
    }
    
}
