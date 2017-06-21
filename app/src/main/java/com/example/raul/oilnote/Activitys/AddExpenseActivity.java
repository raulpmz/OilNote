package com.example.raul.oilnote.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Connection;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class AddExpenseActivity extends AppCompatActivity {

    protected ProgressDialog progressDialog;
    protected SimpleDateFormat ss1, ss2;
    protected String date, type, money;
    protected JSONObject jsonObject;
    protected Connection connection;
    protected CalendarView calendar;
    protected EditText et_money;
    protected TextView tv_date;
    protected Toolbar toolbar;
    protected Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // CalendarView:
        calendar        = (CalendarView) findViewById(R.id.calendar_add_expenses);

        // TextView:
        tv_date         = (TextView) findViewById(R.id.tv_day);

        // Spinner:
        spinner         = (Spinner) findViewById(R.id.sp_add_expenses);

        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.type_expenses , R.layout.support_simple_spinner_dropdown_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        // EditText:
        et_money      = (EditText) findViewById(R.id.et_add_money);

        // Toolbar:
        toolbar         = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            toolbar.setTitle(R.string.name);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // SimpleDateFormat:
        ss1             = new SimpleDateFormat("dd-MM-yyyy");
        ss2             = new SimpleDateFormat("yyyy-MM-dd");

        // Seteamos el TextView con la fecha de hoy:
        tv_date.setText(ss1.format(calendar.getDate()));

        // Guardamos en una variable tipo string la fecha para realizar la consulta sql:
        date = ss2.format(calendar.getDate());

        // Clase Conexión:
        connection      = new Connection();

        // Objetos JSON:
        jsonObject      = new JSONObject();

        // Evento calendario:
        getCalendarOnDateChangeListener();
    }

    // Controlamos el evento que se genera cada vez que cambiamos la fecha del CalendarView:
    public void getCalendarOnDateChangeListener(){
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                int mes = i1+1;
                tv_date.setText(i2 + "-" + mes + "-" + i);
                date = i + "-" + mes + "-" + i2;
            }
        });
    }

    // Botón para añadir el pesaje:
    public void addExpenses(View w){
        // Guardo los valores en las variables:
        type    = spinner.getSelectedItem().toString();
        money   = et_money.getText().toString();

        // Compruebo que el EditText del peso no esta vacío:
        if(et_money.length() > 0){
            // Ejecutamos el hilo para registrar el peso en la base de datos:
            new AddExpensesTask().execute();
            // De estar vacío mostramos un mensaje de error al usuario:
        }else{
            et_money.setError(getString(R.string.emptry_camp));
            et_money.requestFocus();
        }
    }

    // Hilo para guardar el pesaje en la base de datos:
    class AddExpensesTask extends AsyncTask<Void,Void,JSONObject> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(AddExpenseActivity.this, getResources().getString(R.string.save));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                parametrosPost.put("ins_sql",   "INSERT INTO expenses (user_cod ,expense_date, expense_type, expense_money) " +
                                                "VALUES('"+ USER_COD +"','"+ date +"','"+ type +"','"+ money +"')");
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
                        Snackbar.make(findViewById(R.id.LinearAddExpense), getResources().getString(R.string.successful_add_expense), Toast.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(findViewById(R.id.LinearAddExpense), getResources().getString(R.string.error_add_expense), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                et_money.setText(null);
            }else{
                Snackbar.make(findViewById(R.id.LinearAddExpense), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onProgressDialog(Context context, String msg){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();
    }

    // Método con el que paramos el ProgressDialog:
    public void onStopProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
