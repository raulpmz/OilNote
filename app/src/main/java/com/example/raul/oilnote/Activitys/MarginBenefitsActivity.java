package com.example.raul.oilnote.Activitys;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.example.raul.oilnote.R;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.HashMap;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class MarginBenefitsActivity extends BaseActivity {

    protected TextView total_expense, total_benefits, tv_jornal, tv_expenses, tv_weight, tv_efficiency, tv_litro;
    protected int  kgs, series1Index, series2Index;
    protected double expense_money, workers_money, efficiency, animation, oil, total;
    protected DecoView arcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_magin_benefits);

        // TextView's:
        total_expense   = (TextView) findViewById(R.id.tv_total_expenses);
        total_benefits  = (TextView) findViewById(R.id.tv_total_benefits);
        tv_jornal       = (TextView) findViewById(R.id.tv_jornal);
        tv_expenses     = (TextView) findViewById(R.id.tv_expenses);
        tv_weight       = (TextView) findViewById(R.id.tv_weight);
        tv_efficiency   = (TextView) findViewById(R.id.tv_efficiency);
        tv_litro        = (TextView) findViewById(R.id.tv_litro);

        // DecoView (Gráfica):
        arcView     = (DecoView)findViewById(R.id.dynamicArcView);

        new InfoTask().execute();

    }

    // Hilo que comprueba las faltas y asistencias del trabajador:
    class InfoTask extends AsyncTask<String, String, JSONArray> {

        // Variables:
        private JSONArray jsonArrayExpenses     = new JSONArray();
        private JSONArray jsonArrayWorkers      = new JSONArray();
        private JSONArray jsonArrayWeights      = new JSONArray();
        private JSONArray jsonArrayOil          = new JSONArray();
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

                // Consulto el precio del aceite:
                parametrosPost.put("ins_sql",   "SELECT * " +
                                                "FROM oil ");
                jsonArrayOil = connection.sendRequest(BASE_URL_READ, parametrosPost);
                Log.e("jsonArrayWeights","" + parametrosPost);

            } catch (Exception e) {
                e.getMessage();
            }

            return null;
        }

        protected void onPostExecute(JSONArray json) {
            onStopProgressDialog();

            try {
                mapsInfoDates(jsonArrayExpenses,jsonArrayWorkers, jsonArrayWeights, jsonArrayOil);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void mapsInfoDates(JSONArray jsonArrayExpenses, JSONArray jsonArrayWorkers, JSONArray jsonArrayWeights, JSONArray jsonArrayOil) throws JSONException {
        // Variables:
        double te, kg, cont = 0, media = 0;



        // Calculo del número total de gastos:
        for (int i = 0 ; i < jsonArrayExpenses.length(); i++){
            expense_money += Double.parseDouble(jsonArrayExpenses.getJSONObject(i).getString("expense_money"));
        }

        tv_expenses.setText(getString(R.string.expenses) + ": " + expense_money + " €");

        /// Calculo del número total de jornales:
        for (int i = 0 ; i < jsonArrayWorkers.length(); i++){
            workers_money += Double.parseDouble(jsonArrayWorkers.getJSONObject(i).getString("jornal_salary"));
        }

        tv_jornal.setText(getString(R.string.list_jornal) + ": " + workers_money + " €");

        te = expense_money + workers_money;

        DecimalFormat df1 = new DecimalFormat("0.00");
        String fot = df1.format(te);

        total_expense.setText(getString(R.string.total) + " " + fot + " €");

        // Cálculo de los kgs reogidos hasta el momento:
        for (int i = 0 ; i < jsonArrayWeights.length(); i++){
            kgs += Integer.parseInt(jsonArrayWeights.getJSONObject(i).getString("weight_number"));
        }

        tv_weight.setText(getString(R.string.total_weigt) +" "+ kgs +" Kg");

        // Cálculo del rendimiento medio:
        for(int i = 0; i < jsonArrayWeights.length() ; i++ ) {
            if (!jsonArrayWeights.getJSONObject(i).getString("weight_efficiency").equals("")) {
                kg = Double.parseDouble(jsonArrayWeights.getJSONObject(i).getString("weight_efficiency"));
                cont += kg;
                media++;
            }
        }

        efficiency = cont / media;

        tv_efficiency.setText(getString(R.string.efficiency) + ": "+ efficiency +" %");

        // Precio del aceite:
        oil = Double.parseDouble(jsonArrayOil.getJSONObject(0).getString("oil_price"));

        tv_litro.setText(getString(R.string.prec) + " " + jsonArrayOil.getJSONObject(0).getString("oil_price")+ " €");

        total = (kgs / (100/ efficiency)) * oil;

        DecimalFormat df = new DecimalFormat("0.00");
        String fotmat = df.format(total);

        total_benefits.setText(getString(R.string.total) + " " + fotmat + " €");

        animation = (te * 100) / total;

        Log.e("anima",""+(int)animation);
        setGraphyc((int)total,(int)animation);

    }

    // Método para configurar la gráfica, su animación y los valores de esta:
    public void setGraphyc(int expenses, int benefit){
        final SeriesItem seriesItem1 = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.Green))
                .setRange(0, 100, 0)
                .setInitialVisibility(false)
                .setLineWidth(55f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.4f))
                .setSeriesLabel(new SeriesLabel.Builder("Beneficios %.0f%%").build())
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

        final SeriesItem seriesItem2 = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.Red))
                .setRange(0, 100, 0)
                .setInitialVisibility(false)
                .setLineWidth(55f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.4f))
                .setSeriesLabel(new SeriesLabel.Builder("Gastos %.0f%%").build())
                .setInterpolator(new DecelerateInterpolator())
                .setShowPointWhenEmpty(true)
                .setCapRounded(true)
                .setInset(new PointF(20f, 20f))
                .setDrawAsPoint(false)
                .setSpinClockwise(true)
                .setSpinDuration(6000)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
                .build();

        series2Index = arcView.addSeries(seriesItem2);

        // Animación:
        arcView.addEvent(
                new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                        .setDelay(1000)
                        .setDuration(2000)
                        .build()
        );

        // Configuramos la animación:
        arcView.addEvent(new DecoEvent.Builder(100 - benefit).setIndex(series1Index).setDelay(3500).build());

        // Configuramos la animación:
        arcView.addEvent(new DecoEvent.Builder(benefit).setIndex(series2Index).setDelay(7500).build());

    }
}
