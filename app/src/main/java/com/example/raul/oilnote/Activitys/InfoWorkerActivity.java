package com.example.raul.oilnote.Activitys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.oilnote.Adapters.ListJornalWorkerAdapter;
import com.example.raul.oilnote.Objects.Jornal;
import com.example.raul.oilnote.Objects.Weight;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.ImageHelper;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

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

public class InfoWorkerActivity extends BaseActivity {

    protected TextView name_worker, phone_worker, tv_jornal, tv_miss, tvPorciento, tv_total_jornal, tv_total_money;
    protected ImageView imagen_worker, call, sms, whatsapp, share, delete;
    protected int jornal, miss, total, half, animation, series1Index;
    protected List<Jornal> listMissings, listJornals;
    protected ListJornalWorkerAdapter listJornalAdapter;
    protected String cod, name, phone, photo;
    protected LinearLayout linearActions;
    protected AlertDialog.Builder alert;
    protected ListView listViewJornals;
    protected final static int EDIT = 0;
    protected DecoView arcView;
    protected Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.activity_info_worker);

        // Bundle:
        bundle          = getIntent().getExtras();

        // AlertDialog:
        alert = new AlertDialog.Builder(InfoWorkerActivity.this);
        alert.setCancelable(false);

        // ListView:
        listViewJornals         = (ListView) findViewById(R.id.list_jornal);

        // String:
        cod             = bundle.getString("cod");
        name            = bundle.getString("type_expense");
        phone           = bundle.getString("phone");
        photo           = bundle.getString("photo");

        // TextView:
        name_worker     = (TextView) findViewById(R.id.tv_info_worker_name);
        phone_worker    = (TextView) findViewById(R.id.tv_info_worker_phone);
        tvPorciento     = (TextView) findViewById(R.id.tv_porciento);
        tv_jornal       = (TextView) findViewById(R.id.tv_jornal);
        tv_miss         = (TextView) findViewById(R.id.tv_miss);
        tv_total_jornal = (TextView) findViewById(R.id.total_jornal);
        tv_total_money  = (TextView) findViewById(R.id.total_money);

        // ImagenView:
        imagen_worker   = (ImageView) findViewById(R.id.imagen);

        // LinearLayout:
        linearActions   = (LinearLayout) findViewById(R.id.LinearActions);

        // List:

        listMissings    = new ArrayList();
        listJornals     = new ArrayList();

        // ImagenView:
        call            = (ImageView) findViewById(R.id.iv_call);
        sms             = (ImageView) findViewById(R.id.iv_sms);
        whatsapp        = (ImageView) findViewById(R.id.iv_whatsapp);
        delete          = (ImageView) findViewById(R.id.iv_delete);

        // Int:
        series1Index    = 0;

        // DecoView (Grafica):
        arcView         = (DecoView)findViewById(R.id.dynamicArcView);

        // Instancia de los elementos para el onClick:
        call            .setOnClickListener(this);
        sms             .setOnClickListener(this);
        whatsapp        .setOnClickListener(this);
        delete          .setOnClickListener(this);

        // Rellenar campos de la actividad:
        loadDataWorker();

        new CheckJornalMissTask().execute();
    }

    // Relleno los campos con los datos del trabajador:
    public void loadDataWorker(){
        // Nombre:
        name_worker.setText(name);

        // Teléfono (de tenerlo):
        if(!phone.equals("")){
            phone_worker.setText(phone);
            if(linearActions.getVisibility() == View.GONE)linearActions.setVisibility(View.VISIBLE);
        }else{
            phone_worker.setText("");
            if(linearActions.getVisibility() == View.VISIBLE)linearActions.setVisibility(View.GONE);
        }

        // Foto (de tenerla):
        if(!photo.equals("")){
            ImageHelper.rounderImage(photo,imagen_worker);
        }else {
            imagen_worker.setImageResource(R.drawable.user);
        }
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

                Intent intent = new Intent(InfoWorkerActivity.this,EditWorkerActivity.class);

                intent.putExtra("cod",cod);
                intent.putExtra("type_expense",name);
                intent.putExtra("phone",phone);
                intent.putExtra("photo",photo);

                startActivityForResult(intent, EDIT);

                break;

            // Opción para borrar al trabajador:
            case R.id.action_remove:

                alert.setTitle(R.string.attention);
                alert.setMessage(R.string.are_sure_worker);
                alert.setPositiveButton(R.string.add_remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new RemoveWorkerTask().execute();
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
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;

        switch (v.getId()){

            // Para llamar al trabajador:
            case R.id.iv_call:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
                break;

            // Para enviar un mensaje al trabajador:
            case R.id.iv_sms:

                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + phone));

                startActivity(intent);

                break;

            // Para enviar un whatsapp al trabajador:
            case R.id.iv_whatsapp:
                PackageManager pm = getPackageManager();

                try {
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    intent.setPackage("com.whatsapp");
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(intent, "Enviar mensaje a:"));

                } catch (PackageManager.NameNotFoundException e) {
                    Snackbar.make(findViewById(R.id.LinearInfoWorker), "WhatsApp no esta instalado!", Toast.LENGTH_SHORT).show();
                }

                break;



            // Opción para borrar al trabajador:
            case R.id.iv_delete:

                alert.setTitle(R.string.attention);
                alert.setMessage(R.string.are_sure_worker);
                alert.setPositiveButton(R.string.add_remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new RemoveWorkerTask().execute();
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
        if (resultCode == RESULT_CANCELED) {

        } else {
            // De lo contrario, recogemos el resultado de la segunda actividad.
            name    = data.getExtras().getString("type_expense");
            phone   = data.getExtras().getString("phone");
            photo   = data.getExtras().getString("photo");

            // Y rellenamos los TextView con la nueva información:
            switch (requestCode) {
                case EDIT:
                    name_worker.setText(name);

                    // Teléfono (de tenerlo):
                    if(!phone.equals("")){
                        phone_worker.setText(phone);
                        if(linearActions.getVisibility() == View.GONE)linearActions.setVisibility(View.VISIBLE);
                    }else{
                        phone_worker.setText("");
                        if(linearActions.getVisibility() == View.VISIBLE)linearActions.setVisibility(View.GONE);
                    }

                    // Foto (de tenerla):
                    if(!photo.equals("")){
                        ImageHelper.rounderImage(photo,imagen_worker);
                    }else {
                        imagen_worker.setImageResource(R.drawable.user);
                    }
                    break;
            }
        }
    }

    // Hilo para borrar el trabajador:
    class RemoveWorkerTask extends AsyncTask<Void,Void,JSONObject> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(InfoWorkerActivity.this, getResources().getString(R.string.save));
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
                        Toast.makeText(InfoWorkerActivity.this, getResources().getString(R.string.successful_remove_worker), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Snackbar.make(findViewById(R.id.LinearInfoWorker), getResources().getString(R.string.error_remove_worker), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.LinearInfoWorker), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Hilo que comprueba las faltas y asistencias del trabajador:
    class CheckJornalMissTask extends AsyncTask<String, String, JSONArray> {

        // Variables:
        private JSONArray jsonArrayJornal   = new JSONArray();
        private JSONArray jsonArrayMiss     = new JSONArray();
        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            onProgressDialog(InfoWorkerActivity.this,getResources().getString(R.string.checking));
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            try {
                // Consulto los jornales del trabajador:
                parametrosPost.put("ins_sql",   "SELECT jornal_cod ,DATE_FORMAT(jornal_date, '%d-%m-%Y'), worker_name, jornal_salary " +
                                                "FROM jornals  " +
                                                "WHERE user_cod = '" + USER_COD + "' " +
                                                "AND worker_name =  '"+ name +"'" +
                                                "ORDER BY jornal_date DESC");
                jsonArrayJornal = connection.sendRequest(BASE_URL_READ, parametrosPost);

                // Consulto las faltas del trabajador:
                parametrosPost.put("ins_sql",   "SELECT missing_cod ,DATE_FORMAT(missing_date, '%d-%m-%Y'), worker_name " +
                                                "FROM missings  " +
                                                "WHERE user_cod = '" + USER_COD + "' " +
                                                "AND worker_name =  '"+ name +"'" +
                                                "ORDER BY missing_date DESC");
                jsonArrayMiss = connection.sendRequest(BASE_URL_READ, parametrosPost);

                if (jsonArrayJornal != null) {
                    return jSONArray;
                }

                if (jsonArrayMiss != null) {
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
                if (jsonArrayJornal != null){
                    mapJornalsList(jsonArrayJornal);
                    jornal = listJornals.size();
                    total += jornal;

                    // Inicializo el adaptador:
                    listJornalAdapter = new ListJornalWorkerAdapter(InfoWorkerActivity.this, listJornals);
                    // Relacionando la lista con el adaptador:
                    listViewJornals.setAdapter(listJornalAdapter);

                    // Expando el ListView:
                    setListViewHeightBasedOnChildren(listViewJornals);

                    // Jornales totales:
                    tv_total_jornal.setText(getResources().getString(R.string.all_jornal) + " " + listJornals.size());

                    // Dinero ganado:
                    tv_total_money.setText(getResources().getString(R.string.total_money)+ " " + getTotalMoney(listJornals));

                }else{
                    jornal = 0;
                    total += jornal;
                }

                tv_jornal.setText(tv_jornal.getText() +": "+ listJornals.size());

                if(jsonArrayMiss != null){
                    mapMissingsList(jsonArrayMiss);
                    miss = listMissings.size();
                    total += miss;

                }else{
                    miss = 0;
                    total += miss;
                }

                tv_miss.setText(tv_miss.getText() +": "+ listMissings.size());

                if (listJornals.size() == 0 && listMissings.size() == 0){
                    half = 0;
                    total = 0;
                }else{
                    half = (jornal * 100) / total;
                    animation =  (75 * half) / 100;
                }


                setGraphyc(half, animation);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Mapeo los dato del JSONArray que recivo en una lista de trabajadores para montar el adaptador:
    public void mapJornalsList(JSONArray jsonArray) throws JSONException {

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                Jornal jornal = new Jornal();

                jornal.setJornal_cod(jsonArray.getJSONObject(i).getString("jornal_cod"));
                jornal.setWorker_name(jsonArray.getJSONObject(i).getString("worker_name"));
                jornal.setJornal_salary(jsonArray.getJSONObject(i).getString("jornal_salary"));
                jornal.setJornal_date(jsonArray.getJSONObject(i).getString("DATE_FORMAT(jornal_date, '%d-%m-%Y')"));

                listJornals.add(jornal);
            }
        }
    }

    // Mapeo los dato del JSONArray que recivo en una lista de trabajadores para montar el adaptador:
    public void mapMissingsList(JSONArray jsonArray) throws JSONException {

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                Jornal jornal = new Jornal();

                jornal.setJornal_cod(jsonArray.getJSONObject(i).getString("missing_cod"));
                jornal.setWorker_name(jsonArray.getJSONObject(i).getString("worker_name"));
                jornal.setJornal_date(jsonArray.getJSONObject(i).getString("DATE_FORMAT(missing_date, '%d-%m-%Y')"));

                listMissings.add(jornal);
            }
        }
    }

    public String getTotalMoney(List<Jornal> listJornals){
        List<Jornal> list = listJornals;
        double money, cont = 0;

        for(int i = 0; i < list.size() ; i++ ){
            money = Double.parseDouble(list.get(i).getJornal_salary()) ;
            cont += money;
        }

        return cont + " €";
    }

    /**
     *      Grafica:
     */

    // Método para configurar la gráfica, su animación y los valores de esta:
    public void setGraphyc(int half, int animation){
        final SeriesItem seriesItem1 = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.MarronNegro))
                .setRange(0, 100, 0)
                .setInitialVisibility(false)
                .setLineWidth(55f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.4f))
                .setSeriesLabel(new SeriesLabel.Builder("Asistencia %.0f%%").build())
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
        arcView.addEvent(new DecoEvent.Builder(animation).setIndex(series1Index).setDelay(3500).build());
        arcView.addEvent(new DecoEvent.Builder(half).setIndex(series1Index).setDelay(6750).build());

        // Le pasamos los valores a la gráfica:
        seriesItem1.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                // Obtenemos el porcentaje a mostrar:
                float percentFilled = ((currentPosition - seriesItem1.getMinValue()) / (seriesItem1.getMaxValue() - seriesItem1.getMinValue()));
                // Se lo pasamos al TextView:
                tvPorciento.setText(String.format("%.0f%%", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_menu);
        if(drawer != null){
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);

            } else {
                startActivity(new Intent(getBaseContext(), ListWorkerActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
            }
        }else{
            super.onBackPressed();

        }
    }

}
