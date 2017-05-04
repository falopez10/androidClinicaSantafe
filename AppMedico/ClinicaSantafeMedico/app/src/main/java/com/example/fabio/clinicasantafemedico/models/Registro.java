package com.example.fabio.clinicasantafemedico.models;

import java.util.Date;

/**
 * Created by fabio on 02/05/2017.
 */

public class Registro {
    public int id;
    public Date fechaExpedicion;
    public int frecuenciaCardiaca;
    public int presionSanguinea1;
    public int presionSanguinea2;
    public int nivelEstres;
    public int nivelActividadFisica;

    public Registro(int id, Date fechaExpedicion, int frecuenciaCardiaca, int presionSanguinea1,
                    int presionSanguinea2, int nivelEstres, int nivelActividadFisica) {
        this.id = id;
        this.fechaExpedicion = fechaExpedicion;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.presionSanguinea1 = presionSanguinea1;
        this.presionSanguinea2 = presionSanguinea2;
        this.nivelEstres = nivelEstres;
        this.nivelActividadFisica = nivelActividadFisica;
    }

    @Override
    public String toString() {
        String coma = ",";
        return id+coma+fechaExpedicion+coma+frecuenciaCardiaca+coma+presionSanguinea1+coma
                +presionSanguinea2+coma+nivelEstres+coma+nivelActividadFisica;
    }
}
