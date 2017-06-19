package com.example.raul.oilnote.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.oilnote.Adapters.ListJornalAdapter;
import com.example.raul.oilnote.Objects.Jornal;
import com.example.raul.oilnote.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class ListJornalsActivity extends BaseActivity {

    protected ListJornalAdapter listJornalAdapter;
    protected AlertDialog.Builder alert2;
    protected ListView listViewJornals;
    protected List<Jornal> listJornals;
    protected TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_list_jornals);

        // ListView:
        listViewJornals = (ListView) findViewById(R.id.list_jornal);

        // List:
        listJornals     = new ArrayList<>();

        // TextView:
        total           = (TextView) findViewById(R.id.total_jornal);

        // Hilo para mostar la lista de jornales
        new ListJornalsTask().execute();

        // Dialogos para los mensajes de información:
        alert           = new AlertDialog.Builder(this);
        alert2          = new AlertDialog.Builder(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_add_jornal).setVisible(true);
        menu.findItem(R.id.action_add_jornal).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case R.id.action_add_jornal:
                startActivity(new Intent(ListJornalsActivity.this,AddJornalActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    // Evento al seleccionar un elemento de la lista:
    public void onClickList(){
        /*listViewPlots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ListPlotsActivity.this,InfoPlotActivity.class);

                intent.putExtra("cod",listPlots.get(i).getCod());
                intent.putExtra("name",listPlots.get(i).getName());
                intent.putExtra("number_plant",listPlots.get(i).getNumber_plant());

                startActivity(intent);
            }
        });*/

        listViewJornals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final CharSequence[] items = new CharSequence[1];

                //items[0] = getResources().getString(R.string.edit);
                items[0] = getResources().getString(R.string.add_remove);

                alert.setTitle(getResources().getString(R.string.options))
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {

                                // Si la opción es editar:
                                /*if(pos == 0){

                                    Intent intent = new Intent(ListPlotsActivity.this,InfoPlotActivity.class);

                                    intent.putExtra("cod",listJornals.get(i).getJornal_cod());
                                    intent.putExtra("name",listJornals.get(i).get());
                                    intent.putExtra("number_plant",listJornals.get(i).getNumber_plant());

                                    startActivity(intent);
                                }*/
                                // Si la opción es eliminar:
                                if(pos == 0){
                                    dialog.dismiss();
                                    alert2.setTitle(R.string.attention);
                                    alert2.setMessage(R.string.are_sure_plant);
                                    alert2.setPositiveButton(R.string.add_remove, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new RemoveJornalTask(listJornals.get(i).getJornal_cod()).execute();
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

    class ListJornalsTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListJornalsActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                // Consulto los trabajadores que tiene el usuario:
                parametrosPost.put("ins_sql",   "SELECT jornal_cod ,DATE_FORMAT(jornal_date, '%d-%m-%Y'), worker_name " +
                                                "FROM jornals  " +
                                                "WHERE user_cod = '" + USER_COD + "' " +
                                                "ORDER BY jornal_date DESC");

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
                    listJornalAdapter = new ListJornalAdapter(ListJornalsActivity.this, mapJornalsList(jsonArray));
                    // Relacionando la lista con el adaptador:
                    listViewJornals.setAdapter(listJornalAdapter);

                    total.setText(""+listJornals.size());

                    onClickList();

                }else{
                    // Poner una lista avisando de que no tiene jornales:
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Mapeo los dato del JSONArray que recivo en una lista de trabajadores para montar el adaptador:
    public List<Jornal> mapJornalsList(JSONArray jsonArray) throws JSONException {

        List<Jornal> lj = new ArrayList<>();

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                Jornal jornal = new Jornal();

                jornal.setJornal_cod(jsonArray.getJSONObject(i).getString("jornal_cod"));
                jornal.setWorker_name(jsonArray.getJSONObject(i).getString("worker_name"));
                jornal.setJornal_date(jsonArray.getJSONObject(i).getString("DATE_FORMAT(jornal_date, '%d-%m-%Y')"));

                lj.add(jornal);
            }
            listJornals = lj;
        }

        return lj;
    }

    // Hilo para borrar el jornal:
    class RemoveJornalTask extends AsyncTask<Void,Void,JSONObject> {

        private String cod;
        private HashMap<String, String> parametrosPost = new HashMap<>();

        public RemoveJornalTask(String cod) {
            this.cod = cod;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListJornalsActivity.this, getResources().getString(R.string.remove));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                parametrosPost.put("ins_sql",   "DELETE FROM jornals " +
                                                "WHERE user_cod = "+ USER_COD +" " +
                                                "AND jornal_cod = "+ cod);
                jsonObject = connection.sendWrite(BASE_URL_WRITE, parametrosPost);

                if (jsonObject != null) {
                    return jsonObject;
                }

            } catch (JSONException e) {
                Snackbar.make(findViewById(R.id.ListLayout), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
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
                        Snackbar.make(findViewById(R.id.ListLayout), getResources().getString(R.string.successful_remove_jornal), Toast.LENGTH_SHORT).show();
                        new ListJornalsTask().execute();
                    }else{
                        Snackbar.make(findViewById(R.id.ListLayout), getResources().getString(R.string.error_remove_jornal), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.ListLayout), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new ListJornalsTask().execute();
    }
}
