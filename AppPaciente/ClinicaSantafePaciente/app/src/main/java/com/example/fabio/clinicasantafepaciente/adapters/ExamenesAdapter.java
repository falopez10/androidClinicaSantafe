package com.example.fabio.clinicasantafepaciente.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fabio.clinicasantafepaciente.R;
import com.example.fabio.clinicasantafepaciente.models.Examen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fabio on 03/05/2017.
 */

public class ExamenesAdapter extends ArrayAdapter<Examen>{
    ArrayList<Examen> examenes;

    public ExamenesAdapter(Context context, ArrayList<Examen> examenes) {
        super(context, 0, examenes);
        this.examenes = examenes;
    }

    public Examen getItem(int position){
        return examenes.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Examen examen = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_examen, parent, false);
        }
        // Lookup view for data population
        TextView tvTipoE = (TextView) convertView.findViewById(R.id.tvTipoE);
        TextView tvResultadosE = (TextView) convertView.findViewById(R.id.tvResultadosE);
        TextView tvFechaE = (TextView) convertView.findViewById(R.id.tvFechaE);
        //Se obtienen las bases
        String tipoBase = tvTipoE.getText().toString();
        String resultadosBase = tvResultadosE.getText().toString();
        String fechaBase = tvFechaE.getText().toString();
        // Populate the data into the template view using the data object
        tvTipoE.setText(tipoBase + examen.tipo);
        tvResultadosE.setText(resultadosBase + examen.resultados);
        tvFechaE.setText(fechaBase + examen.fecha.toString());
        // Return the completed view to render on screen
        return convertView;
    }

    /**
     * genera la lista de examenes a partir del responseBody
     * @param responseBody
     */
    static public void traducirJSON(byte[] responseBody, ArrayList<Examen> examenes)
    {
        try {
            JSONArray jsonArray = new JSONArray(new String(responseBody));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json_data = jsonArray.getJSONObject(i);
                int id = json_data.getInt("id");
                String tipo = json_data.getString("tipo");
                String resultados = json_data.getString("resultados");
                String sFecha = json_data.getString("fecha");
                Date fecha = sFecha.equals("null") ? new Date() : new Date(Long.parseLong(sFecha));
                Examen examen = new Examen(id,tipo, resultados,fecha);
                examenes.add(examen);
            }

        } catch (JSONException e) {
            Log.d("exception", e.getMessage());
        }
    }
}
