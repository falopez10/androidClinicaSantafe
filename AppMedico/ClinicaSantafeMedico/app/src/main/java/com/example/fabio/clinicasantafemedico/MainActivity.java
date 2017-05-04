package com.example.fabio.clinicasantafemedico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.fabio.clinicasantafemedico.models.Medico;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    public final static String IP_Y_PUERTO = "http://172.24.42.68:9000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestGetMedicoHttp();//Se obtiene el medico y se agregan los valores
    }

    public void requestGetMedicoHttp()
    {
        int idMedico = 1;
        String urlGetMedico = MainActivity.IP_Y_PUERTO+"/medico/"+idMedico; //Por ahora siempre es Carlos Castro
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlGetMedico, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    traducirJSON(responseBody);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fallo en get: ", error.getMessage());
                requestGetMedicoHttp();
            }
        });
    }

    private void traducirJSON(byte[] responseBody) {
        try {
            JSONObject jObject = new JSONObject(new String(responseBody));
            int id = jObject.getInt("id");
            String nombre = jObject.getString("nombre");
            String apellido = jObject.getString("apellido");
            String correo = jObject.getString("correo");

            TextView tvNombreM = (TextView)findViewById(R.id.tvNombreM);
            TextView tvApellidoM = (TextView)findViewById(R.id.tvApellidoM);
            TextView tvCorreoM =  (TextView) findViewById(R.id.tvCorreoM);

            tvNombreM.setText(nombre);
            tvApellidoM.setText(apellido);
            tvCorreoM.setText(correo);

            setContentView(R.layout.activity_main);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** SE USA cuando desde xml se oprime boton get */
    public void getPacientes(View view) {
        //El intent no hace nada mas que asignar la activity pues no le metimos un string como parametro
        // ni nada pero puede ser util para el POST.
        Intent intent = new Intent(this, GetPacientesActivity.class);
        startActivity(intent);

    }









}
