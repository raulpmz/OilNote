package com.example.raul.oilnote.Activitys;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.CalendarView;
import android.widget.TextView;


import com.example.raul.oilnote.Adapters.ListJornalWorkerAdapter;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.CustomScrollView;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.ExpandListView.setListViewHeightBasedOnChildren;
import static com.example.raul.oilnote.Utils.GlobalVars.*;

public class MainActivity extends BaseActivity {

    protected TextView tv_plots, tv_tree, tv_workers, tv_kgs;
    protected int plots, tree, workers, kgs, series1Index;
    protected double efficiency, animation;
    protected DecoView arcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);

        // TextView:
        tv_plots    = (TextView) findViewById(R.id.tv_plots);
        tv_tree     = (TextView) findViewById(R.id.tv_tree);
        tv_workers  = (TextView) findViewById(R.id.tv_workers);
        tv_kgs      = (TextView) findViewById(R.id.tv_kg);

        // DecoView (Grafica):
        arcView     = (DecoView)findViewById(R.id.dynamicArcView);

        Log.e("CODIGO USUARIO","" + USER_COD);Log.e("NOMBRE USUARIO",USER_NAME);Log.e("CORREO USUARIO",USER_EMAIL);Log.e("CONTRASEÑA USUARIO",USER_PASSWORD);

        new InfoTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_settings).setVisible(true);
        menu.findItem(R.id.action_preferences).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            // Opción ajustes:
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this,OptionsActivity.class));
                break;

            // Opción preferencias:
            case R.id.action_preferences:
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    // Hilo que comprueba las faltas y asistencias del trabajador:
    class InfoTask extends AsyncTask<String, String, JSONArray> {

        // Variables:
        private JSONArray jsonArrayPlots    = new JSONArray();
        private JSONArray jsonArrayWorkers  = new JSONArray();
        private JSONArray jsonArrayWeights  = new JSONArray();
        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            onProgressDialog(MainActivity.this,getResources().getString(R.string.checking));
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            try {
                // Consulto los jornales del trabajador:
                parametrosPost.put("ins_sql",   "SELECT plot_number " +
                                                "FROM plots " +
                                                "WHERE user_cod = '" + USER_COD + "'");
                jsonArrayPlots = connection.sendRequest(BASE_URL_READ, parametrosPost);
                Log.e("jsonArrayPlots","" + parametrosPost);

                // Consulto las faltas del trabajador:
                parametrosPost.put("ins_sql",   "SELECT COUNT(worker_name) " +
                                                "FROM workers " +
                                                "WHERE user_cod = '" + USER_COD + "'");
                jsonArrayWorkers = connection.sendRequest(BASE_URL_READ, parametrosPost);
                Log.e("jsonArrayWorkers","" + parametrosPost);
                // Consulto las faltas del trabajador:
                parametrosPost.put("ins_sql",   "SELECT weight_number, weight_efficiency " +
                                                "FROM weights " +
                                                "WHERE user_cod = '" + USER_COD + "'");
                jsonArrayWeights = connection.sendRequest(BASE_URL_READ, parametrosPost);
                Log.e("jsonArrayWeights","" + parametrosPost);
                if (jsonArrayPlots != null) {
                    return jSONArray;
                }

                if (jsonArrayWorkers != null) {
                    return jSONArray;
                }

                if (jsonArrayWeights != null) {
                    return jSONArray;
                }
            } catch (Exception e) {
                e.getMessage();
            }

            return null;
        }

        protected void onPostExecute(JSONArray json) {
            onStopProgressDialog();

           try {
               mapsInfoDates(jsonArrayPlots,jsonArrayWorkers, jsonArrayWeights);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void mapsInfoDates(JSONArray jsonArrayPlots, JSONArray jsonArrayWorkers, JSONArray jsonArrayWeights) throws JSONException {
        // Variables:
        double kg, cont = 0, media = 0;

        // Número de parcelas:
        plots = jsonArrayPlots.length();

        tv_plots.setText(getString(R.string.plots) + ": " +plots);

        // Calculo del número de olivos:
        for (int i = 0 ; i < jsonArrayPlots.length(); i++){
            tree += Integer.parseInt(jsonArrayPlots.getJSONObject(i).getString("plot_number"));
        }

        tv_tree.setText(getString(R.string.tree) + ": " + tree);

        // Trabajadores del usuario:
        workers = Integer.parseInt(jsonArrayWorkers.getJSONObject(0).getString("COUNT(worker_name)"));

        tv_workers.setText(getString(R.string.workers) + ": " + workers);

        // Cálculo de los kgs reogidos hasta el momento:
        for (int i = 0 ; i < jsonArrayWeights.length(); i++){
            kgs += Integer.parseInt(jsonArrayWeights.getJSONObject(i).getString("weight_number"));
        }

        tv_kgs.setText(getString(R.string.total_weigt) +" "+ kgs);

        // Cálculo del rendimiento medio:
        for(int i = 0; i < jsonArrayWeights.length() ; i++ ) {
            if (!jsonArrayWeights.getJSONObject(i).getString("weight_efficiency").equals("")) {
                kg = Double.parseDouble(jsonArrayWeights.getJSONObject(i).getString("weight_efficiency"));
                cont += kg;
                media++;
            }
        }

        efficiency = cont / media;

        animation = (75 * efficiency) / 100;

        setGraphyc((int)efficiency,(int)animation);

    }

    // Método para configurar la gráfica, su animación y los valores de esta:
    public void setGraphyc(int half, int animation){
        final SeriesItem seriesItem1 = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.MarronNegro))
                .setRange(0, 100, 0)
                .setInitialVisibility(false)
                .setLineWidth(55f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.4f))
                .setSeriesLabel(new SeriesLabel.Builder("%.0f%%").build())
                .setInterpolator(new DecelerateInterpolator())
                .setShowPointWhenEmpty(true)
                .setCapRounded(true)
                .setInset(new PointF(20f, 20f))
                .setDrawAsPoint(false)
                .setSpinClockwise(true)
                .setSpinDuration(6000)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
                .build();

        series1Index = arcView.addSeries(seriesItem1);

        // Animación:
        arcView.addEvent(
                new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                        .setDelay(1000)
                        .setDuration(2000)
                        .build()
        );

        // Configuramos 2 animaciones para mostrar el resultado en dos tiempos:
        //arcView.addEvent(new DecoEvent.Builder(animation).setIndex(series1Index).setDelay(3500).build());
        arcView.addEvent(new DecoEvent.Builder(half).setIndex(series1Index).setDelay(3500).build());

    }

}
