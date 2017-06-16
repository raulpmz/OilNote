package com.example.raul.oilnote.Activitys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.raul.oilnote.Adapters.ListPlotAdapter;
import com.example.raul.oilnote.Adapters.ListWeightAdapter;
import com.example.raul.oilnote.Objects.Plot;
import com.example.raul.oilnote.Objects.Weight;
import com.example.raul.oilnote.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class ListWeightsActivity extends BaseActivity {

    protected ListView listViewWeigths;
    protected ListWeightAdapter listWeightAdapter;
    protected List<Weight> listWeight;
    protected TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_list_weights);

        // ListView:
        listViewWeigths   = (ListView) findViewById(R.id.list_view_weigth);

        // List:
        listWeight       = new ArrayList<>();

        // TextView:
        total           = (TextView) findViewById(R.id.total_weights);

        // Hilo para rellenar los datos las parcelas:
        new ListWeigthsTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_add_weigth).setVisible(true);
        menu.findItem(R.id.action_add_weigth).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    // Metodo para calcular el total de los kilogramos:
    public String calculateTotalWeigth(List<Weight> listWeight){
        List<Weight> list = listWeight;
        int kg, cont = 0;

        for(int i = 0; i < list.size() ; i++ ){
            kg = Integer.parseInt(list.get(i).getWeight_number()) ;
            cont += kg;
        }

        return "" + cont;
    }

    // Hilo para obtener los datos de las parcelas:
    class ListWeigthsTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListWeightsActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                // Consulto los trabajadores que tiene el usuario:
                parametrosPost.put("ins_sql",   "SELECT weight_cod ,DATE_FORMAT(weight_date, '%d-%m-%Y'), plot_name, weight_number " +
                                                "FROM weights a " +
                                                "INNER JOIN plots b " +
                                                "        ON a.plot_cod = b.plot_cod " +
                                                "WHERE a.user_cod = '" + USER_COD + "'");

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
                    listWeightAdapter = new ListWeightAdapter(ListWeightsActivity.this, mapWeigthsList(jsonArray));
                    // Relacionando la lista con el adaptador:
                    listViewWeigths.setAdapter(listWeightAdapter);

                    System.out.println("Total kilogramos: "+calculateTotalWeigth(listWeight));
                    total.setText(calculateTotalWeigth(listWeight) + " kg");

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
    public List<Weight> mapWeigthsList(JSONArray jsonArray) throws JSONException {

        List<Weight> lw = new ArrayList<>();

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                Weight weight = new Weight();

               weight.setWeight_cod(jsonArray.getJSONObject(i).getString("weight_cod"));
                weight.setWeight_date(jsonArray.getJSONObject(i).getString("DATE_FORMAT(weight_date, '%d-%m-%Y')"));
               weight.setPlot_name(jsonArray.getJSONObject(i).getString("plot_name"));
               weight.setWeight_number(jsonArray.getJSONObject(i).getString("weight_number"));

                lw.add(weight);
            }
            listWeight = lw;
        }

        return lw;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new ListWeigthsTask().execute();
    }


}
