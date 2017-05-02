package com.example.fabio.clinicasantafemedico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

//------------------------------------------------------------------------------------------------//
//------------------------------IMPORTANTE!!! MIRAR EN MOMENTO DE DUDA----------------------------//
//------------------------------------------------------------------------------------------------//
// Al principio solo debe desplegarse la vista. El medico debe llenar el formulario de creacion en el app
// Como el decide cuando ha terminado el formulario, solo cuando presione el boton de crear se envia
// la petición POST.
// Para crear el paciente se requiere hacer varios requests:
// 1. Create: informacion basica (disponible en el DTO creado por aca)
// 2. Agregar Medico: Se le debe asignar el medico que esta logueado. El default sera 1 (Carlos Castro)
public class CreatePacienteActivity extends AppCompatActivity {

    private int idPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_paciente);

        //depronto por aca se le puede meter el parametro de id medico
        Intent intent = getIntent();


    }



    public void enviarCreateRequest(View view)
    {
        //SE OBTIENEN DATOS DE FORMULARIO
        EditText editNombre = (EditText) findViewById(R.id.editNombre);
        String pNombre = editNombre.getText().toString();

        EditText editApellido = (EditText) findViewById(R.id.editApellido);
        String pApellido = editApellido.getText().toString();

        EditText editCorreo = (EditText) findViewById(R.id.editCorreo);
        String pCorreo = editCorreo.getText().toString();

        EditText editEdad = (EditText) findViewById(R.id.editEdad);
        String pEdad = editEdad.getText().toString();

        EditText editPeso = (EditText) findViewById(R.id.editPeso);
        String pPeso = editPeso.getText().toString();

        EditText editEstatura = (EditText) findViewById(R.id.editEstatura);
        String pEstatura = editEstatura.getText().toString();

        RadioButton radioButtonF = (RadioButton) findViewById(R.id.radioButtonF);
        String pGenero = radioButtonF.isChecked() ? "Femenino" : "Masculino";

        String[] formulario = {pNombre, pApellido, pCorreo, pEdad, pPeso, pEstatura, pGenero};

        //SI SE QUIERE HACER VALIDACIONES, HACER UN IF POR ACA
        //Se genera la peticion con el formulario
        requestPostPacienteHttp(formulario);
        Toast.makeText(this, "Paciente creado exitosamente!", Toast.LENGTH_SHORT).show();
        //Se crea un nuevo intent para regresar a la lista de pacientes actualizada.
        Intent intent = new Intent(this, GetPacientesActivity.class);
        startActivity(intent);
    }

    private void requestPostPacienteHttp(final String[] formulario)
    {
        String urlPostPaciente = MainActivity.IP_Y_PUERTO+"/clinica/paciente";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("nombre", formulario[0]);
        params.put("apellido", formulario[1]);
        params.put("correo", formulario[2]);
        params.put("edad", formulario[3]);
        params.put("peso", formulario[4]);
        params.put("estatura", formulario[5]);
        params.put("sexo", formulario[6]);

        client.post(urlPostPaciente, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    TextView txtResponse = (TextView) findViewById(R.id.textLleneFormularioPac);//Si queremos meterlo en txtResponse

                    assert txtResponse != null;

                    //Ya hecha la petición, se debe procesar el JSON para saber cual es el id y agregarle un medico
                    darIdResponse(responseBody);//Se cambia valor de idPaciente
                    agregarMedico(1,idPaciente);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fallo, response: ", error.getMessage());
                requestPostPacienteHttp(formulario);
            }
        });
    }

    private void agregarMedico(int idMedico, int idPaciente)
    {
        String urlAgregarMedicoAPaciente = MainActivity.IP_Y_PUERTO+"/paciente/agregarMedico/"+idMedico+"/"+idPaciente;
        Log.d("Se agrega:","paciente "+idPaciente+" Al medico "+idMedico);

        // es un PUT con esa direccion, sin json.
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.put(urlAgregarMedicoAPaciente, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    TextView txtResponse = (TextView) findViewById(R.id.textLleneFormularioPac);//Si queremos meterlo en txtResponse

                    assert txtResponse != null;

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fallo, response: ", error.getMessage());
            }
        });
    }

    private void darIdResponse(byte[] responseBody)
    {
        try {
            String responseStr = new String(responseBody);
            Log.d("responseStr: ",responseStr);
            JSONObject jObject = new JSONObject(responseStr);
            Log.d("jObject.toString: ", jObject.toString());

            idPaciente = jObject.getInt("id");
            Log.d("id: ",String.valueOf(idPaciente));
        } catch (JSONException e) {
            Log.d("excepcion en Json",e.getMessage());
        }
    }

}
