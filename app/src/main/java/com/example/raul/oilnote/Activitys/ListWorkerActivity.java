package com.example.raul.oilnote.Activitys;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.raul.oilnote.Adapters.ListWorkerAdapter;
import com.example.raul.oilnote.Objects.Worker;
import com.example.raul.oilnote.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.GlobalVars.*;

public class ListWorkerActivity extends BaseActivity {

    protected ListWorkerAdapter listWorkerAdapter;
    protected Boolean controlFilter, b_name;
    protected LinearLayout linearFilterName;
    protected AlertDialog.Builder alert2;
    protected ListView listViewWorkers;
    protected List<Worker> listWorkers;
    protected EditText et_name;
    protected String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_list_worker);

        // ListView:
        listViewWorkers         = (ListView) findViewById(R.id.list_worker);

        // List trabajadores:
        listWorkers             = new ArrayList<>();

        // Dialogos para los mensajes de información:
        alert                   = new AlertDialog.Builder(this);
        alert2                  = new AlertDialog.Builder(this);

        // EditText:
        et_name                 = (EditText) findViewById(R.id.et_filter_name);

        // LinearLayout:
        linearFilterName        = (LinearLayout) findViewById(R.id.LinearFilterName);

        // Boleano  control de filtros:
        controlFilter           = false;
        b_name                  = false;

        // Evento para recoger los caracteres del EditText:
        onKeyListener();

        //Hilo para obtener los trabajadores:
        new ListWorkersTask(name).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_add_worker).setVisible(true);
        menu.findItem(R.id.action_add_worker).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_filter).setVisible(true);
        menu.findItem(R.id.action_filter).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            // En el caso de elegir la opción para añadir trabajador:
            case R.id.action_add_worker:
                startActivity(new Intent(ListWorkerActivity.this,AddWorkerActivity.class));
                break;
            // En el caso de elegir la opción de filtros:
            case R.id.action_filter:
                actionFilters();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    // Evento al seleccionar un elemento de la lista:
    public void onClickList(){
        listViewWorkers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ListWorkerActivity.this,InfoWorkerActivity.class);

                intent.putExtra("cod",listWorkers.get(i).getWorkerCod());
                intent.putExtra("name",listWorkers.get(i).getWorkerName());
                intent.putExtra("phone",listWorkers.get(i).getWorkerPhone());
                intent.putExtra("photo",listWorkers.get(i).getWorkerPhoto());

                startActivity(intent);
            }
        });

        listViewWorkers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final CharSequence[] items = new CharSequence[2];

                items[0] = getResources().getString(R.string.edit);
                items[1] = getResources().getString(R.string.add_remove);

                alert.setTitle(getResources().getString(R.string.options))
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {

                                // Si la opción es editar:
                                if(pos == 0){

                                    Intent intent = new Intent(ListWorkerActivity.this,InfoWorkerActivity.class);

                                    intent.putExtra("cod",listWorkers.get(i).getWorkerCod());
                                    intent.putExtra("name",listWorkers.get(i).getWorkerName());
                                    intent.putExtra("phone",listWorkers.get(i).getWorkerPhone());
                                    intent.putExtra("photo",listWorkers.get(i).getWorkerPhoto());

                                    startActivity(intent);
                                }
                                // Si la opción es la eliminar:
                                if(pos == 1){
                                    dialog.dismiss();
                                    alert2.setTitle(R.string.attention);
                                    alert2.setMessage(R.string.are_sure_worker);
                                    alert2.setPositiveButton(R.string.add_remove, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new RemoveWorkerTask(listWorkers.get(i).getWorkerCod()).execute();
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

    class ListWorkersTask extends AsyncTask<Void, Void, JSONArray>{

        private HashMap<String, String> parametrosPost = new HashMap<>();
        private String name;

        public ListWorkersTask(String name) {
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListWorkerActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            // Consulto los trabajadores que tiene el usuario:
            try {
                if(b_name){
                    parametrosPost.put("ins_sql",   "SELECT * " +
                                                    "FROM workers " +
                                                    "WHERE user_cod = '" + USER_COD + "' " +
                                                    "AND worker_name " +
                                                    "LIKE '%"+ name +"%' "+
                                                    "ORDER BY worker_name");
                    b_name = false;
                }else{
                    parametrosPost.put("ins_sql",   "SELECT * " +
                                                    "FROM workers " +
                                                    "WHERE user_cod = '" + USER_COD + "' " +
                                                    "ORDER BY worker_name");
                }
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
                    System.out.println("Foto encode: "+mapWorkersList(jsonArray).get(0).getWorkerPhoto());
                    // Inicializo el adaptador:
                    listWorkerAdapter = new ListWorkerAdapter(ListWorkerActivity.this, mapWorkersList(jsonArray));
                    // Relacionando la lista con el adaptador:
                    listViewWorkers.setAdapter(listWorkerAdapter);

                    onClickList();

                }else{
                    // Poner una lista avisando de que no tiene trabajadores:
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Mapeo los dato del JSONArray que recivo en una lista de trabajadores para montar el adaptador:
    public List<Worker> mapWorkersList(JSONArray jsonArray) throws JSONException {

        List<Worker> lw = new ArrayList<>();

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                Worker worker = new Worker();

                worker.setWorkerCod(jsonArray.getJSONObject(i).getString("worker_cod"));
                worker.setWorkerName(jsonArray.getJSONObject(i).getString("worker_name"));
                worker.setWorkerPhone(jsonArray.getJSONObject(i).getString("worker_phone"));
                worker.setWorkerPhoto(jsonArray.getJSONObject(i).getString("worker_photo"));

                lw.add(worker);
            }
            listWorkers = lw;
        }

        return lw;
    }

    // Hilo para borrar el trabajador:
    class RemoveWorkerTask extends AsyncTask<Void,Void,JSONObject> {

        private String cod;
        private HashMap<String, String> parametrosPost = new HashMap<>();

        public RemoveWorkerTask(String cod) {
            this.cod = cod;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListWorkerActivity.this, getResources().getString(R.string.save));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                parametrosPost.put("ins_sql",   "DELETE FROM workers " +
                        "WHERE user_cod = "+ USER_COD +" " +
                        "AND worker_cod = "+ cod);
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
                        Snackbar.make(findViewById(R.id.ListLayout), getResources().getString(R.string.successful_remove_worker), Toast.LENGTH_SHORT).show();
                        new ListWorkersTask(name).execute();
                    }else{
                        Snackbar.make(findViewById(R.id.ListLayout), getResources().getString(R.string.error_remove_worker), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.ListLayout), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *      Filtros:
     */

    public void actionFilters(){
        if (controlFilter){
            if(linearFilterName.getVisibility() == View.VISIBLE){
                linearFilterName.setVisibility(View.GONE);
                controlFilter = false;
                et_name.setText(null);
                b_name = false;
            }
        }else{
            if(linearFilterName.getVisibility() == View.GONE){
                linearFilterName.setVisibility(View.VISIBLE);
                controlFilter = true;
                et_name.setText(null);
                b_name = false;
            }
        }
    }

    public void onKeyListener(){
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Obtengo los caracteres del EditTect:
                name = et_name.getText().toString();
                b_name = true;
                // Ejecutar el hilo para obtener el filtrado por nombre:
                new ListWorkersTask(name).execute();

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        b_name = false;
        new ListWorkersTask(name).execute();
    }

}
