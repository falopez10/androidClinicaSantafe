package com.example.fabio.clinicasantafemedico;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class DetallePacienteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Paciente paciente;
    private TextView tvDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_paciente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Se genera el paciente!
        Intent intent = getIntent();
        String pacMsg = intent.getStringExtra(GetPacientesActivity.EXTRA_MESSAGE);
        paciente = new Paciente(pacMsg);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //--------------------------------FUNCIONALIDADES-----------------------------------------//
        tvDetalle = (TextView) findViewById(R.id.tvDetalle);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detalle_paciente, menu);
        //Se agregan datos del paciente al navigation header
        TextView paciente_header = (TextView) findViewById(R.id.paciente_header);
        paciente_header.setText(paciente.nombre +" "+paciente.apellido);
        TextView paciente_correo_header = (TextView) findViewById(R.id.paciente_correo_header);
        paciente_correo_header.setText(paciente.correo);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tratamientos) {
            // Handle the camera action
            requestTratamientosHttp();
        } else if (id == R.id.nav_examenes) {
            requestExamenesHttp();
        } else if (id == R.id.nav_diagnosticos) {
            requestDiagnosticosHttp();
        } else if (id == R.id.nav_registros) {
            requestRegistrosHttp();
        } else if (id == R.id.nav_consejo) {
            desplegarFormatoEnvioConsejo();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void requestTratamientosHttp()
    {
        String urlGetTratamientos = MainActivity.IP_Y_PUERTO+"/paciente/tratamientoID/"+paciente.id;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlGetTratamientos, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    String responseTxt = new String(responseBody);
                    tvDetalle.setText(responseTxt);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fallo: ", error.getMessage());
                requestTratamientosHttp();
            }
        });
    }

    private void requestRegistrosHttp()
    {
        String urlGetRegistros = MainActivity.IP_Y_PUERTO+"/paciente/registroID/"+paciente.id;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlGetRegistros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    String responseTxt = new String(responseBody);

                    tvDetalle.setText(responseTxt);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fallo: ", error.getMessage());
                requestRegistrosHttp();
            }
        });
    }

    private void requestExamenesHttp()
    {
        String urlGetExamenes = MainActivity.IP_Y_PUERTO+"/paciente/examenID/"+paciente.id ;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlGetExamenes, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    String responseTxt = new String(responseBody);
                    tvDetalle.setText(responseTxt);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fallo: ", error.getMessage());
                requestExamenesHttp();
            }
        });
    }

    private void requestDiagnosticosHttp()
    {
        String urlGetDiagnosticos = MainActivity.IP_Y_PUERTO+"/paciente/diagnosticoID/"+paciente.id ;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlGetDiagnosticos, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    String responseTxt = new String(responseBody);
                    tvDetalle.setText(responseTxt);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fallo: ", error.getMessage());
                requestDiagnosticosHttp();
            }
        });
    }

    private void desplegarFormatoEnvioConsejo() {
        //Se hacen visibles solo los campos para enviar consejo
        tvDetalle.setVisibility(View.GONE);
        EditText editAsunto = (EditText)findViewById(R.id.editAsunto);
        editAsunto.setVisibility(View.VISIBLE);
        EditText editConsejo = (EditText)findViewById(R.id.editConsejo);
        editConsejo.setVisibility(View.VISIBLE);
        Button btnEnviarConsejo = (Button)findViewById(R.id.btnEnviarConsejo);
        btnEnviarConsejo.setVisibility(View.VISIBLE);
    }

    public void enviarConsejo(View view){
        //Se envia y se encarga de regresar al estado original
        EditText editAsunto = (EditText)findViewById(R.id.editAsunto);
        EditText editConsejo = (EditText)findViewById(R.id.editConsejo);
        Button btnEnviarConsejo = (Button)findViewById(R.id.btnEnviarConsejo);

        String asunto = editAsunto.getText().toString();
        String descripcion = editConsejo.getText().toString();

        requestPostConsejoHttp(asunto, descripcion);
        Toast.makeText(this, "Consejo enviado!", Toast.LENGTH_SHORT).show();
        tvDetalle.setVisibility(View.VISIBLE);
        editAsunto.setVisibility(View.GONE);
        editConsejo.setVisibility(View.GONE);
        btnEnviarConsejo.setVisibility(View.GONE);


    }

    private void requestPostConsejoHttp(final String asunto, final String descripcion)
    {
        String urlPostPaciente = MainActivity.IP_Y_PUERTO+"/consejo/"+1+"/"+paciente.id;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("asunto", asunto);
        params.put("descripcion", descripcion);

        client.post(urlPostPaciente, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fallo, response: ", error.getMessage());
                requestPostConsejoHttp(asunto, descripcion);
            }
        });
    }


}
