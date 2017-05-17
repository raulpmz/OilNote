package com.example.raul.oilnote.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Connection;

import org.json.JSONArray;

import java.util.HashMap;

import static com.example.raul.oilnote.Utils.HttpQuery.BASE_URL_READ;

public class LoginActivity extends BaseActivity {

    protected Button btn_enter;
    protected TextView tv_registration;
    protected EditText et_user, et_password;
    protected String username, userpassword, url_query;
    protected Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Botones:
        btn_enter           = (Button) findViewById(R.id.btn_enter);

        // TextView:
        tv_registration     = (TextView) findViewById(R.id.tv_registration);

        // EditText:
        et_user             = (EditText) findViewById(R.id.et_user);
        et_password         = (EditText) findViewById(R.id.et_password);
        et_user             .setText("raulpmz");
        et_password         .setText("administrador");

        // onClick:
        btn_enter           .setOnClickListener(this);
        tv_registration     .setOnClickListener(this);

        //Clase Conexión:
        connection  = new Connection();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            // En el caso de la selección del botón entrar abrimos MainActivity:
            case R.id.btn_enter:
                username        = et_user.getText().toString().toLowerCase();
                userpassword    = et_password.getText().toString();
                startLogin();
                break;

            // En el caso de la selección del botón registrar abrimos RegistrationActivity:
            case R.id.tv_registration:
                startActivity(new Intent(this,RegistrationActivity.class));
                finish();
                break;
        }
    }

    // Verificamos si el usuario y la contraseña son correctos:
    public Boolean loginVerification(){

        // Si el EditText Usuario está vacio:
        if(et_user.getText().toString().isEmpty()){
            et_user.setError(getString(R.string.emptry_camp));
            return false;
        }

        // Si el EditText Contraseña está vacio:
        if(et_password.getText().toString().isEmpty()){
            et_password.setError(getString(R.string.emptry_camp));
            return false;
        }

        // Si el EditText Contraseña tiene menos de 6 digitos:
        if(et_password.getText().length() < 6){
            et_password.setError(getString(R.string.error_password));
            return false;
        }

        return true;
    }

    // Si los datos complen de forma correcta las condiciones se inicia la consulta asincrona:
    public void startLogin(){
        Log.e("User",username);
        Log.e("Password",userpassword);
        if(loginVerification()) new LoginTask().execute();
    }

    // Hilo para comprobar los datos del usuario se encuentran en la base de datos:
    class LoginTask extends AsyncTask<String, String, JSONArray> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Comprobando datos...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            try {
                HashMap<String, String> parametrosPost = new HashMap<>();
                parametrosPost.put("ins_sql", "select * from users where user_name ='" + username + "' and user_password = '" + userpassword + "'");

                jSONArray = connection.sendRequest(BASE_URL_READ, parametrosPost);
                Log.e("jSONArray",""+parametrosPost);

                if (jSONArray != null) {
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

            if (json != null && json.length() > 0) {
                Log.e("Json",""+json);
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }else {
                et_password.setError(getString(R.string.login_fail));
            }
        }
    }

}
