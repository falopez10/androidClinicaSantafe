package com.example.fabio.clinicasantafepaciente.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fabio.clinicasantafepaciente.R;
import com.example.fabio.clinicasantafepaciente.models.Tratamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fabio on 03/05/2017.
 */

public class TratamientosAdapter extends ArrayAdapter<Tratamiento> {
    ArrayList<Tratamiento> tratamientos;

    public TratamientosAdapter(Context context, ArrayList<Tratamiento> tratamientos) {
        super(context, 0, tratamientos);
        this.tratamientos = tratamientos;
    }

    public Tratamiento getItem(int position){
        return tratamientos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Tratamiento tratamiento = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tratamiento, parent, false);
        }
        // Lookup view for data population
        TextView tvDescT = (TextView) convertView.findViewById(R.id.tvDescT);
        TextView tvFechaInicioT = (TextView) convertView.findViewById(R.id.tvFechaInicioT);
        TextView tvFechaFinT = (TextView) convertView.findViewById(R.id.tvFechaFinT);
        //Se obtienen las bases...
        String descBase = tvDescT.getText().toString();
        String fechaIBase = tvFechaInicioT.getText().toString();
        String fechaFBase = tvFechaFinT.getText().toString();
        // Populate the data into the template view using the data object
        tvDescT.setText(descBase + tratamiento.descripcion);
        tvFechaInicioT.setText(fechaIBase + tratamiento.fechaInicio.toString());
        tvFechaFinT.setText(fechaFBase + tratamiento.fechaFin.toString());
        // Return the completed view to render on screen
        return convertView;
    }

    /**
     * genera la lista de tratamientos a partir del responseBody
     * @param responseBody
     */
    static public void traducirJSON(byte[] responseBody, ArrayList<Tratamiento> tratamientos)
    {
        try {
            JSONArray jsonArray = new JSONArray(new String(responseBody));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json_data = jsonArray.getJSONObject(i);
                int id = json_data.getInt("id");
                String descripcion = json_data.getString("descripcion");
                String sFechaI = json_data.getString("fechaInicio");
                String sFechaF = json_data.getString("fechaFin");
                //fecha inicio por default es ayer
                Date fechaInicio = sFechaI.equals("null") ? new Date(new Date().getTime()-3600*1000*24) : new Date(Long.parseLong(sFechaI));
                //fecha fin por default es hoy
                Date fechaFin = sFechaF.equals("null") ? new Date() : new Date(Long.parseLong(sFechaF));
                Tratamiento tratamiento = new Tratamiento(id,descripcion,fechaInicio, fechaFin);
                tratamientos.add(tratamiento);
            }

        } catch (JSONException e) {
            Log.d("exception", e.getMessage());
        }
    }
}
