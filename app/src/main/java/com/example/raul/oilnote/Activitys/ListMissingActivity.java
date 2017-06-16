package com.example.raul.oilnote.Activitys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.raul.oilnote.Adapters.ListJornalAdapter;
import com.example.raul.oilnote.Adapters.ListMissingAdapter;
import com.example.raul.oilnote.Objects.Jornal;
import com.example.raul.oilnote.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class ListMissingActivity extends BaseActivity {

    protected ListView listViewMissing;
    protected ListJornalAdapter listMissingAdapter;
    protected List<Jornal> listMissings;
    protected TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_list_missing);

        // ListView:
        listViewMissing = (ListView) findViewById(R.id.list_view_missing);

        // List:
        listMissings = new ArrayList<>();

        // TextView:
        total           = (TextView) findViewById(R.id.total_jornal);

        new ListMissingsTask().execute();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_add_jornal).setVisible(true);
        menu.findItem(R.id.action_add_jornal).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }*/

    class ListMissingsTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListMissingActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                // Consulto los trabajadores que tiene el usuario:
                parametrosPost.put("ins_sql",   "SELECT missing_cod ,DATE_FORMAT(missing_date, '%d-%m-%Y'), worker_name " +
                        "FROM missing a " +
                        "INNER JOIN workers b " +
                        "ON a.worker_cod = b.worker_cod " +
                        "WHERE a.user_cod = '" + USER_COD + "' " +
                        "ORDER BY missing_date DESC");

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
                    listMissingAdapter = new ListJornalAdapter(ListMissingActivity.this, mapJornalsList(jsonArray));
                    // Relacionando la lista con el adaptador:
                    listViewMissing.setAdapter(listMissingAdapter);

                    total.setText(""+ listMissings.size());

                    //onClickList();

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

                jornal.setJornal_cod(jsonArray.getJSONObject(i).getString("missing_cod"));
                jornal.setWorker_name(jsonArray.getJSONObject(i).getString("worker_name"));
                jornal.setJornal_date(jsonArray.getJSONObject(i).getString("DATE_FORMAT(missing_date, '%d-%m-%Y')"));

                lj.add(jornal);
            }
            listMissings = lj;
        }

        return lj;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new ListMissingsTask().execute();
    }
}
