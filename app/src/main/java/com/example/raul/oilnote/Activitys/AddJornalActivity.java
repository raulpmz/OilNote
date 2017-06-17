package com.example.raul.oilnote.Activitys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.raul.oilnote.Objects.Worker;
import com.example.raul.oilnote.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class AddJornalActivity extends BaseActivity {

    protected List<Worker> listWorkers, listJW, lw;
    protected RadioButton rb_assist, rb_miss;
    protected SimpleDateFormat ss1, ss2;
    protected CalendarView calendar;
    protected TextView tv_date;
    protected Spinner spinner;
    protected String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_add_jornal);

        // CalendarView:
        calendar        = (CalendarView) findViewById(R.id.calendar_add_jornal);

        // RadioButton's:
        rb_assist       = (RadioButton) findViewById(R.id.rb_add_jornal_asist);
        rb_miss         = (RadioButton) findViewById(R.id.rb_add_jornal_miss);

        // TextView:
        tv_date         = (TextView) findViewById(R.id.tv_day);

        // Spinner:
        spinner         = (Spinner) findViewById(R.id.sp_add_jornal);

        // ArrayList's:
        listWorkers     = new ArrayList<>();
        listJW          = new ArrayList<>();

        // SimpleDateFormat:
        ss1             = new SimpleDateFormat("dd-MM-yyyy");
        ss2             = new SimpleDateFormat("yyyy-MM-dd");

        // Seteamos el TextView con la fecha de hoy:
        tv_date.setText(ss1.format(calendar.getDate()));

        // Guardamos en una variable tipo string la fecha para realizar la consulta sql:
        date = ss2.format(calendar.getDate());

        // Lanzamos el hilo para rellenar los datos con los trabajadores disponibles de ese dia:
        new WorkersTask().execute();

        // Evento al cabiar el dia seleccionado:
        getCalendarOnDateChangeListener();
    }

    public void getCalendarOnDateChangeListener(){
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                int mes = i1+1;
                tv_date.setText(i2 + "-" + mes + "-" + i);
                date = i + "-" + mes + "-" + i2;
                new JornalWorkersTask().execute();
            }
        });
    }

    // Hilo para obtener los trabajadores:
    class WorkersTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(AddJornalActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                // Consulto los trabajadores que tiene el usuario:
                parametrosPost.put("ins_sql", "SELECT * FROM workers WHERE user_cod = '" + USER_COD + "' ORDER BY worker_name");
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
                    listWorkers = mapWorkersList(jsonArray);
                    new JornalWorkersTask().execute();

                }else{
                    // Poner mensaje avisando de que no tiene trabajadores.
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Mapeo los trabajadores:
    public List<Worker> mapWorkersList(JSONArray jsonArray) throws JSONException {

        List<Worker> lw = new ArrayList<>();

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                Worker worker = new Worker();

                worker.setWorkerCod(jsonArray.getJSONObject(i).getString("worker_cod"));
                worker.setWorkerName(jsonArray.getJSONObject(i).getString("worker_name"));

                lw.add(worker);
            }
        }
        return lw;
    }
    // Mapeo los trabajadores:
    public void mapListJW(JSONArray jsonArray) throws JSONException {

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                Worker worker = new Worker();

                worker.setWorkerCod(jsonArray.getJSONObject(i).getString("worker_cod"));
                worker.setWorkerName(jsonArray.getJSONObject(i).getString("worker_name"));

                listJW.add(worker);
            }
        }
    }


    // Hilo para obtener los trabajadores que tienen ese dia anotado ya:
    class JornalWorkersTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(AddJornalActivity.this,getString(R.string.loading));
            listJW.clear();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            try {
                // Consulto los trabajadores que tiene el usuario:
                parametrosPost.put("ins_sql",   "SELECT a.worker_cod, b.worker_name " +
                                                "FROM jornals a " +
                                                "INNER JOIN workers b " +
                                                "ON a.worker_cod = b.worker_cod " +
                                                "WHERE jornal_date = '"+ date +"' " +
                                                "AND a.user_cod = '" + USER_COD + "' ORDER BY worker_name");
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
                    mapListJW(jsonArray);
                    new MissingWorkersTask().execute();
                }else{
                    // Poner mensaje avisando de que no tiene trabajadores.
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Hilo para obtener los trabajadores que tienen ese dia anotado ya:
    class MissingWorkersTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(AddJornalActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            try {
                // Consulto los trabajadores que tiene el usuario:
                parametrosPost.put("ins_sql",   "SELECT a.worker_cod, b.worker_name " +
                                                "FROM missing a " +
                                                "INNER JOIN workers b " +
                                                "ON a.worker_cod = b.worker_cod " +
                                                "WHERE missing_date = '"+ date +"' " +
                                                "AND a.user_cod = '" + USER_COD + "' " +
                                                "ORDER BY worker_name");
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
                    mapListJW(jsonArray);
                    fillSpinner();
                }else{
                    // Poner mensaje avisando de que no tiene trabajadores.
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Metodo para rellenar el spiner con los trabajadores disponebles para apuntar el jornal
    public void fillSpinner(){
        List<String> lw2 = new ArrayList<>();
        lw = new ArrayList<>();
        boolean contains;

        // Limpio el array de los trabajadores que ya tienen ese dia anotado:
        for(int i = 0; i < listWorkers.size(); i++){
            contains = false;
            for(int j = 0; j < listJW.size(); j++){
                if(listWorkers.get(i).getWorkerCod().equals(listJW.get(j).getWorkerCod())){
                    contains = true;
                }
            }
            if(!contains){
                lw.add(listWorkers.get(i));
            }
        }
        // Lleno una lista con los nombres de los trabajadores disponibles para llenar el spinner:
        for(int i = 0; i < lw.size(); i++){
            lw2.add(lw.get(i).getWorkerName());
        }

        //Creamos el adaptador:
        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lw2);

        //Añadimos el layout para el menú y rellenamos el spinner:
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

    }
}
