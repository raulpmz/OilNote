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
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends BaseActivity {

    private Button btn_enter;
    private TextView tv_registration;
    private EditText et_user, et_password;

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

        // onClick:
        btn_enter           .setOnClickListener(this);
        tv_registration     .setOnClickListener(this);

        connection = new Connection();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            // En el caso de la selección del botón entrar abrimos MainActivity:
            case R.id.btn_enter:
                loginVerification();
                break;

            // En el caso de la selección del botón registrar abrimos RegistrationActivity:
            case R.id.tv_registration:
                startActivity(new Intent(this,RegistrationActivity.class));
                finish();
                break;
        }
    }

    // Verificamos si el usuario y la contraseña son correctos:
    public void loginVerification(){
        //new LoginTask(et_user.getText().toString(),et_password.getText().toString()).execute();
    }



    class LoginTask extends AsyncTask<String, String, JSONArray> {

        private JSONArray jSONArray;
        private String username, userpassword;

        public LoginTask(String username, String userpassword) {
            this.username = username;
            this.userpassword = userpassword;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... args) {
            try {
                HashMap<String, String> parametrosPost = new HashMap<>();
                parametrosPost.put("ins_sql", "select * from users where user_name ='"+username+"' and user_password = '"+userpassword+"'");
                jSONArray = connection.sendRequest(url_consulta, parametrosPost);

                if (jSONArray != null) {
                    System.out.println("Obtiene objeto jSon");
                    return jSONArray;

                }else{
                    System.out.println("No obtiene objeto jSon");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (json != null) {
                Log.e("Json",""+json);
            }else {
                Log.e("Json","Vacio");
            }
        }
    }

}
