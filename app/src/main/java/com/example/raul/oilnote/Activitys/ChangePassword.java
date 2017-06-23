package com.example.raul.oilnote.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Connection;
import com.example.raul.oilnote.Utils.MailJob;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.GlobalVars.CHANGEPASS;
import static com.example.raul.oilnote.Utils.GlobalVars.CONTENT;
import static com.example.raul.oilnote.Utils.GlobalVars.MAIL_APP;
import static com.example.raul.oilnote.Utils.GlobalVars.MAIL_PASS;
import static com.example.raul.oilnote.Utils.GlobalVars.SUBJETC;
import static com.example.raul.oilnote.Utils.GlobalVars.SUBJETC2;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_EMAIL;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_PASSWORD;

public class ChangePassword extends AppCompatActivity {

    protected EditText old_pass, new_pass, new_pass2;
    protected ProgressDialog progressDialog;
    protected Connection connection;
    protected JSONObject jsonObject;
    protected String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // EditText's:
        old_pass    = (EditText) findViewById(R.id.old_pass);
        new_pass    = (EditText) findViewById(R.id.new_pass);
        new_pass2   = (EditText) findViewById(R.id.new_pass2);

        // JsonObject:
        jsonObject  = new JSONObject();

        // Clase Conexión:
        connection      = new Connection();

    }

    public void bChangePass(View v){
        if(ischeckVerification()){
            pass = new_pass.getText().toString();
            new ChangePassTask().execute();
        }
    }

    public boolean ischeckVerification(){
        // Si los campos están vacíos:
        if(old_pass.getText().toString().equals("")){
            old_pass.setError(getString(R.string.emptry_camp));
            old_pass.requestFocus();
            return false;
        }
        if(new_pass.getText().toString().equals("")){
            new_pass.setError(getString(R.string.emptry_camp));
            new_pass.requestFocus();
            return false;
        }
        if(new_pass2.getText().toString().equals("")){
            new_pass2.setError(getString(R.string.emptry_camp));
            new_pass2.requestFocus();
            return false;
        }

        // Si la contraseña actual no coincide con la escrita:
        if (!old_pass.getText().toString().equals(USER_PASSWORD)){
            old_pass.setError(getString(R.string.error_old_password));
            old_pass.requestFocus();
            return false;
        }
        // Si las contraseñas nuevas no coinciden:
        if(!new_pass.getText().toString().equals(new_pass2.getText().toString())){
            new_pass2.setError(getString(R.string.distinc_camp));
            new_pass2.requestFocus();
            return false;
        }

        // Si contienen menos de 6 caracteres:
        if(old_pass.getText().length() < 6){
            old_pass.setError(getString(R.string.error_password));
            old_pass.requestFocus();
            return false;
        }
        if(new_pass.getText().length() < 6){
            new_pass.setError(getString(R.string.error_password));
            new_pass.requestFocus();
            return false;
        }
        if(new_pass2.getText().length() < 6){
            new_pass2.setError(getString(R.string.error_password));
            new_pass2.requestFocus();
            return false;
        }

        // Si la contraseña es la misma que la anterior:
        if(new_pass.getText().toString().equals(USER_PASSWORD) || new_pass2.getText().toString().equals(USER_PASSWORD)){
            new_pass.setError(getString(R.string.equals_password));
            new_pass.requestFocus();
            return false;
        }

        return true;
    }

    // Hilo encargado de cambiar la contraseña:
    class ChangePassTask extends AsyncTask<String, String, JSONObject> {

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            onProgressDialog(ChangePassword.this,getResources().getString(R.string.change_password));
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                parametrosPost.put("ins_sql",   "UPDATE users " +
                                                "SET user_password = '"+ pass +"' " +
                                                "WHERE user_cod = "+ USER_COD +";");
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
            onStopProgressDialog();

            if (json != null) {
                try {
                    if(json.getInt("added") == 1){
                        Snackbar.make(findViewById(R.id.LinearChangePass), getResources().getString(R.string.successful_change), Toast.LENGTH_SHORT).show();
                        sendMail();
                        USER_PASSWORD = pass;
                    }else{
                        Snackbar.make(findViewById(R.id.LinearChangePass), getResources().getString(R.string.error_change), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.LinearChangePass), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ProgressDialog de la aplicación al cual le paso el contexto y el mensaje a aparecer en cada caso:
    public void onProgressDialog(Context context, String msg){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.show();
    }

    // Método con el que paramos el ProgressDialog:
    public void onStopProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public void sendMail(){
        new MailJob(MAIL_APP, MAIL_PASS).execute(
                new MailJob.Mail(MAIL_APP, USER_EMAIL, SUBJETC2, CHANGEPASS + pass)
        );
    }
}
