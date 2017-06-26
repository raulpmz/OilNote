package com.example.raul.oilnote.Activitys;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;
import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class ListJornalsActivity extends BaseActivity {

    protected LinearLayout linearFilterName, linearFilterDate, linearFilterDateToDate;
    protected Boolean controlFilter, b_name, b_date, b_date_from, b_date_to;
    protected DatePickerDialog.OnDateSetListener mDateFromSetListener;
    protected DatePickerDialog.OnDateSetListener mDateToSetListener;
    protected DatePickerDialog.OnDateSetListener mDateSetListener;
    protected TextView total, tv_date, tv_date_from, tv_date_to;
    protected int year, month, day, compare_from, compare_to;
    protected String name, date, salary, date_from, date_to;
    protected ListJornalAdapter listJornalAdapter;
    protected EditText et_name;
    protected AlertDialog.Builder alert2;
    protected ListView listViewJornals;
    protected List<Jornal> listJornals;
    protected Calendar cal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_list_jornals);

        // ListView:
        listViewJornals         = (ListView) findViewById(R.id.list_jornal);

        // List:
        listJornals             = new ArrayList<>();

        // LinearLayout:
        linearFilterName        = (LinearLayout) findViewById(R.id.LinearFilterName);
        linearFilterDate        = (LinearLayout) findViewById(R.id.LinearFilterDate);
        linearFilterDateToDate  = (LinearLayout) findViewById(R.id.LinearFilterDateFromTo);

        // TextView:
        total                   = (TextView) findViewById(R.id.total_jornal);
        tv_date                 = (TextView) findViewById(R.id.tv_filter_date);
        tv_date_from            = (TextView) findViewById(R.id.tv_filter_date_from);
        tv_date_to              = (TextView) findViewById(R.id.tv_filter_date_to);

        // EditText:
        et_name                 = (EditText) findViewById(R.id.et_filter_name);

        // Boleano:
        controlFilter           = false;
        b_name                  = false;
        b_date                  = false;
        b_date_from             = false;
        b_date_to               = false;

        // Dialogos para los mensajes de información:
        alert                   = new AlertDialog.Builder(this);
        alert2                  = new AlertDialog.Builder(this);

        // Hilo para mostar la lista de jornales:
        new ListJornalsTask().execute();

        // Evento para recoger los caracteres del EditText:
        onKeyListener();

        // Selector de fecha:
        mDateSetListener();
        mDateFromSetListener();
        mDateToSetListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_add_jornal).setVisible(true);
        menu.findItem(R.id.action_add_jornal).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_filter).setVisible(true);
        menu.findItem(R.id.action_filter).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            // En el caso de elegir la opción de añadir jornal:
            case R.id.action_add_jornal:
                startActivity(new Intent(ListJornalsActivity.this,AddJornalActivity.class));
                break;
            // En el caso de elegir la opción de filtros:
            case R.id.action_filter:
                actionFilters();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    // Evento al seleccionar un elemento de la lista:
    public void onClickList(){

        listViewJornals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final CharSequence[] items = new CharSequence[1];

                items[0] = getResources().getString(R.string.add_remove);

                alert.setTitle(getResources().getString(R.string.options))
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                if(pos == 0){
                                    dialog.dismiss();
                                    alert2.setTitle(R.string.attention);
                                    alert2.setMessage(R.string.are_sure_jornal);
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
                parametrosPost.put("ins_sql",   "SELECT jornal_cod ,DATE_FORMAT(jornal_date, '%d-%m-%Y'), worker_name, jornal_salary " +
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

                jornal.setJornal_date(jsonArray.getJSONObject(i).getString("DATE_FORMAT(jornal_date, '%d-%m-%Y')"));
                jornal.setJornal_salary(jsonArray.getJSONObject(i).getString("jornal_salary"));
                jornal.setWorker_name(jsonArray.getJSONObject(i).getString("worker_name"));
                jornal.setJornal_cod(jsonArray.getJSONObject(i).getString("jornal_cod"));

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

    /**
     *      Filtros:
     */

    public void actionFilters(){
        if (controlFilter){

            if(linearFilterName.getVisibility() == View.VISIBLE){
                linearFilterName.setVisibility(View.GONE);
                controlFilter = false;
                et_name.setText(null);
                b_name = false;
            }

            if(linearFilterDate.getVisibility() == View.VISIBLE){
                linearFilterDate.setVisibility(View.GONE);
                controlFilter = false;
                tv_date.setText(R.string.select_date);
                b_date = false;
            }

            if(linearFilterDateToDate.getVisibility() == View.VISIBLE){
                linearFilterDateToDate.setVisibility(View.GONE);
                controlFilter = false;
                tv_date_from.setText(R.string.select_date);
                tv_date_to.setText(R.string.select_date);
                b_date_from = false;
                b_date_to = false;
            }

        }else{
            final CharSequence[] items = new CharSequence[3];

            items[0] = getResources().getString(R.string.for_name);
            items[1] = getResources().getString(R.string.for_date);
            items[2] = getResources().getString(R.string.date_to_date);

            alert.setTitle(getResources().getString(R.string.filter))
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int pos) {

                            // Si la opción es nombre:
                            if(pos == 0){
                                controlFilter = true;

                                if(linearFilterName.getVisibility() == View.GONE){
                                    linearFilterName.setVisibility(View.VISIBLE);
                                    et_name.setText(null);
                                    b_name = false;
                                }

                            }
                            // Si la opción es fecha:
                            if(pos == 1){
                                controlFilter = true;

                                if(linearFilterDate.getVisibility() == View.GONE){
                                    linearFilterDate.setVisibility(View.VISIBLE);
                                    tv_date.setText(R.string.select_date);
                                    b_date = false;
                                }
                            }
                            // Si la opción es de fecha a fecha:
                            if(pos == 2){
                                controlFilter = true;


                                if(linearFilterDateToDate.getVisibility() == View.GONE){
                                    linearFilterDateToDate.setVisibility(View.VISIBLE);
                                    tv_date_from.setText(R.string.select_date);
                                    tv_date_to.setText(R.string.select_date);
                                    b_date_from = false;
                                    b_date_to = false;
                                }
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
        }
    }

    public void onKeyListener(){
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Obtengo los caracteres del EditTect:
                name = et_name.getText().toString();
                b_name = true;
                // Ejecutar el hilo para obtener el filtrado por nombre:
                new ListFilterJornalsTask(name, date, date_from, date_to).execute();

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void setCalendar(){
        cal     = Calendar.getInstance();
        year    = cal.get(Calendar.YEAR);
        month   = cal.get(Calendar.MONTH);
        day     = cal.get(Calendar.DAY_OF_MONTH);
    }

    public void buttonDate(View v){
        setCalendar();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void buttonDateFrom(View v){
        setCalendar();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateFromSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void buttonDateTo(View v){
        setCalendar();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateToSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void mDateSetListener(){
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                date = year + "-" + month + "-" + day;
                tv_date.setText(day + "-" + month + "-" + year);
                b_date = true;

                // Ejecutar el hilo para obtener el filtrado por fecha:
                new ListFilterJornalsTask(name, date, date_from, date_to).execute();
            }
        };
    }

    public void mDateFromSetListener(){
        mDateFromSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                date_from = year + "-" + month + "-" + day;
                compare_from = Integer.parseInt(day + "" + month + "" + year);
                tv_date_from.setText(day + "-" + month + "-" + year);
                b_date_from = true;

                // Ejecutar el hilo si las dos fechas están insertadas:
                if(b_date_from && b_date_to){
                    new ListFilterJornalsTask(name, date, date_from, date_to).execute();
                }
            }
        };
    }

    public void mDateToSetListener(){
        mDateToSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                date_to = year + "-" + month + "-" + day;
                compare_to = Integer.parseInt(day + "" + month + "" + year);
                tv_date_to.setText(day + "-" + month + "-" + year);
                b_date_to = true;

                // Ejecutar el hilo si las dos fechas están insertadas:
                if(b_date_from && b_date_to){
                    new ListFilterJornalsTask(name, date, date_from, date_to).execute();
                }
            }
        };
    }

    class ListFilterJornalsTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();
        private String name, date, date_from, date_to;

        public ListFilterJornalsTask(String name, String date, String date_from, String date_to) {
            this.name = name;
            this.date = date;
            this.date_from = date_from;
            this.date_to = date_to;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListJornalsActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                // Consulto los trabajadores que tiene el usuario:
                if(b_name){
                    parametrosPost.put("ins_sql",   "SELECT jornal_cod ,DATE_FORMAT(jornal_date, '%d-%m-%Y'), worker_name, jornal_salary " +
                                                    "FROM jornals  " +
                                                    "WHERE user_cod = '" + USER_COD + "' " +
                                                    "AND worker_name "+
                                                    "LIKE '%"+ name +"%' "+
                                                    "ORDER BY jornal_date DESC");
                    b_name = false;
                }
                else if(b_date){
                    parametrosPost.put("ins_sql",   "SELECT jornal_cod ,DATE_FORMAT(jornal_date, '%d-%m-%Y'), worker_name, jornal_salary " +
                                                    "FROM jornals  " +
                                                    "WHERE user_cod = '" + USER_COD + "' " +
                                                    "AND jornal_date = '"+ date +"' "+
                                                    "ORDER BY jornal_date DESC");
                    b_date = false;
                }
                else if(b_date_from && b_date_to){

                    if(compare_from < compare_to){
                        parametrosPost.put("ins_sql",   "SELECT jornal_cod ,DATE_FORMAT(jornal_date, '%d-%m-%Y'), worker_name, jornal_salary " +
                                "FROM jornals  " +
                                "WHERE user_cod = '" + USER_COD + "' " +
                                "AND jornal_date " +
                                "BETWEEN '"+ date_from +"' AND '"+ date_to +"' " +
                                "ORDER BY jornal_date DESC");
                    }else {
                        parametrosPost.put("ins_sql",   "SELECT jornal_cod ,DATE_FORMAT(jornal_date, '%d-%m-%Y'), worker_name, jornal_salary " +
                                "FROM jornals  " +
                                "WHERE user_cod = '" + USER_COD + "' " +
                                "AND jornal_date " +
                                "BETWEEN '"+ date_to +"' AND '"+ date_from +"' " +
                                "ORDER BY jornal_date DESC");
                    }

                }
                else{
                    parametrosPost.put("ins_sql",   "SELECT jornal_cod ,DATE_FORMAT(jornal_date, '%d-%m-%Y'), worker_name, jornal_salary " +
                                                    "FROM jornals  " +
                                                    "WHERE user_cod = '" + USER_COD + "' " +
                                                    "ORDER BY jornal_date DESC");
                    b_date_from = false;
                    b_date_to   = false;
                    b_name      = false;
                    b_date      = false;
                }

                jSONArray = connection.sendRequest(BASE_URL_READ, parametrosPost);

                Log.e("parametrosPost",""+parametrosPost);
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


    @Override
    protected void onRestart() {
        super.onRestart();

        b_name                  = false;
        b_date                  = false;
        b_date_from             = false;
        b_date_to               = false;

        if(linearFilterName.getVisibility() == View.VISIBLE){
            linearFilterName.setVisibility(View.GONE);
            controlFilter = false;
            et_name.setText(null);
            b_name = false;
        }

        if(linearFilterDate.getVisibility() == View.VISIBLE){
            linearFilterDate.setVisibility(View.GONE);
            controlFilter = false;
            tv_date.setText(R.string.select_date);
            b_date = false;
        }

        if(linearFilterDateToDate.getVisibility() == View.VISIBLE){
            linearFilterDateToDate.setVisibility(View.GONE);
            controlFilter = false;
            tv_date_from.setText(R.string.select_date);
            tv_date_to.setText(R.string.select_date);
            b_date_from = false;
            b_date_to = false;
        }

        new ListJornalsTask().execute();
    }
}
