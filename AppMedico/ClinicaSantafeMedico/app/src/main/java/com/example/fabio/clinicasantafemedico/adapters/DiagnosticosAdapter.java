package com.example.fabio.clinicasantafemedico.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.fabio.clinicasantafemedico.R;
import com.example.fabio.clinicasantafemedico.models.Diagnostico;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fabio on 27/04/2017.
 * Es el intermediario entre un arreglo de diagnosticos y la listView que creamos para la interfaz
 * Su metodo estático permite poblar un arreglo desde un responseBody...
 */

public class DiagnosticosAdapter extends ArrayAdapter<Diagnostico> {

    ArrayList<Diagnostico> diagnosticos;

    public DiagnosticosAdapter(Context context, ArrayList<Diagnostico> diagnosticos) {
        super(context, 0, diagnosticos);
        this.diagnosticos = diagnosticos;
    }

    public Diagnostico getItem(int position){
        return diagnosticos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Diagnostico diagnostico = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_diagnostico, parent, false);
        }
        // Lookup view for data population
        TextView tvDescD = (TextView) convertView.findViewById(R.id.tvDescD);
        TextView tvFechaD = (TextView) convertView.findViewById(R.id.tvFechaD);
        //Se obtienen las bases
        String descBase = tvDescD.getText().toString();
        String fechaBase = tvFechaD.getText().toString();
        // Populate the data into the template view using the data object
        tvDescD.setText(descBase + diagnostico.descripcion);
        tvFechaD.setText(fechaBase + diagnostico.fecha.toString());
        // Return the completed view to render on screen
        return convertView;
    }

    /**
     * genera la lista de diagnosticos a partir del responseBody
     * @param responseBody
     */
    static public void traducirJSON(byte[] responseBody, ArrayList<Diagnostico> diagnosticos)
    {
        try {
            JSONArray jsonArray = new JSONArray(new String(responseBody));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json_data = jsonArray.getJSONObject(i);
                int id = json_data.getInt("id");
                String descripcion = json_data.getString("descripcion");
                String sFecha = json_data.getString("fecha");
                Date fecha = sFecha.equals("null") ? new Date() : new Date(Long.parseLong(sFecha));
                Diagnostico diagnostico = new Diagnostico(id,descripcion,fecha);
                diagnosticos.add(diagnostico);
            }

        } catch (JSONException e) {
            Log.d("exception", e.getMessage());
        }
    }
}
