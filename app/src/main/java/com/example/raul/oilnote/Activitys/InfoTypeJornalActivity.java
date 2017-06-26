package com.example.raul.oilnote.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.raul.oilnote.Adapters.ListTypeJornalAdapter;
import com.example.raul.oilnote.Adapters.ListWorkerAdapter;
import com.example.raul.oilnote.Objects.TypeJornal;
import com.example.raul.oilnote.Objects.Worker;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class InfoTypeJornalActivity extends AppCompatActivity {

    protected ListTypeJornalAdapter listTypeJornalAdapter;
    protected AlertDialog.Builder alert2, alert;
    protected List<TypeJornal> listTypeJornals;
    protected ProgressDialog progressDialog;
    protected ListView listViewTypeJonal;
    protected Connection connection;
    protected JSONObject jsonObject;
    protected JSONArray jSONArray;
    protected Toolbar toolbar;
    protected Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_type_jornal);

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

        // Dialogos para los mensajes de información:
        alert                   = new AlertDialog.Builder(this);
        alert2                  = new AlertDialog.Builder(this);

        // Clase Conexión:
        connection      = new Connection();

        // Objetos JSON:
        jSONArray       = new JSONArray();
        jsonObject      = new JSONObject();

        new ListTypeJornalTask().execute();

    }

    class ListTypeJornalTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(InfoTypeJornalActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            // Consulto los trabajadores que tiene el usuario:
            try {

                parametrosPost.put("ins_sql",   "SELECT * " +
                                                "FROM type_jornal " +
                                                "WHERE user_cod = '" + USER_COD + "' ");


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
                    listTypeJornalAdapter = new ListTypeJornalAdapter(InfoTypeJornalActivity.this, mapTypeJornalList(jsonArray));
                    // Relacionando la lista con el adaptador:
                    listViewTypeJonal.setAdapter(listTypeJornalAdapter);

                    onClickList();

                }else{
                    // Poner una lista avisando de que no tiene jornales personalizados:
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Hilo para borrar la parcela:
    class RemoveTask extends AsyncTask<Void,Void,JSONObject> {

        private String cod;
        private HashMap<String, String> parametrosPost = new HashMap<>();

        public RemoveTask(String cod) {
            this.cod = cod;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(InfoTypeJornalActivity.this, getResources().getString(R.string.save));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                parametrosPost.put("ins_sql",   "DELETE FROM type_jornal " +
                                                "WHERE type = "+ cod);
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
                        Toast.makeText(InfoTypeJornalActivity.this, getResources().getString(R.string.successful_remove_type), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Snackbar.make(findViewById(R.id.LinearTypeJornal), getResources().getString(R.string.error_remove_type), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.LinearTypeJornal), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
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

    // Evento al seleccionar un elemento de la lista:
    public void onClickList() {
        listViewTypeJonal.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final CharSequence[] items = new CharSequence[1];

                items[0] = getResources().getString(R.string.add_remove);

                alert.setTitle(getResources().getString(R.string.options))
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                if(pos == 0){
                                    dialog.dismiss();
                                    alert2.setTitle(R.string.attention);
                                    alert2.setMessage(R.string.are_sure_jornal);
                                    alert2.setPositiveButton(R.string.add_remove, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new RemoveTask(listTypeJornals.get(i).getId()).execute();
                                        }
                                    });
                                    alert2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alert2.show();
                                }
                            }
                        });
                alert.setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface alert, int which) {
                                // TODO Auto-generated method stub
                                alert.dismiss();
                            }
                        });
                alert.show();
                return false;
            }
        });
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
