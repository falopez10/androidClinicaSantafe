package com.example.fabio.clinicasantafemedico.models;

import java.util.Date;

/**
 * Created by fabio on 02/05/2017.
 */

public class Examen {
    public int id;
    public String tipo;
    public String resultados;
    public Date fecha;

    public Examen(int id, String tipo, String resultados, Date fecha)
    {
        this.id = id;
        this.tipo = tipo;
        this.resultados = resultados;
        this.fecha = fecha;
    }

    public String toString()
    {
        String coma = ",";
        return id+coma+tipo+coma+resultados+coma+fecha;
    }
}
