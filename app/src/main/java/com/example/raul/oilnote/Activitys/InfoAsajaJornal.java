package com.example.raul.oilnote.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.example.raul.oilnote.Adapters.ListTypeJornalAdapter;
import com.example.raul.oilnote.Objects.TypeJornal;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Connection;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class InfoAsajaJornal extends AppCompatActivity {

    protected ListTypeJornalAdapter listTypeJornalAdapter;
    protected List<TypeJornal> listTypeJornals;
    protected ProgressDialog progressDialog;
    protected ListView listViewTypeJonal;
    protected Connection connection;
    protected JSONArray jSONArray;
    protected Toolbar toolbar;
    protected Bundle bundle;
    protected int cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_asaja_jornal);

        // Bundle:
        bundle          = getIntent().getExtras();

        // ListView:
        listViewTypeJonal = (ListView) findViewById(R.id.listview_type_jornal);

        // List:
        listTypeJornals = new ArrayList<>();

        // Toolbar:
        toolbar         = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            toolbar.setTitle(R.string.name);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Clase Conexión:
        connection      = new Connection();

        // Objetos JSON:
        jSONArray       = new JSONArray();

        // String:
        cod             = bundle.getInt("cod");

        new ListTypeJornalTask().execute();
    }

    class ListTypeJornalTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(InfoAsajaJornal.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            // Consulto los trabajadores que tiene el usuario:
            try {

                parametrosPost.put("ins_sql",   "SELECT * " +
                                                "FROM asaja " +
                                                "WHERE type = '"+ cod  +"'");


                Log.e("parametrosPost",""+parametrosPost);
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
                    // Inicializo el adaptador:
                    listTypeJornalAdapter = new ListTypeJornalAdapter(InfoAsajaJornal.this, mapTypeJornalList(jsonArray));
                    // Relacionando la lista con el adaptador:
                    listViewTypeJonal.setAdapter(listTypeJornalAdapter);

                }else{
                    // Poner una lista avisando de que no tiene jornales personalizados:
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // Mapeo los datos del JSONArray que recibo para montar el adaptador:
    public List<TypeJornal> mapTypeJornalList(JSONArray jsonArray) throws JSONException {

        List<TypeJornal> lw = new ArrayList<>();

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                TypeJornal typeJornal = new TypeJornal();

                typeJornal.setId(jsonArray.getJSONObject(i).getString("type"));
                typeJornal.setName(jsonArray.getJSONObject(i).getString("type_name"));
                typeJornal.setMoney(jsonArray.getJSONObject(i).getString("type_value"));

                lw.add(typeJornal);
            }
            listTypeJornals = lw;
        }

        return lw;
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
        finish();
        return false;
    }
}
