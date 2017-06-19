package com.example.raul.oilnote.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.oilnote.Adapters.ListPlotAdapter;
import com.example.raul.oilnote.Adapters.ListWeightAdapter;
import com.example.raul.oilnote.Objects.Plot;
import com.example.raul.oilnote.Objects.Weight;
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

public class ListWeightsActivity extends BaseActivity {

    protected ListWeightAdapter listWeightAdapter;
    protected AlertDialog.Builder alert2;
    protected ListView listViewWeigths;
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

        // Dialogos para los mensajes de información:
        alert           = new AlertDialog.Builder(this);
        alert2          = new AlertDialog.Builder(this);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case R.id.action_add_weigth:
                startActivity(new Intent(ListWeightsActivity.this,AddWeightActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    // Evento al seleccionar un elemento de la lista:
    public void onClickList(){
        listViewWeigths.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ListWeightsActivity.this,InfoWeightActivity.class);

                intent.putExtra("cod",listWeight.get(i).getWeight_cod());
                intent.putExtra("date",listWeight.get(i).getWeight_date());
                intent.putExtra("name",listWeight.get(i).getPlot_name());
                intent.putExtra("number",listWeight.get(i).getWeight_number());

                startActivity(intent);
            }
        });

        listViewWeigths.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

                                    Intent intent = new Intent(ListWeightsActivity.this,InfoWeightActivity.class);

                                    intent.putExtra("cod",listWeight.get(i).getWeight_cod());
                                    intent.putExtra("date",listWeight.get(i).getWeight_date());
                                    intent.putExtra("name",listWeight.get(i).getPlot_name());
                                    intent.putExtra("number",listWeight.get(i).getWeight_number());

                                    startActivity(intent);
                                }
                                // Si la opción es eliminar:
                                if(pos == 1){
                                    dialog.dismiss();
                                    alert2.setTitle(R.string.attention);
                                    alert2.setMessage(R.string.are_sure_weight);
                                    alert2.setPositiveButton(R.string.add_remove, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new RemoveWeightTask(listWeight.get(i).getWeight_cod()).execute();
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
                                                "FROM weights " +
                                                "WHERE user_cod = '" + USER_COD + "' " +
                                                "ORDER BY weight_date DESC");
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
                    listWeightAdapter = new ListWeightAdapter(ListWeightsActivity.this, mapWeigthsList(jsonArray));

                    // Relacionando la lista con el adaptador:
                    listViewWeigths.setAdapter(listWeightAdapter);

                    total.setText(calculateTotalWeigth(listWeight) + " kg");

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

    // Hilo para borrar la parcela:
    class RemoveWeightTask extends AsyncTask<Void,Void,JSONObject> {

        private String cod;
        private HashMap<String, String> parametrosPost = new HashMap<>();

        public RemoveWeightTask(String cod) {
            this.cod = cod;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListWeightsActivity.this, getResources().getString(R.string.save));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                parametrosPost.put("ins_sql",   "DELETE FROM weights " +
                                                "WHERE user_cod = "+ USER_COD +" " +
                                                "AND weight_cod = "+ cod);
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
                        Snackbar.make(findViewById(R.id.LinearListWeight), getResources().getString(R.string.successful_remove_weight), Toast.LENGTH_SHORT).show();
                        new ListWeigthsTask().execute();
                    }else{
                        Snackbar.make(findViewById(R.id.LinearListWeight), getResources().getString(R.string.error_remove_weight), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.LinearListWeight), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new ListWeigthsTask().execute();
    }


}
