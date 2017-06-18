package com.example.raul.oilnote.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class AddPlotActivity extends AppCompatActivity {

    protected EditText name_plot, number_plant;
    protected ProgressDialog progressDialog;
    protected Connection connection;
    protected JSONObject jsonObject;
    protected String name, number;
    protected JSONArray jSONArray;
    protected boolean plot_exist;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plot);

        // EditText's:
        name_plot       = (EditText) findViewById(R.id.et_add_plot);
        number_plant    = (EditText) findViewById(R.id.et_add_number_plant);

        // Boolean:
        plot_exist = false;

        // Toolbar:
        toolbar         = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            toolbar.setTitle(R.string.name);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Conexion:
        connection = new Connection();

        // Objetos JSON:
        jSONArray       = new JSONArray();
        jsonObject      = new JSONObject();
    }

    public boolean checkData(){
        if(name_plot.length() <= 2){
            name_plot.setError(getResources().getString(R.string.name_small));
            name_plot.requestFocus();
            return false;
        }

        // Si el EditText Correo está vacio:
        if(number_plant.getText().toString().isEmpty()){
            number_plant.setError(getString(R.string.emptry_camp));
            number_plant.requestFocus();
            return false;
        }

        return true;
    }

    // Botón para añadir parcela:
    public void buttonAddPlot(View v) {
        name = name_plot.getText().toString();
        number = number_plant.getText().toString();

        if (checkData()) {
            new CheckPlotTask().execute();
            Log.e("checkData", "entra");
        }
    }

    // Hilo encargado de registrar al usuario:
    class CheckPlotTask extends AsyncTask<String, String, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            onProgressDialog(AddPlotActivity.this,getResources().getString(R.string.checking));
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            try {
                // Consulto que el usuario no existe en la base de datos:
                parametrosPost.put("ins_sql",   "SELECT * " +
                        "FROM plots " +
                        "WHERE plot_name = '" + name + "'");
                Log.e("parametrosPost",""+parametrosPost);
                jSONArray = connection.sendRequest(BASE_URL_READ, parametrosPost);

                if (jSONArray != null && jSONArray.length() > 0 ) {
                    Log.e("jSONArray","true");
                    plot_exist  = true;
                    return jSONArray;
                }else {
                    Log.e("jSONArray","false");
                }

            } catch (Exception e) {
                e.getMessage();
            }

            return null;
        }
        protected void onPostExecute(JSONArray json) {
            onStopProgressDialog();
            Log.e("onPostExecute","entra");
            // Si la parcela existe lanzo un mensaje de aviso:
            if (plot_exist) {
                name_plot.setError(getString(R.string.plot_exist));
                name_plot.requestFocus();

                // Si no hay ninguna parcela con nombre lanzo el hilo para registrarlo:
            }else {
                new AddPlotTask().execute();
            }
        }
    }

    // Hilo encargado de registrar al usuario:
    class AddPlotTask extends AsyncTask<String, String, JSONObject> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            onProgressDialog(AddPlotActivity.this,getResources().getString(R.string.save));
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                parametrosPost.put("ins_sql",   "INSERT INTO plots (user_cod, plot_name, plot_number) " +
                                                "VALUES('" + USER_COD + "','" + name + "','" + number + "');");
                jsonObject = connection.sendWrite(BASE_URL_WRITE, parametrosPost);

                if (jsonObject != null) {
                    return jsonObject;
                }

            } catch (JSONException e) {
                e.getMessage();
            }

            return null;
        }
        protected void onPostExecute(JSONObject json) {
            onStopProgressDialog();

            if (json != null) {
                try {
                    if(json.getInt("added") == 1){
                        Toast.makeText(AddPlotActivity.this, getResources().getString(R.string.successful_add_plot), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Snackbar.make(findViewById(R.id.LinearAddPlotActivity), getResources().getString(R.string.error_add_plot), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.LinearAddPlotActivity), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
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
        finish();
        return false;
    }
}
