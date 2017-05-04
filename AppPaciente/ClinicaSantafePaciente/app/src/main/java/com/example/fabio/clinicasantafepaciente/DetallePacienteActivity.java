package com.example.fabio.clinicasantafepaciente;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fabio.clinicasantafepaciente.adapters.ConsejosAdapter;
import com.example.fabio.clinicasantafepaciente.adapters.DiagnosticosAdapter;
import com.example.fabio.clinicasantafepaciente.adapters.ExamenesAdapter;
import com.example.fabio.clinicasantafepaciente.adapters.RegistrosAdapter;
import com.example.fabio.clinicasantafepaciente.adapters.TratamientosAdapter;
import com.example.fabio.clinicasantafepaciente.models.Consejo;
import com.example.fabio.clinicasantafepaciente.models.Diagnostico;
import com.example.fabio.clinicasantafepaciente.models.Examen;
import com.example.fabio.clinicasantafepaciente.models.Paciente;
import com.example.fabio.clinicasantafepaciente.models.Registro;
import com.example.fabio.clinicasantafepaciente.models.Tratamiento;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        String pacMsg = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
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
        } else if (id == R.id.nav_consejos) {
            requestConsejosHttp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void requestTratamientosHttp()
    {
        String urlGetTratamientos = LoginActivity.IP_Y_PUERTO+"/paciente/tratamientoID/"+paciente.id;
        AsyncHttpClient client = new AsyncHttpClient();
        final Activity thisAct = this;
        client.get(urlGetTratamientos, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    tvDetalle.setText("tratamientos: ");
                    ListView listView = (ListView) findViewById(R.id.list);
                    //traduccion del response a un arreglo
                    ArrayList<Tratamiento> tratamientos = new ArrayList<Tratamiento>();
                    TratamientosAdapter.traducirJSON(responseBody,tratamientos);
                    //Con arreglo se crea adapter, que genera la listView
                    final TratamientosAdapter adapter = new TratamientosAdapter(thisAct, tratamientos);
                    listView.setAdapter(adapter);
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
        String urlGetRegistros = LoginActivity.IP_Y_PUERTO+"/paciente/registroID/"+paciente.id;
        AsyncHttpClient client = new AsyncHttpClient();
        final Activity thisAct = this;
        client.get(urlGetRegistros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    tvDetalle.setText("registros: ");
                    ListView listView = (ListView) findViewById(R.id.list);
                    //traduccion del response a un arreglo
                    ArrayList<Registro> registros = new ArrayList<Registro>();
                    RegistrosAdapter.traducirJSON(responseBody,registros);
                    //Con arreglo se crea adapter, que genera la listView
                    final RegistrosAdapter adapter = new RegistrosAdapter(thisAct, registros);
                    listView.setAdapter(adapter);
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
        String urlGetExamenes = LoginActivity.IP_Y_PUERTO+"/paciente/examenID/"+paciente.id ;
        AsyncHttpClient client = new AsyncHttpClient();
        final Activity thisAct = this;
        client.get(urlGetExamenes, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    String responseTxt = new String(responseBody);
                    tvDetalle.setText("examenes: ");
                    ListView listView = (ListView) findViewById(R.id.list);
                    //traduccion del response a un arreglo
                    ArrayList<Examen> examenes = new ArrayList<Examen>();
                    ExamenesAdapter.traducirJSON(responseBody,examenes);
                    //Con arreglo se crea adapter, que genera la listView
                    final ExamenesAdapter adapter = new ExamenesAdapter(thisAct, examenes);
                    listView.setAdapter(adapter);
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
        String urlGetDiagnosticos = LoginActivity.IP_Y_PUERTO+"/paciente/diagnosticoID/"+paciente.id ;
        AsyncHttpClient client = new AsyncHttpClient();
        final Activity thisAct = this;
        client.get(urlGetDiagnosticos, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    String responseTxt = new String(responseBody);
                    tvDetalle.setText("diagnosticos: ");
                    ListView listView = (ListView) findViewById(R.id.list);
                    //traduccion del response a un arreglo
                    ArrayList<Diagnostico> diagnosticos = new ArrayList<Diagnostico>();
                    DiagnosticosAdapter.traducirJSON(responseBody,diagnosticos);
                    //Con arreglo se crea adapter, que genera la listView
                    final DiagnosticosAdapter adapter = new DiagnosticosAdapter(thisAct, diagnosticos);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fallo: ", error.getMessage());
                requestDiagnosticosHttp();
            }
        });
    }

    private void requestConsejosHttp()
    {
        String urlGetConsejos = LoginActivity.IP_Y_PUERTO+"/paciente/consejo/"+paciente.id ;
        AsyncHttpClient client = new AsyncHttpClient();
        final Activity thisAct = this;
        client.get(urlGetConsejos, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    String responseTxt = new String(responseBody);
                    tvDetalle.setText("consejos: ");
                    ListView listView = (ListView) findViewById(R.id.list);
                    //traduccion del response a un arreglo
                    ArrayList<Consejo> consejos = new ArrayList<Consejo>();
                    ConsejosAdapter.traducirJSON(responseBody,consejos);
                    //Con arreglo se crea adapter, que genera la listView
                    final ConsejosAdapter adapter = new ConsejosAdapter(thisAct, consejos);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fallo: ", error.getMessage());
                requestDiagnosticosHttp();
            }
        });
    }



    public void cerrarSesion(View view)
    {
        //Por si se quiere una forma de salir de sesion
//        Intent intent2 = new Intent(this,LoginActivity.class);
//        //based on item add info to intent
//        intent2.putExtra(LoginActivity.EXTRA_MESSAGE, LoginActivity.LOG_OUT);
//        startActivity(intent2);
    }



}
