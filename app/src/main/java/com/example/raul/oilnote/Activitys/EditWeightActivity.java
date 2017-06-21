package com.example.raul.oilnote.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.oilnote.Objects.Plot;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class EditWeightActivity extends AppCompatActivity {

    protected String cod, date, date2, name, number;
    protected ProgressDialog progressDialog;
    protected EditText et_nweight;
    protected SimpleDateFormat ss1, ss2;
    protected JSONObject jsonObject;
    protected Connection connection;
    protected JSONArray jSONArray;
    protected CalendarView calendar;
    protected List<Plot> listPlots;
    protected TextView tv_date;
    protected Toolbar toolbar;
    protected Spinner spinner;
    protected Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_weight);

        // Bundle:
        bundle          = getIntent().getExtras();

        // String:
        cod             = bundle.getString("cod");
        date            = bundle.getString("date");
        name            = bundle.getString("type_expense");
        number          = bundle.getString("number");

        // CalendarView:
        calendar        = (CalendarView) findViewById(R.id.calendar_edit_weight);

        // TextView:
        tv_date         = (TextView) findViewById(R.id.tv_day);

        // Spinner:
        spinner         = (Spinner) findViewById(R.id.sp_edit_weight);

        // List:
        listPlots       = new ArrayList<>();

        // EditText:
        et_nweight      = (EditText) findViewById(R.id.et_edit_number_weight);

        // Toolbar:
        toolbar         = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            toolbar.setTitle(R.string.name);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // SimpleDateFormat:
        ss1             = new SimpleDateFormat("dd-MM-yyyy");
        ss2             = new SimpleDateFormat("yyyy-MM-dd");

        // Seteamos el TextView con la fecha de hoy:
        tv_date.setText(ss1.format(calendar.getDate()));

        // Guardamos en una variable tipo string la fecha para realizar la consulta sql:
        date            = ss2.format(calendar.getDate());
        date2           = ss1.format(calendar.getDate());

        // Clase Conexión:
        connection      = new Connection();

        // Objetos JSON:
        jSONArray       = new JSONArray();
        jsonObject      = new JSONObject();

        // Relleno el EditText con la información que le paso de la actividad anterior:
        et_nweight.setText(number);

        // Evento al cabiar el día seleccionado:
        getCalendarOnDateChangeListener();

        // Ejecuto el hilo para llenar el spinner con las parcelas:
        new ListPlotsTask().execute();
    }

    // Controlamos el evento que se genera cada vez que cambiamos la fecha del CalendarView:
    public void getCalendarOnDateChangeListener(){
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                int mes = i1+1;
                date2 = i2 + "-" + mes + "-" + i;
                tv_date.setText(date2);
                date = i + "-" + mes + "-" + i2;
            }
        });
    }

    // Botón para añadir el pesaje:
    public void editWeight(View w){
        // Guardo los valores en las variables:
        name    = spinner.getSelectedItem().toString();
        number  = et_nweight.getText().toString();

        // Compruebo que el EditText del peso no esta vacío:
        if(et_nweight.length() > 0){

            // Ejecutamos el hilo para registrar el peso en la base de datos:
            new EditWeightTask().execute();

            // De estar vacío mostramos un mensaje de error al usuario:
        }else{
            et_nweight.setError(getString(R.string.emptry_camp));
            et_nweight.requestFocus();
        }
    }

    // Metodo para rellenar el spiner con las parcelas:
    public void fillSpinner(){
        List<String> lw = new ArrayList<>();

        // Lleno una lista con los nombres de las parcelas para llenar el spinner:
        for(int i = 0; i < listPlots.size(); i++){
            lw.add(listPlots.get(i).getName());
        }

        //Creamos el adaptador:
        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lw);

        //Añadimos el layout para el menú y rellenamos el spinner:
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
    }

    // Mapeo los dato del JSONArray que recivo en una lista de trabajadores para montar el adaptador:
    public void mapPlotsList(JSONArray jsonArray) throws JSONException {

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                Plot plot = new Plot();

                plot.setCod(jsonArray.getJSONObject(i).getString("plot_cod"));
                plot.setName(jsonArray.getJSONObject(i).getString("plot_name"));
                plot.setNumber_plant(jsonArray.getJSONObject(i).getString("plot_number"));

                listPlots.add(plot);
            }
        }
    }

    // Hilo para obtener los datos de las parcelas:
    class ListPlotsTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(EditWeightActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                // Consulto los trabajadores que tiene el usuario:
                parametrosPost.put("ins_sql",   "SELECT * " +
                                                "FROM plots " +
                                                "WHERE user_cod = '" + USER_COD + "'");
                jSONArray = connection.sendRequest(BASE_URL_READ, parametrosPost);

                if (jSONArray != null) {
                    return jSONArray;
                }
            } catch (Exception e) {
                e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);

            onStopProgressDialog();

            try {
                if(jsonArray != null){
                    // Llenamos el array con las parcelas:
                    mapPlotsList(jsonArray);

                    // Llenamos el spinner con el nombre de las parcelas:
                    fillSpinner();

                }else{
                    // Poner una vista avisando de que no tiene parcelas:
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Hilo para editar el pesaje de la base de datos:
    class EditWeightTask extends AsyncTask<Void,Void,JSONObject>{

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(EditWeightActivity.this, getResources().getString(R.string.save));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                parametrosPost.put("ins_sql",   "UPDATE weights " +
                                                "SET plot_name = '"+ name +"', weight_date = '"+ date +"', weight_number = '"+ number +"' " +
                                                "WHERE user_cod = "+ USER_COD +" " +
                                                "AND weight_cod = "+ cod +";");
                jsonObject = connection.sendWrite(BASE_URL_WRITE, parametrosPost);


                if (jsonObject != null) {
                    return jsonObject;
                }

            } catch (JSONException e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            onStopProgressDialog();

            if (jsonObject != null) {
                try {
                    if(jsonObject.getInt("added") == 1){
                        Toast.makeText(EditWeightActivity.this, getResources().getString(R.string.successful_edit_weight), Toast.LENGTH_SHORT).show();

                        Intent i = getIntent();

                        i.putExtra("date",date2);
                        i.putExtra("type_expense",name);
                        i.putExtra("number",number);

                        setResult(RESULT_OK, i);

                        finish();
                    }else{
                        Snackbar.make(findViewById(R.id.LinearEditWeight), getResources().getString(R.string.error_edit_weight), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                et_nweight.setText(null);
            }else{
                Snackbar.make(findViewById(R.id.LinearEditWeight), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onProgressDialog(Context context, String msg){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();
    }

    // Método con el que paramos el ProgressDialog:
    public void onStopProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return false;
    }
}
