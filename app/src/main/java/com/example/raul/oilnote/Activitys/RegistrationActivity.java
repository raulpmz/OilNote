package com.example.raul.oilnote.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.raul.oilnote.Utils.GlobalVars.*;

public class RegistrationActivity extends BaseActivity {

    // Variables:
    protected TextView tv_login;
    protected EditText et_user, et_mail, et_password;
    protected Button btn_registration;
    protected String user, email, pass;
    protected Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // TextView:
        tv_login            = (TextView) findViewById(R.id.tv_login);

        // EditText:
        et_user             = (EditText) findViewById(R.id.et_user);
        et_mail             = (EditText) findViewById(R.id.et_mail);
        et_password         = (EditText) findViewById(R.id.et_password);

        // Button:
        btn_registration    = (Button) findViewById(R.id.btn_registration);

        // onClick:
        tv_login            .setOnClickListener(this);
        btn_registration    .setOnClickListener(this);

        //Clase Conexión:
        connection  = new Connection();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            // En el caso de la selección del botón registrar abrimos RegistrationActivity:
            case R.id.btn_registration:

                user    = et_user.getText().toString().toLowerCase();
                email   = et_mail.getText().toString().toLowerCase();
                pass    = et_password.getText().toString();

                //Ocultar el teclado:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_password.getWindowToken(), 0);
                startRegitrer();

                break;

            // En el caso de la selección del botón entrar abrimos MainActivity:
            case R.id.tv_login:

            startActivity(new Intent(this, LoginActivity.class));
            finish();

            break;
        }
    }

    // Método para comprobar que los EditText's estén correctamente rellenados:
    public Boolean regitrerVerification(){

        // Si el EditText Usuario está vacio:
        if(et_user.getText().toString().isEmpty()){
            et_user.setError(getString(R.string.emptry_camp));
            et_user.requestFocus();
            return false;
        }

        if(et_user.getText().length() < 4){
            et_user.setError(getString(R.string.error_user));
            et_user.requestFocus();
            return false;
        }

        // Si el EditText Correo está vacio:
        if(et_mail.getText().toString().isEmpty()){
            et_mail.setError(getString(R.string.emptry_camp));
            et_mail.requestFocus();
            return false;
        }

        // Si el EditText Contraseña está vacio:
        if(et_password.getText().toString().isEmpty()){
            et_password.setError(getString(R.string.emptry_camp));
            et_password.requestFocus();
            return false;
        }

        if(et_password.getText().length() < 6){
            et_password.setError(getString(R.string.error_password));
            et_password.requestFocus();
            return false;
        }

        // Comprobamos que tiene estructura de email:
        if(et_mail.getText().toString().contains("@") && (et_mail.getText().toString().contains(".es") || et_mail.getText().toString().contains(".com") || et_mail.getText().toString().contains(".net"))){
            return true;
        }else{
            et_mail.setError(getString(R.string.error_mail));
            et_mail.requestFocus();
            return false;
        }
    }

    // Ejecutamos el hilo:
    public void startRegitrer(){
        if(regitrerVerification()) new CheckRegistrationTask().execute();
    }

    // Hilo que comprueba que no hay ningún usuario con esas credenciales:
    class CheckRegistrationTask extends AsyncTask<String, String, JSONArray> {

        // Variables:
        private boolean user_exist       = false;
        private boolean email_exist      = false;

        private JSONArray jsonArrayUser  = new JSONArray();
        private JSONArray jsonArrayEmail = new JSONArray();

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RegistrationActivity.this);
            progressDialog.setMessage("Comprobando...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            try {
                // Consulto que el usuario no existe en la base de datos:
                parametrosPost.put("ins_sql", "select * from users where user_name = '" + user + "'");
                jsonArrayUser = connection.sendRequest(BASE_URL_READ, parametrosPost);

                if (jsonArrayUser != null && jsonArrayUser.length() > 0 ) {
                    user_exist  = true;
                    return jSONArray;
                }

                // Consulto que el correo no tiene más de una cuenta registrada:
                parametrosPost.put("ins_sql", "select * from users where user_email ='" + email + "'");
                jsonArrayEmail = connection.sendRequest(BASE_URL_READ, parametrosPost);

                if (jsonArrayEmail != null && jsonArrayEmail.length() > 0) {
                    email_exist = true;
                    return jSONArray;
                }
            } catch (Exception e) {
                e.getMessage();
            }

            return null;
        }

        protected void onPostExecute(JSONArray json) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            // Si no hay ningun usuario con este tipo lanzo el hilo para registrarlo:
            if (!user_exist && !email_exist) {
                new RegistrationTask().execute();
            }else {
                // Si el usuario ya está en uso:
                if(user_exist){
                    et_user.setError(getString(R.string.user_exist));
                    et_user.requestFocus();
                }

                // Si el correo ya tiene una cuenta registrada:
                if(email_exist){
                    et_mail.setError(getString(R.string.email_exist));
                    et_mail.requestFocus();
                }
            }
        }
    }

    // Hilo encargado de registrar al usuario:
    class RegistrationTask extends AsyncTask<String, String, JSONObject> {

        // Variables:
        private JSONObject jsonObject = new JSONObject();
        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RegistrationActivity.this);
            progressDialog.setMessage("Registrado usuario...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                parametrosPost.put("ins_sql", "INSERT INTO users (user_name, user_email, user_password) VALUES('" + user + "','" + email + "','" + pass + "');"); //INSERT INTO users (user_name, user_email, user_password) VALUES("raulpmz","raulpm92@gmail.com","administrador");
                jsonObject = connection.sendWrite(BASE_URL_WRITE, parametrosPost);

                if (jsonObject != null) {
                    return jsonObject;
                }

            } catch (JSONException e) {
                e.getMessage();
            }

            return null;
        }
        protected void onPostExecute(JSONObject json) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (json != null) {
                try {
                    if(json.getInt("added") == 1){
                        Snackbar.make(findViewById(R.id.layout_Register), getResources().getString(R.string.successful_registration), Toast.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(findViewById(R.id.layout_Register), getResources().getString(R.string.error_registration), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.layout_Register), getResources().getString(R.string.error_registration), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
