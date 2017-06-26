package com.example.raul.oilnote.Activitys;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import com.example.raul.oilnote.R;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class MarginBenefitsActivity extends BaseActivity {

    protected int expense_money, workers_money, kgs, series1Index;
    protected double efficiency, animation;
    protected DecoView arcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_magin_benefits);


    }

    // Hilo que comprueba las faltas y asistencias del trabajador:
    class InfoTask extends AsyncTask<String, String, JSONArray> {

        // Variables:
        private JSONArray jsonArrayExpenses     = new JSONArray();
        private JSONArray jsonArrayWorkers      = new JSONArray();
        private JSONArray jsonArrayWeights      = new JSONArray();
        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            onProgressDialog(MarginBenefitsActivity.this,getResources().getString(R.string.checking));
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            try {
                // Cosulto los gastos del usuario:
                parametrosPost.put("ins_sql",   "SELECT expense_money " +
                                                "FROM expenses " +
                                                "WHERE user_cod = '" + USER_COD + "'");
                jsonArrayExpenses = connection.sendRequest(BASE_URL_READ, parametrosPost);

                Log.e("jsonArrayWorkers","" + parametrosPost);

                parametrosPost.put("ins_sql",   "SELECT jornal_salary " +
                                                "FROM jornals " +
                                                "WHERE user_cod = '" + USER_COD + "'");
                jsonArrayWorkers = connection.sendRequest(BASE_URL_READ, parametrosPost);

                Log.e("jsonArrayWorkers","" + parametrosPost);

                // Consulto los pesajes y el rendimiento del usuario:
                parametrosPost.put("ins_sql",   "SELECT weight_number, weight_efficiency " +
                        "FROM weights " +
                        "WHERE user_cod = '" + USER_COD + "'");
                jsonArrayWeights = connection.sendRequest(BASE_URL_READ, parametrosPost);
                Log.e("jsonArrayWeights","" + parametrosPost);

            } catch (Exception e) {
                e.getMessage();
            }

            return null;
        }

        protected void onPostExecute(JSONArray json) {
            onStopProgressDialog();

            try {
                mapsInfoDates(jsonArrayExpenses,jsonArrayWorkers, jsonArrayWeights);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void mapsInfoDates(JSONArray jsonArrayExpenses, JSONArray jsonArrayWorkers, JSONArray jsonArrayWeights) throws JSONException {
        /*// Variables:
        double kg, cont = 0, media = 0;



        // Calculo del número total de gastos:
        for (int i = 0 ; i < jsonArrayExpenses.length(); i++){
            expense_money += Integer.parseInt(jsonArrayExpenses.getJSONObject(i).getString("expense_money"));
        }

        tv_tree.setText(getString(R.string.tree) + ": " + tree);

        /// Calculo del número total de jornales:
        for (int i = 0 ; i < jsonArrayWorkers.length(); i++){
            workers_money += Integer.parseInt(jsonArrayWorkers.getJSONObject(i).getString("jornal_salary"));
        }

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
        }*/

        /*efficiency = cont / media;

        animation = (75 * efficiency) / 100;

        setGraphyc((int)efficiency,(int)animation);*/

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

        // Configuramos la animación:
        arcView.addEvent(new DecoEvent.Builder(half).setIndex(series1Index).setDelay(3500).build());

    }
}
