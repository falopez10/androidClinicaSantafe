package com.example.fabio.clinicasantafepaciente.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fabio.clinicasantafepaciente.R;
import com.example.fabio.clinicasantafepaciente.models.Consejo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fabio on 03/05/2017.
 */

public class ConsejosAdapter extends ArrayAdapter<Consejo> {
    ArrayList<Consejo> consejos;

    public ConsejosAdapter(Context context, ArrayList<Consejo> consejos) {
        super(context, 0, consejos);
        this.consejos = consejos;
    }

    public Consejo getItem(int position){
        return consejos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Consejo consejo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_consejo, parent, false);
        }
        // Lookup view for data population
        TextView tvAsuntoC = (TextView) convertView.findViewById(R.id.tvAsuntoC);
        TextView tvDescC = (TextView) convertView.findViewById(R.id.tvDescC);
        //Se obtienen las bases
        String asuntoBase = tvAsuntoC.getText().toString();
        String descBase = tvDescC.getText().toString();
        // Populate the data into the template view using the data object
        tvAsuntoC.setText(asuntoBase + consejo.asunto);
        tvDescC.setText(descBase + consejo.descripcion);
        // Return the completed view to render on screen
        return convertView;
    }

    /**
     * genera la lista de consejos a partir del responseBody
     * @param responseBody
     */
    static public void traducirJSON(byte[] responseBody, ArrayList<Consejo> consejos)
    {
        try {
            JSONArray jsonArray = new JSONArray(new String(responseBody));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json_data = jsonArray.getJSONObject(i);
                int id = json_data.getInt("id");
                String asunto = json_data.getString("asunto");
                String descripcion = json_data.getString("descripcion");
                Consejo consejo = new Consejo(id,asunto,descripcion);
                consejos.add(consejo);
            }

        } catch (JSONException e) {
            Log.d("exception", e.getMessage());
        }
    }
}
