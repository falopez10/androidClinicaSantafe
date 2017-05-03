package com.example.fabio.clinicasantafemedico.models;

import java.util.Date;

/**
 * Created by fabio on 02/05/2017.
 */

public class Diagnostico {
    public int id;
    public String descripcion;
    public Date fecha;

    public Diagnostico(int id, String descripcion, Date fecha)
    {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public String toString()
    {
        String coma = ",";
        return id+coma+descripcion+coma+fecha;
    }
}
