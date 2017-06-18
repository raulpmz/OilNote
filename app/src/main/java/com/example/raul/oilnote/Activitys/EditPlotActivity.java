package com.example.raul.oilnote.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class EditPlotActivity extends AppCompatActivity {

    protected EditText name_plot, number_plant;
    protected ProgressDialog progressDialog;
    protected String cod, name, number;
    protected Connection connection;
    protected JSONObject jsonObject;
    protected Toolbar toolbar;
    protected Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plot);

        // Bundle:
        bundle          = getIntent().getExtras();

        // String:
        cod             = bundle.getString("cod");
        name            = bundle.getString("name");
        number          = bundle.getString("number_plant");

        // EditText:
        name_plot       = (EditText) findViewById(R.id.et_edit_plot);
        number_plant    = (EditText) findViewById(R.id.et_edit_number_plant);

        // Toolbar:
        toolbar         = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            toolbar.setTitle(R.string.name);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Cargar los datos de la parcela seleccionada:
        name_plot.setText(name);
        number_plant.setText(number);

        // Conexion:
        connection = new Connection();

        // Objetos JSON:
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
    public void buttonEditPlot(View v) {
        name = name_plot.getText().toString();
        number = number_plant.getText().toString();

        if (checkData()) {
            new EditPlotTask().execute();
            Log.e("checkData", "entra");
        }
    }

    // Hilo encargado de registrar al usuario:
    class EditPlotTask extends AsyncTask<String, String, JSONObject> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            onProgressDialog(EditPlotActivity.this,getResources().getString(R.string.save));
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                parametrosPost.put("ins_sql",   "UPDATE plots " +
                                                "SET plot_name = '"+ name +"', plot_number = '"+ number +"' " +
                                                "WHERE user_cod = "+ USER_COD +" " +
                                                "AND plot_cod = "+ cod +";");
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
                        Toast.makeText(EditPlotActivity.this, getResources().getString(R.string.successful_edit_plot), Toast.LENGTH_SHORT).show();

                        Intent i = getIntent();

                        i.putExtra("name", name);
                        i.putExtra("number", number);

                        setResult(RESULT_OK, i);

                        finish();
                    }else{
                        Snackbar.make(findViewById(R.id.LinearEditPlotActivity), getResources().getString(R.string.error_edit_plot), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.LinearEditPlotActivity), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
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
