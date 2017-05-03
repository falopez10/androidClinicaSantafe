package com.example.fabio.clinicasantafemedico.models;

import com.example.fabio.clinicasantafemedico.models.Medico;

import java.util.ArrayList;

/**
 * Created by fabio on 26/04/2017.
 */

public class Paciente {
    public int id;
    public String nombre;
    public String apellido;
    public String correo;
    public int edad;
    public double peso;
    public double estatura;
    public String sexo;
    public ArrayList<Medico> medicos;

    public Paciente(int id, String nombre, String apellido, String correo, int edad, double peso, double estatura, String sexo, ArrayList<Medico> medicos)
    {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.edad = edad;
        this.peso = peso;
        this.estatura = estatura;
        this.sexo = sexo;
        this.medicos = medicos;
    }

    public Paciente(String sPaciente)
    {
        String[] arrPaciente = sPaciente.split(",");
        this.id = Integer.parseInt(arrPaciente[0]);
        this.nombre = arrPaciente[1];
        this.apellido = arrPaciente[2];
        this.correo = arrPaciente[3];
        this.edad = Integer.parseInt(arrPaciente[4]);
        this.peso = Double.parseDouble(arrPaciente[5]);
        this.estatura = Double.parseDouble(arrPaciente[6]);
        this.sexo = arrPaciente[7];
    }

    public String toString()
    {
        String coma = ",";
        return id + coma + nombre+ coma+ apellido+coma + correo+ coma + edad+ coma + peso
                + coma + estatura+ coma + sexo;
    }



}
