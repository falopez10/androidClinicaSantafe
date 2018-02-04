package com.example.fabio.clinicasantafemedico;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fabio.clinicasantafemedico.adapters.ConsejosAdapter;
import com.example.fabio.clinicasantafemedico.adapters.DiagnosticosAdapter;
import com.example.fabio.clinicasantafemedico.adapters.ExamenesAdapter;
import com.example.fabio.clinicasantafemedico.adapters.RegistrosAdapter;
import com.example.fabio.clinicasantafemedico.adapters.TratamientosAdapter;
import com.example.fabio.clinicasantafemedico.models.Consejo;
import com.example.fabio.clinicasantafemedico.models.Diagnostico;
import com.example.fabio.clinicasantafemedico.models.Examen;
import com.example.fabio.clinicasantafemedico.models.Paciente;
import com.example.fabio.clinicasantafemedico.models.Registro;
import com.example.fabio.clinicasantafemedico.models.Tratamiento;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class DetallePacienteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Paciente paciente;
    private TextView tvDetalle;
    private ListView listView;
    EditText editAsunto;
    EditText editConsejo;
    Button btnEnviarConsejo;

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
        listView = (ListView) findViewById(R.id.list);
        editAsunto = (EditText)findViewById(R.id.editAsunto);
        editConsejo = (EditText)findViewById(R.id.editConsejo);
        btnEnviarConsejo = (Button)findViewById(R.id.btnEnviarConsejo);

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Vuelve a la normalidad
        vistaComun();

        if (id == R.id.nav_tratamientos) {
            requestTratamientosHttp();
        } else if (id == R.id.nav_examenes) {
            requestExamenesHttp();
        } else if (id == R.id.nav_diagnosticos) {
            requestDiagnosticosHttp();
        } else if (id == R.id.nav_registros) {
            requestRegistrosHttp();
            mostrarFormatoSeleccionFechasRegistros();
        } else if (id == R.id.nav_consejos) {
            requestConsejosHttp();
        }else if (id == R.id.nav_consejo) {
            desplegarFormatoEnvioConsejo();
        }else if (id == R.id.nav_recibir_notificaciones) {
            requestNotificacionesHttp();
        }else if (id == R.id.nav_config_marcapasos) {
            desplegarFormatoConfiguracionMarcapasos();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void vistaComun()
    {
        LinearLayout llRegistrosFecha = (LinearLayout) findViewById(R.id.llRegistrosFechas);
        llRegistrosFecha.setVisibility(View.GONE);
        editAsunto.setVisibility(View.GONE);
        editConsejo.setVisibility(View.GONE);
        btnEnviarConsejo.setVisibility(View.GONE);
        tvDetalle.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);

    }


    private void requestTratamientosHttp()
    {
        String urlGetTratamientos = MainActivity.IP_Y_PUERTO+"/paciente/tratamientoID/"+paciente.id;
        AsyncHttpClient client = new AsyncHttpClient();
        final Activity thisAct = this;
        client.get(urlGetTratamientos, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    tvDetalle.setText("tratamientos: ");
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

    private void mostrarFormatoSeleccionFechasRegistros()
    {
        LinearLayout llRegistrosFecha = (LinearLayout) findViewById(R.id.llRegistrosFechas);
        llRegistrosFecha.setVisibility(View.VISIBLE);
    }

    public void enviarPeticionRegistroRangoFechas(View view)
    {

    }

    private void requestRegistrosHttp()
    {
        String urlGetRegistros = MainActivity.IP_Y_PUERTO+"/paciente/registroID/"+paciente.id;
        AsyncHttpClient client = new AsyncHttpClient();
        final Activity thisAct = this;
        client.get(urlGetRegistros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    tvDetalle.setText("Registros (por fecha): ");
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
        String urlGetExamenes = MainActivity.IP_Y_PUERTO+"/paciente/examenID/"+paciente.id ;
        AsyncHttpClient client = new AsyncHttpClient();
        final Activity thisAct = this;
        client.get(urlGetExamenes, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    String responseTxt = new String(responseBody);
                    tvDetalle.setText("examenes: ");
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
        String urlGetDiagnosticos = MainActivity.IP_Y_PUERTO+"/paciente/diagnosticoID/"+paciente.id ;
        AsyncHttpClient client = new AsyncHttpClient();
        final Activity thisAct = this;
        client.get(urlGetDiagnosticos, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    String responseTxt = new String(responseBody);
                    tvDetalle.setText("diagnosticos: ");
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
        String urlGetConsejos = MainActivity.IP_Y_PUERTO+"/paciente/consejo/"+paciente.id ;
        AsyncHttpClient client = new AsyncHttpClient();
        final Activity thisAct = this;
        client.get(urlGetConsejos, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {
                    String responseTxt = new String(responseBody);
                    tvDetalle.setText("consejos: ");
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
    private void requestNotificacionesHttp()
    {

        tvDetalle.setText("notificaciones: ");
        //traduccion del response a un arreglo
        ArrayList<String> notificaciones = new ArrayList<String>();
        notificaciones.add("Frecuencia cardiaca: 57 Actividad fisica:2 Nivel Estres: 9 Presion sanguinea:149-83 Fecha: "+new Date()+"Color: ROJO");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, notificaciones);
        //Con arreglo se crea adapter, que genera la listView
        listView.setAdapter(adapter);
    }


    public void accionBoton(View view)
    {
        if(btnEnviarConsejo.getText().toString().equals("Configurar marcapasos"))
            enviarConfiguracionMarcapasos();
        else
            enviarConsejo();
    }

    private  void desplegarFormatoConfiguracionMarcapasos()
    {
        tvDetalle.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        EditText editAsunto = (EditText)findViewById(R.id.editAsunto);
        editAsunto.setHint("Frecuencia");
        editAsunto.setVisibility(View.VISIBLE);
        Button btnEnviarConsejo = (Button)findViewById(R.id.btnEnviarConsejo);
        btnEnviarConsejo.setVisibility(View.VISIBLE);
        btnEnviarConsejo.setText("Configurar marcapasos");
    }

    private void enviarConfiguracionMarcapasos()
    {
        EditText editAsunto = (EditText)findViewById(R.id.editAsunto);
        Button btnEnviarConsejo = (Button)findViewById(R.id.btnEnviarConsejo);


        Toast.makeText(this, "Configuracion enviada!", Toast.LENGTH_SHORT).show();
        tvDetalle.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        editAsunto.setVisibility(View.GONE);
        btnEnviarConsejo.setVisibility(View.GONE);
    }

    private void desplegarFormatoEnvioConsejo() {
        //Se hacen visibles solo los campos para enviar consejo
        tvDetalle.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        editAsunto.setVisibility(View.VISIBLE);
        editConsejo.setVisibility(View.VISIBLE);
        btnEnviarConsejo.setVisibility(View.VISIBLE);
        btnEnviarConsejo.setText("Enviar Consejo a Paciente");
    }

    public void enviarConsejo(){
        //Se envia y se encarga de regresar al estado original


        String asunto = editAsunto.getText().toString();
        String descripcion = editConsejo.getText().toString();

        requestPostConsejoHttp(asunto, descripcion);
        Toast.makeText(this, "Consejo enviado!", Toast.LENGTH_SHORT).show();
        tvDetalle.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
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
