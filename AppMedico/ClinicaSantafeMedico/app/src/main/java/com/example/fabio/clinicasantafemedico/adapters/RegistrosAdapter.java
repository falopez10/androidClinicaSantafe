package com.example.fabio.clinicasantafemedico.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fabio.clinicasantafemedico.R;
import com.example.fabio.clinicasantafemedico.models.Registro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fabio on 03/05/2017.
 */

public class RegistrosAdapter extends ArrayAdapter<Registro> {
    ArrayList<Registro> registros;

    public RegistrosAdapter(Context context, ArrayList<Registro> registros) {
        super(context, 0, registros);
        this.registros = registros;
    }

    public Registro getItem(int position){
        return registros.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Registro registro = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_registro, parent, false);
        }
        // Lookup view for data population
        TextView tvFechaExpedicionR = (TextView) convertView.findViewById(R.id.tvFechaExpedicionR);
        TextView tvFrecuenciaCardiaca = (TextView) convertView.findViewById(R.id.tvFrecuenciaCardiaca);
        TextView tvPresionSanguinea1 = (TextView) convertView.findViewById(R.id.tvPresionSanguinea1);
        TextView tvPresionSanguinea2 = (TextView) convertView.findViewById(R.id.tvPresionSanguinea2);
        TextView tvNivelEstres = (TextView) convertView.findViewById(R.id.tvNivelEstres);
        TextView tvNivelActividadFisica = (TextView) convertView.findViewById(R.id.tvNivelActividadFisica);
        //Se obtienen las bases
        String fechaExpedicionBase = tvFechaExpedicionR.getText().toString();
        String frecuenciaCardiacaBase = tvFrecuenciaCardiaca.getText().toString();
        String presionSanguinea1Base = tvPresionSanguinea1.getText().toString();
        String presionSanguinea2Base = tvPresionSanguinea2.getText().toString();
        String nivelEstresBase = tvNivelEstres.getText().toString();
        String nivelActividadFisicaBase = tvNivelActividadFisica.getText().toString();
        // Populate the data into the template view using the data object
        tvFechaExpedicionR.setText(fechaExpedicionBase + registro.fechaExpedicion);
        tvFrecuenciaCardiaca.setText(frecuenciaCardiacaBase + registro.frecuenciaCardiaca);
        tvPresionSanguinea1.setText(presionSanguinea1Base + registro.presionSanguinea1);
        tvPresionSanguinea2.setText(presionSanguinea2Base + registro.presionSanguinea2);
        tvNivelEstres.setText(nivelEstresBase + registro.nivelEstres);
        tvNivelActividadFisica.setText(nivelActividadFisicaBase + registro.nivelActividadFisica);
        // Return the completed view to render on screen
        return convertView;
    }

    /**
     * genera la lista de registros a partir del responseBody
     * @param responseBody
     */
    static public void traducirJSON(byte[] responseBody, ArrayList<Registro> registros)
    {
        try {
            JSONArray jsonArray = new JSONArray(new String(responseBody));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json_data = jsonArray.getJSONObject(i);
                int id = json_data.getInt("id");
                String sFechaExpedicion = json_data.getString("fechaExpedicion");
                Date fechaExpedicion = sFechaExpedicion.equals("null") ? new Date() : new Date(Long.parseLong(sFechaExpedicion));
                int frecuenciaCardiaca = json_data.getInt("frecuenciaCardiaca");
                int presionSanguinea1 = json_data.getInt("presionSanguinea1");
                int presionSanguinea2 = json_data.getInt("presionSanguinea2");
                int nivelEstres = json_data.getInt("nivelEstres");
                int nivelActividadFisica = json_data.getInt("nivelActividadFisica");
                Registro registro = new Registro(id,fechaExpedicion,frecuenciaCardiaca,presionSanguinea1,presionSanguinea2,nivelEstres,nivelActividadFisica);
                registros.add(registro);
            }

        } catch (JSONException e) {
            Log.d("exception", e.getMessage());
        }
    }
}
