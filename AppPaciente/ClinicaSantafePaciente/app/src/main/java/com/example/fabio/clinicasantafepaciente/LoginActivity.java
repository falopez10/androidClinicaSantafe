package com.example.fabio.clinicasantafepaciente;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{


    public final static String EXTRA_MESSAGE = "com.example.fabio.MESSAGE";
    public final static String LOG_OUT = "Log out";
    public static final String IP_Y_PUERTO = "http://172.24.42.68:9000";
    ArrayList<Paciente> pacientes = new ArrayList<Paciente>();
    Paciente pacienteEscogido;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button email_sign_in_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Carga los pacientes
        requestGetHttp();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        email_sign_in_button = (Button) findViewById(R.id.email_sign_in_button);
        mPasswordView = (EditText) findViewById(R.id.password);


    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin(View view) {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            asignarPaciente(email);
            if(pacienteEscogido != null)
            {
                Intent intent = new Intent(this,DetallePacienteActivity.class);
                //based on item add info to intent
                intent.putExtra(EXTRA_MESSAGE, pacienteEscogido.toString());
//                mEmailView.setVisibility(View.INVISIBLE);
//                mPasswordView.setVisibility(View.INVISIBLE);
//                email_sign_in_button.setText("Ingresar");
                startActivity(intent);

            }
            else
                Toast.makeText(this, "Correo desconocido", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }



    private void requestGetHttp()
    {
        String urlGetPacientes = IP_Y_PUERTO+"/clinica/pacienteID";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlGetPacientes, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {

                    //agrega los pacientes a la lista
                    traducirJSON(responseBody);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fallo en get: ", error.getMessage());
                requestGetHttp();
            }
        });
    }


    private void traducirJSON(byte[] responseBody)
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
            }
        } catch (JSONException e) {
            Log.d("exception", e.getMessage());
        }
    }

    private void asignarPaciente(String correo)
    {
        for (Paciente paciente : pacientes)
        {
            if(paciente.correo.equals(correo))
            {
                pacienteEscogido = paciente;
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }


}

