package com.example.fabio.clinicasantafepaciente.models;

/**
 * Created by fabio on 02/05/2017.
 */

public class Consejo {
    public int id;
    public String asunto;
    public String descripcion;

    public Consejo(int id, String asunto, String descripcion) {
        this.id = id;
        this.asunto = asunto;
        this.descripcion = descripcion;
    }

    public String toString()
    {
        String coma = ",";
        return id+coma+asunto+coma+descripcion;
    }


}
