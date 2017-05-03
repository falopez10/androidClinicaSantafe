package com.example.fabio.clinicasantafepaciente.models;

/**
 * Created by fabio on 01/05/2017.
 */

public class Medico {
    public int id;
    public String nombre;
    public String apellido;
    public String correo;

    public Medico(int id, String nombre, String apellido, String correo)
    {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
    }
}
