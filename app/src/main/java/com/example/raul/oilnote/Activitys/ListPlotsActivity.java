package com.example.raul.oilnote.Activitys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.raul.oilnote.Adapters.ListPlotAdapter;
import com.example.raul.oilnote.Objects.Plot;
import com.example.raul.oilnote.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class ListPlotsActivity extends BaseActivity {

    protected ListView listViewPlots;
    protected ListPlotAdapter listPlotAdapter;
    protected List<Plot> listPlots;
    protected TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_list_plots);

        // ListView:
        listViewPlots   = (ListView) findViewById(R.id.list_view_plot);

        // List:
        listPlots       = new ArrayList<>();

        // TextView:
        total           = (TextView) findViewById(R.id.total_plots);

        // Hilo para rellenar los datos las parcelas:
        new ListPlotsTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_add_plot).setVisible(true);
        menu.findItem(R.id.action_add_plot).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    // Hilo para obtener los datos de las parcelas:
    class ListPlotsTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListPlotsActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                // Consulto los trabajadores que tiene el usuario:
                parametrosPost.put("ins_sql",   "SELECT * FROM plots WHERE user_cod = '" + USER_COD + "'");

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
                    //System.out.println("Cod_jornal: "+mapPlotsList(jsonArray).get(0).getJornal_date());
                    // Inicializo el adaptador:
                    listPlotAdapter = new ListPlotAdapter(ListPlotsActivity.this, mapPlotsList(jsonArray));
                    // Relacionando la lista con el adaptador:
                    listViewPlots.setAdapter(listPlotAdapter);

                    total.setText(""+listPlots.size());

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
    public List<Plot> mapPlotsList(JSONArray jsonArray) throws JSONException {

        List<Plot> lj = new ArrayList<>();

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                Plot plot = new Plot();

                plot.setCod(jsonArray.getJSONObject(i).getString("plot_cod"));
                plot.setName(jsonArray.getJSONObject(i).getString("plot_name"));
                plot.setNumber_plant(jsonArray.getJSONObject(i).getString("plot_number"));

                lj.add(plot);
            }
            listPlots = lj;
        }

        return lj;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new ListPlotsTask().execute();
    }
}
