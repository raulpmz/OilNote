package com.example.raul.oilnote.Activitys;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.raul.oilnote.Objects.TypeJornal;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Connection;
import com.example.raul.oilnote.Utils.CustomRadioGroups;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class TypeJornalActivity extends AppCompatActivity {

    protected LinearLayout linearSeisQuince, linearSeisTreinta, linearSiete, linearSieteDescanso, linearCustom;
    protected CustomRadioGroups rg = new CustomRadioGroups ();
    protected ProgressDialog progressDialog;
    protected JSONObject jsonObject;
    protected Connection connection;
    protected RadioButton[] rb;
    protected Toolbar toolbar;
    protected Dialog dialog;
    protected SharedPreferences prefs;
    protected SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_jornal);

        // Preferencias:
        prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        editor = prefs.edit();

        // Toolbar:
        toolbar         = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            toolbar.setTitle(R.string.name);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // LinearLayouts:
        linearSeisQuince    = (LinearLayout) findViewById(R.id.LinearSeisQuince);
        linearSeisTreinta   = (LinearLayout) findViewById(R.id.LinearSeisTreinta);
        linearSiete         = (LinearLayout) findViewById(R.id.LinearSiete);
        linearSieteDescanso = (LinearLayout) findViewById(R.id.LinearSieteDescanso);
        linearCustom        = (LinearLayout) findViewById(R.id.LinearCustom);

        clickLinear();

        // Dialogo:
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_custom_type_jornal);
        buttonAdd();

        // Clase Conexión:
        connection      = new Connection();

        // Objetos JSON:
        jsonObject      = new JSONObject();

        // CustomRadioGroups:
        rg = new CustomRadioGroups ();

        // Array RadioButtons:
        rb = new RadioButton [5];

        // Añadimos los RadioButtons al RadioGroup:
        rb[0] = (RadioButton)findViewById(R.id.rb_seis_quince);
        rb[1] = (RadioButton)findViewById(R.id.rb_seis_treinta);
        rb[2] = (RadioButton)findViewById(R.id.rb_siete_descanso);
        rb[3] = (RadioButton)findViewById(R.id.rb_siete);
        rb[4] = (RadioButton)findViewById(R.id.rb_custom);

        rg.createRadioGroup(rb);

        rb[prefs.getInt("type_jornal",1)-1].setChecked(true);

        onSelectedItemRB();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_add).setVisible(true);
        menu.findItem(R.id.action_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){

            // En el caso de elegir la opción de añadir jornal:
            case R.id.action_add:
                dialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickLinear(){
        linearSeisQuince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeJornalActivity.this,InfoAsajaJornal.class);
                intent.putExtra("cod",1);
                startActivity(intent);
            }
        });
        linearSeisTreinta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeJornalActivity.this,InfoAsajaJornal.class);
                intent.putExtra("cod",2);
                startActivity(intent);
            }
        });
        linearSieteDescanso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeJornalActivity.this,InfoAsajaJornal.class);
                intent.putExtra("cod",3);
                startActivity(intent);
            }
        });
        linearSiete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeJornalActivity.this,InfoAsajaJornal.class);
                intent.putExtra("cod",4);
                startActivity(intent);
            }
        });
        linearCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TypeJornalActivity.this, InfoTypeJornalActivity.class));
            }
        });
    }

    public void onSelectedItemRB(){
        rb[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rb[0].isChecked()){
                    editor.putInt("type_jornal", 1);
                    editor.commit();
                }
            }
        });
        rb[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rb[1].isChecked()){
                    editor.putInt("type_jornal", 2);
                    editor.commit();
                }
            }
        });
        rb[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rb[2].isChecked()){
                    editor.putInt("type_jornal", 3);
                    editor.commit();
                }
            }
        });
        rb[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rb[3].isChecked()){
                    editor.putInt("type_jornal", 4);
                    editor.commit();
                }
            }
        });
        rb[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(rb[4].isChecked()){
                    editor.putInt("type_jornal", 5);
                    editor.commit();
                }
            }
        });

    }

    public void buttonAdd(){

        final EditText et_value = (EditText) dialog.findViewById(R.id.et_cuantity);
        final EditText et_name  = (EditText) dialog.findViewById(R.id.et_name);

        dialog.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new AddTypeJornalTask(et_name.getText().toString(), et_value.getText().toString()).execute();
                dialog.dismiss();
            }
        });
    }


    // Hilo para guardar el tipo de jornal:
    class AddTypeJornalTask extends AsyncTask<Void,Void,JSONObject> {

        private HashMap<String, String> parametrosPost = new HashMap<>();
        private String name, money;

        public AddTypeJornalTask(String name, String money) {
            this.name = name;
            this.money = money;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(TypeJornalActivity.this, getResources().getString(R.string.save));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                parametrosPost.put("ins_sql",   "INSERT INTO type_jornal (user_cod, type_name, type_value) " +
                                                "VALUES ('"+ USER_COD +"' ,'"+ name +"','"+ money +"');");
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
                        Snackbar.make(findViewById(R.id.LinearTypeJornal), getResources().getString(R.string.successful_add_type_jornal), Toast.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(findViewById(R.id.LinearTypeJornal), getResources().getString(R.string.error_add_type_jornal), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.LinearTypeJornal), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
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
