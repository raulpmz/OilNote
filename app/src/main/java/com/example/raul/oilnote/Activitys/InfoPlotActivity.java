package com.example.raul.oilnote.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.oilnote.Adapters.InfoPlotAdapter;
import com.example.raul.oilnote.Adapters.ListWeightAdapter;
import com.example.raul.oilnote.Objects.Weight;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.ImageHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.ExpandListView.setListViewHeightBasedOnChildren;
import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class InfoPlotActivity extends BaseActivity {

    protected TextView name_plot, number_plant;
    protected InfoPlotAdapter infoPlotAdapter;
    protected final static int EDIT = 0;
    protected AlertDialog.Builder alert;
    protected String cod, name, number;
    protected ListView listViewWeigths;
    protected List<Weight> listWeight;
    protected TextView total;
    protected Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_info_plot);

        // Bundle:
        bundle          = getIntent().getExtras();

        // TextView:
        name_plot       = (TextView) findViewById(R.id.tv_name_plot);
        number_plant    = (TextView) findViewById(R.id.tv_number_plant);

        // String:
        cod             = bundle.getString("cod");
        name            = bundle.getString("type_expense");
        number          = bundle.getString("number_plant");

        // ListView:
        listViewWeigths = (ListView) findViewById(R.id.list_plot_weight);

        // List:
        listWeight      = new ArrayList<>();

        // TextView:
        total           = (TextView) findViewById(R.id.tv_total_weight);

        // AlertDialog:
        alert = new AlertDialog.Builder(InfoPlotActivity.this);
        alert.setCancelable(false);

        // Se le dan los valores a los TextView:
        name_plot.setText(name);
        number_plant.setText(number);

        // Hilo para comprobar los kg de la parcela:
        new ListWeigthsTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_edit).setVisible(true);
        menu.findItem(R.id.action_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_remove).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            // Opción para editar al trabajador:
            case R.id.action_edit:

                Intent intent = new Intent(InfoPlotActivity.this,EditPlotActivity.class);

                intent.putExtra("cod",cod);
                intent.putExtra("type_expense",name);
                intent.putExtra("number_plant",number);

                startActivityForResult(intent, EDIT);

                break;

            // Opción para borrar al trabajador:
            case R.id.action_remove:

                alert.setTitle(R.string.attention);
                alert.setMessage(R.string.are_sure_plant);
                alert.setPositiveButton(R.string.add_remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new RemovePlotTask(cod).execute();
                    }
                });
                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
        if (resultCode == RESULT_CANCELED) {

        } else {
            // De lo contrario, recogemos el resultado de la segunda actividad.
            name     = data.getExtras().getString("type_expense");
            number   = data.getExtras().getString("number");

            // Y rellenamos los TextView con la nueva información:
            switch (requestCode) {
                case EDIT:
                    name_plot.setText(name);
                    number_plant.setText(number);
                    break;
            }
        }
    }

    // Hilo para borrar la parcela:
    class RemovePlotTask extends AsyncTask<Void,Void,JSONObject> {

        private String cod;
        private HashMap<String, String> parametrosPost = new HashMap<>();

        public RemovePlotTask(String cod) {
            this.cod = cod;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(InfoPlotActivity.this, getResources().getString(R.string.save));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                parametrosPost.put("ins_sql",   "DELETE FROM plots " +
                                                "WHERE user_cod = "+ USER_COD +" " +
                                                "AND plot_cod = "+ cod);
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
                        Toast.makeText(InfoPlotActivity.this, getResources().getString(R.string.successful_remove_plot), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Snackbar.make(findViewById(R.id.LinearInfoPlotActivity), getResources().getString(R.string.error_remove_plot), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.LinearInfoPlotActivity), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Hilo para obtener los datos de las parcelas:
    class ListWeigthsTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(InfoPlotActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                // Consulto los trabajadores que tiene el usuario:
                parametrosPost.put("ins_sql",   "SELECT DATE_FORMAT(weight_date, '%d-%m-%Y'), weight_number " +
                                                "FROM weights " +
                                                "WHERE plot_name = '"+ name +"' " +
                                                "AND user_cod = '" + USER_COD +"' "+
                                                "ORDER BY weight_date");
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
                    infoPlotAdapter = new InfoPlotAdapter(InfoPlotActivity.this, mapWeigthsList(jsonArray));

                    // Relacionando la lista con el adaptador:
                    listViewWeigths.setAdapter(infoPlotAdapter);

                    // Expando el ListView:
                    setListViewHeightBasedOnChildren(listViewWeigths);

                    // Pongo el total de los kg recogidos en esa parcela:
                    total.setText("Total recogidos " + calculateTotalWeigth(listWeight) + " kg");

                }else{
                    // Poner una lista avisando de que no tiene jornales:
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    // Mapeo los dato del JSONArray que recivo en una lista de trabajadores para montar el adaptador:
    public List<Weight> mapWeigthsList(JSONArray jsonArray) throws JSONException {

        List<Weight> lw = new ArrayList<>();

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                Weight weight = new Weight();

                weight.setWeight_date(jsonArray.getJSONObject(i).getString("DATE_FORMAT(weight_date, '%d-%m-%Y')"));
                weight.setWeight_number(jsonArray.getJSONObject(i).getString("weight_number"));

                lw.add(weight);
            }
            listWeight = lw;
        }

        return lw;
    }
}
