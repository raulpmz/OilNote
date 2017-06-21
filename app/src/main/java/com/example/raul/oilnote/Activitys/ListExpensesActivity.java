package com.example.raul.oilnote.Activitys;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.oilnote.Adapters.ListExpenseAdapter;
import com.example.raul.oilnote.Objects.Expense;
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

public class ListExpensesActivity extends BaseActivity {

    protected LinearLayout linearFilterType, linearFilterDate, linearFilterDateToDate;
    protected Boolean controlFilter, b_type, b_date, b_date_from, b_date_to;
    protected DatePickerDialog.OnDateSetListener mDateFromSetListener;
    protected DatePickerDialog.OnDateSetListener mDateToSetListener;
    protected DatePickerDialog.OnDateSetListener mDateSetListener;
    protected TextView total, tv_date, tv_date_from, tv_date_to;
    protected ListExpenseAdapter listExpenseAdapter;
    protected String type_expense, date, date_from, date_to;
    protected AlertDialog.Builder alert2;
    protected ListView listViewExpenses;
    protected List<Expense> listExpense;
    protected int year, month, day, compare_from, compare_to;
    protected Spinner spinner;
    protected Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_list_expenses);

        // ListView:
        listViewExpenses        = (ListView) findViewById(R.id.list_expense);

        // List:
        listExpense             = new ArrayList<>();

        // LinearLayout:
        linearFilterType        = (LinearLayout) findViewById(R.id.LinearFilterType);
        linearFilterDate        = (LinearLayout) findViewById(R.id.LinearFilterDate);
        linearFilterDateToDate  = (LinearLayout) findViewById(R.id.LinearFilterDateFromTo);

        // TextView:
        total                   = (TextView) findViewById(R.id.total_expense);
        tv_date                 = (TextView) findViewById(R.id.tv_filter_date);
        tv_date_from            = (TextView) findViewById(R.id.tv_filter_date_from);
        tv_date_to              = (TextView) findViewById(R.id.tv_filter_date_to);

        // Boleanos:
        controlFilter           = false;
        b_type                  = false;
        b_date                  = false;
        b_date_from             = false;
        b_date_to               = false;

        // Dialogos para los mensajes de información:
        alert                   = new AlertDialog.Builder(this);
        alert2                  = new AlertDialog.Builder(this);

        // Spinner:
        spinner         = (Spinner) findViewById(R.id.sp_filter_type);

        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.type_filter , R.layout.support_simple_spinner_dropdown_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        onKeyListener();

        // Hilo para mostar la lista de gastos:
        new ListExpensesTask().execute();

        // Selector de fecha:
        mDateSetListener();
        mDateFromSetListener();
        mDateToSetListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_expense).setVisible(true);
        menu.findItem(R.id.action_expense).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_filter).setVisible(true);
        menu.findItem(R.id.action_filter).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            // En el caso de elegir la opción de añadir jornal:
            case R.id.action_expense:
                startActivity(new Intent(ListExpensesActivity.this,AddExpenseActivity.class));
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

        listViewExpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                                    alert2.setMessage(R.string.are_sure_expense);
                                    alert2.setPositiveButton(R.string.add_remove, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new RemoveExpenseTask(listExpense.get(i).getCod()).execute();
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
    public String calculateTotalExpenses(List<Expense> listExpense){
        List<Expense> list = listExpense;
        double exp, cont = 0;

        for(int i = 0; i < list.size() ; i++ ){
            exp = Double.parseDouble(list.get(i).getMoney()) ;
            cont += exp;
        }

        return "" + cont;
    }

    class ListExpensesTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListExpensesActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                // Consulto los trabajadores que tiene el usuario:
                parametrosPost.put("ins_sql",   "SELECT expense_cod ,DATE_FORMAT(expense_date, '%d-%m-%Y'), expense_type, expense_money " +
                                                "FROM expenses  " +
                                                "WHERE user_cod = '" + USER_COD + "' " +
                                                "ORDER BY expense_date DESC");

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
                    listExpenseAdapter = new ListExpenseAdapter(ListExpensesActivity.this,mapExpensesList(jsonArray));
                    // Relacionando la lista con el adaptador:
                    listViewExpenses.setAdapter(listExpenseAdapter);

                    total.setText(calculateTotalExpenses(listExpense) + " €");

                    onClickList();

                }else{
                    // Poner una lista avisando de que no tiene jornales:
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Hilo para borrar la parcela:
    class RemoveExpenseTask extends AsyncTask<Void,Void,JSONObject> {

        private String cod;
        private HashMap<String, String> parametrosPost = new HashMap<>();

        public RemoveExpenseTask(String cod) {
            this.cod = cod;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListExpensesActivity.this, getResources().getString(R.string.save));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                parametrosPost.put("ins_sql",   "DELETE FROM expenses " +
                                                "WHERE user_cod = "+ USER_COD +" " +
                                                "AND expense_cod = "+ cod);
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
                        Snackbar.make(findViewById(R.id.LinearExpenses), getResources().getString(R.string.successful_remove_expense), Toast.LENGTH_SHORT).show();
                        new ListExpensesTask().execute();
                    }else{
                        Snackbar.make(findViewById(R.id.LinearExpenses), getResources().getString(R.string.error_remove_expense), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.LinearExpenses), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Mapeo los dato del JSONArray que recivo en una lista de trabajadores para montar el adaptador:
    public List<Expense> mapExpensesList(JSONArray jsonArray) throws JSONException {

        List<Expense> lj = new ArrayList<>();

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                Expense expense = new Expense();

                expense.setCod(jsonArray.getJSONObject(i).getString("expense_cod"));
                expense.setDate(jsonArray.getJSONObject(i).getString("DATE_FORMAT(expense_date, '%d-%m-%Y')"));
                expense.setType(jsonArray.getJSONObject(i).getString("expense_type"));
                expense.setMoney(jsonArray.getJSONObject(i).getString("expense_money"));

                lj.add(expense);
            }
            listExpense = lj;
        }

        return lj;
    }

    /**
     *      Filtros:
     */

    public void actionFilters(){
        if (controlFilter){

            if(linearFilterType.getVisibility() == View.VISIBLE){
                linearFilterType.setVisibility(View.GONE);
                controlFilter = false;
                spinner.setSelection(0);
                b_type = false;
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

            new ListExpensesTask().execute();

        }else{
            final CharSequence[] items = new CharSequence[3];

            items[0] = getResources().getString(R.string.type);
            items[1] = getResources().getString(R.string.for_date);
            items[2] = getResources().getString(R.string.date_to_date);

            alert.setTitle(getResources().getString(R.string.filter))
                    .setCancelable(true)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int pos) {

                            // Si la opción es nombre:
                            if(pos == 0){
                                controlFilter = true;

                                if(linearFilterType.getVisibility() == View.GONE){
                                    linearFilterType.setVisibility(View.VISIBLE);
                                    spinner.setSelection(0);
                                    b_type = false;
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getSelectedItemPosition() == 0){
                    new ListExpensesTask().execute();
                }else{
                    type_expense = spinner.getSelectedItem().toString();
                    b_type = true;
                    new ListFilterExpensesTask(type_expense, date, date_from, date_to).execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                new ListFilterExpensesTask(type_expense, date, date_from, date_to).execute();
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
                    new ListFilterExpensesTask(type_expense, date, date_from, date_to).execute();
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
                    new ListFilterExpensesTask(type_expense, date, date_from, date_to).execute();
                }
            }
        };
    }

    class ListFilterExpensesTask extends AsyncTask<Void, Void, JSONArray> {

        private HashMap<String, String> parametrosPost = new HashMap<>();
        private String type, date, date_from, date_to;

        public ListFilterExpensesTask(String type, String date, String date_from, String date_to) {
            this.type = type;
            this.date = date;
            this.date_from = date_from;
            this.date_to = date_to;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListExpensesActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                // Consulto los trabajadores que tiene el usuario:
                if(b_type){
                    parametrosPost.put("ins_sql",   "SELECT expense_cod ,DATE_FORMAT(expense_date, '%d-%m-%Y'), expense_type, expense_money " +
                                                    "FROM expenses  " +
                                                    "WHERE user_cod = '" + USER_COD + "' " +
                                                    "AND expense_type ='"+ type +"' "+
                                                    "ORDER BY expense_date DESC");
                    b_type = false;
                }
                else if(b_date){
                    parametrosPost.put("ins_sql",   "SELECT expense_cod ,DATE_FORMAT(expense_date, '%d-%m-%Y'), expense_type, expense_money " +
                                                    "FROM expenses  " +
                                                    "WHERE user_cod = '" + USER_COD + "' " +
                                                    "AND expense_date = '"+ date +"' "+
                                                    "ORDER BY expense_date DESC");
                    b_date = false;
                }
                else if(b_date_from && b_date_to){
                    if (compare_from < compare_to){
                        parametrosPost.put("ins_sql",   "SELECT expense_cod ,DATE_FORMAT(expense_date, '%d-%m-%Y'), expense_type, expense_money " +
                                "FROM expenses  " +
                                "WHERE user_cod = '" + USER_COD + "' " +
                                "AND expense_date " +
                                "BETWEEN '"+ date_from +"' AND '"+ date_to +"' " +
                                "ORDER BY expense_date DESC");
                    }else{
                        parametrosPost.put("ins_sql",   "SELECT expense_cod ,DATE_FORMAT(expense_date, '%d-%m-%Y'), expense_type, expense_money " +
                                "FROM expenses  " +
                                "WHERE user_cod = '" + USER_COD + "' " +
                                "AND expense_date " +
                                "BETWEEN '"+ date_to +"' AND '"+ date_from +"' " +
                                "ORDER BY expense_date DESC");
                    }


                }
                else{
                    parametrosPost.put("ins_sql",  "SELECT expense_cod ,DATE_FORMAT(expense_date, '%d-%m-%Y'), expense_type, expense_money " +
                                                    "FROM expenses  " +
                                                    "WHERE user_cod = '" + USER_COD + "' " +
                                                    "ORDER BY expense_date DESC");
                    b_date_from = false;
                    b_date_to   = false;
                    b_type      = false;
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
                    listExpenseAdapter = new ListExpenseAdapter(ListExpensesActivity.this,mapExpensesList(jsonArray));
                    // Relacionando la lista con el adaptador:
                    listViewExpenses.setAdapter(listExpenseAdapter);

                    total.setText(calculateTotalExpenses(listExpense) + " €");

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

        b_type                  = false;
        b_date                  = false;
        b_date_from             = false;
        b_date_to               = false;

        if(linearFilterType.getVisibility() == View.VISIBLE){
            linearFilterType.setVisibility(View.GONE);
            controlFilter = false;
            spinner.setSelection(0);
            b_type = false;
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

        new ListExpensesTask().execute();
    }

}
