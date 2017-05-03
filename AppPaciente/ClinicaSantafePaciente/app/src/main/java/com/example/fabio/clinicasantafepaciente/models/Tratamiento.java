package com.example.fabio.clinicasantafepaciente.models;

import java.util.Date;

/**
 * Created by fabio on 02/05/2017.
 */

public class Tratamiento {
    public int id;
    public String descripcion;
    public Date fechaInicio;
    public Date fechaFin;

    public Tratamiento(int id, String descripcion, Date fechaInicio, Date fechaFin)
    {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String toString()
    {
        String coma = ",";
        return id+coma+descripcion+coma+fechaInicio.toString()+coma+fechaFin.toString();
    }
}
