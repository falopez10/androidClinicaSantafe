package com.example.fabio.clinicasantafemedico;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.fabio.clinicasantafemedico.adapters.PacientesAdapter;
import com.example.fabio.clinicasantafemedico.models.Paciente;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class GetPacientesActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.fabio.MESSAGE";

    ArrayList<Paciente> pacientes = new ArrayList<Paciente>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pacientes);

        Intent intent = getIntent();

        requestGetHttp();
        setContentView(R.layout.activity_get_pacientes);//Esto hace que se actualice
    }

    private void requestGetHttp()
    {
        String urlGetPacientes = MainActivity.IP_Y_PUERTO+"/clinica/pacienteID";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlGetPacientes, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    ListView listView = (ListView) findViewById(R.id.listPacientes);
//                    txtResponse.setText(new String(responseBody));
                    traducirJSON(responseBody, listView);//Falta verificar que sean del medico actual (id=1)
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fallo en get: ", error.getMessage());
                requestGetHttp();
            }
        });
    }

    /** SE USA cuando desde xml se oprimer boton post */
    public void crearPaciente(View view) {
        //El intent no hace nada mas que asignar la activity pues no le metimos un string como parametro
        // ni nada pero puede ser util para el POST.
        Intent intent = new Intent(this, CreatePacienteActivity.class);
        startActivity(intent);

    }



    private void traducirJSON(byte[] responseBody, ListView listView)
    {
        try {
            JSONArray jsonArray = new JSONArray(new String(responseBody));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json_data = jsonArray.getJSONObject(i);
                int id = json_data.getInt("id");
                String nombre = json_data.getString("nombre");
                String apellido = json_data.getString("apellido");
                String correo = json_data.getString("correo");
                int edad = json_data.getInt("edad");
                double peso = json_data.getDouble("peso");
                double estatura = json_data.getDouble("estatura");
                String sexo = json_data.getString("sexo");
                //Falta agregar medicos
                Paciente paciente = new Paciente(id, nombre, apellido, correo, edad, peso, estatura, sexo, null);
                pacientes.add(paciente);
//                Log.d(nombre, "Paciente Agregado a la lista a desplegar");
            }


            final PacientesAdapter adapter = new PacientesAdapter(this, pacientes);
            listView.setAdapter(adapter);
            final Activity thisAct = this;
            //Se configura click sobre cada item de la lista
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Paciente clickedPaciente = adapter.getItem(position);

                    Intent intent = new Intent(thisAct,DetallePacienteActivity.class);
                    //based on item add info to intent
                    intent.putExtra(EXTRA_MESSAGE, clickedPaciente.toString());
                    startActivity(intent);

                }
            });
        } catch (JSONException e) {
            Log.d("exception", e.getMessage());
        }
    }



}
