package com.example.fabio.clinicasantafemedico.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fabio.clinicasantafemedico.R;
import com.example.fabio.clinicasantafemedico.models.Paciente;

import java.util.ArrayList;

/**
 * Created by fabio on 27/04/2017.
 * Es el intermediario entre un arreglo de pacientes y la listView que creamos para la interfaz
 */

public class PacientesAdapter extends ArrayAdapter<Paciente> {

    ArrayList<Paciente> pacientes;

    public PacientesAdapter(Context context, ArrayList<Paciente> pacientes) {
        super(context, 0, pacientes);
        this.pacientes = pacientes;
    }

    public Paciente getItem(int position){
        return pacientes.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Paciente paciente = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_paciente, parent, false);
        }
        // Lookup view for data population
        TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
        TextView tvNombre = (TextView) convertView.findViewById(R.id.tvNombre);
        TextView tvApellido = (TextView) convertView.findViewById(R.id.tvApellido);
        TextView tvCorreo = (TextView) convertView.findViewById(R.id.tvCorreo);
        TextView tvEdad = (TextView) convertView.findViewById(R.id.tvEdad);
        TextView tvSexo = (TextView) convertView.findViewById(R.id.tvSexo);
        // Populate the data into the template view using the data object
        tvId.setText(String.valueOf(paciente.id));
        tvNombre.setText(paciente.nombre);
        tvApellido.setText(paciente.apellido);
        tvCorreo.setText(paciente.correo);
        tvEdad.setText(String.valueOf(paciente.edad) + " años");
        tvSexo.setText(paciente.sexo);
        // Return the completed view to render on screen
        return convertView;
    }
}
