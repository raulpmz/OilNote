package com.example.raul.oilnote.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.oilnote.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class InfoWeightActivity extends BaseActivity {

    protected TextView tv_date, tv_name, tv_weight, tv_efficiency;
    protected String cod, date, name, number, efficiency;
    protected AlertDialog.Builder alert;
    protected final static int EDIT = 0;
    protected Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_info_weight);

        // Bundle:
        bundle          = getIntent().getExtras();

        // TextView's:
        tv_date         = (TextView) findViewById(R.id.tv_date_weight);
        tv_name         = (TextView) findViewById(R.id.tv_name_plot);
        tv_weight       = (TextView) findViewById(R.id.tv_number_weight);
        tv_efficiency   = (TextView) findViewById(R.id.tv_efficiency);

        // String:
        cod             = bundle.getString("cod");
        date            = bundle.getString("date");
        name            = bundle.getString("type_expense");
        number          = bundle.getString("number");
        efficiency      = bundle.getString("efficiency");

        // AlertDialog:
        alert = new AlertDialog.Builder(InfoWeightActivity.this);
        alert.setCancelable(false);

        // Le damos los valores a los TextView:
        tv_date.setText(date);
        tv_name.setText(name);
        tv_weight.setText(number);
        if (!efficiency.equals("")){
            tv_efficiency.setText(efficiency + " %");
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

            // Opción para editar el pesaje:
            case R.id.action_edit:

                Intent intent = new Intent(InfoWeightActivity.this,EditWeightActivity.class);

                intent.putExtra("cod",cod);
                intent.putExtra("date",date);
                intent.putExtra("type_expense",name);
                intent.putExtra("number",number);
                intent.putExtra("efficiency",efficiency);

                startActivityForResult(intent, EDIT);

                break;

            // Opción para borrar el pesaje:
            case R.id.action_remove:

                alert.setTitle(R.string.attention);
                alert.setMessage(R.string.are_sure_weight);
                alert.setPositiveButton(R.string.add_remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new RemoveWeightTask(cod).execute();
                    }
                });
                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Comprobamos si el resultado de la segunda actividad es "RESULT_CANCELED".
        if (resultCode == RESULT_CANCELED) {

        } else {
            // De lo contrario, recogemos el resultado de la segunda actividad.
            date        = data.getExtras().getString("date");
            name        = data.getExtras().getString("type_expense");
            number      = data.getExtras().getString("number");
            efficiency  = data.getExtras().getString("efficiency");

            // Y rellenamos los TextView con la nueva información:
            switch (requestCode) {
                case EDIT:
                    tv_date.setText(date);
                    tv_name.setText(name);
                    tv_weight.setText(number);
                    if (!efficiency.equals("")){
                        tv_efficiency.setText(efficiency + " %");
                    }
                    break;
            }
        }
    }

    // Hilo para borrar la parcela:
    class RemoveWeightTask extends AsyncTask<Void,Void,JSONObject> {

        private String cod;
        private HashMap<String, String> parametrosPost = new HashMap<>();

        public RemoveWeightTask(String cod) {
            this.cod = cod;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(InfoWeightActivity.this, getResources().getString(R.string.save));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                parametrosPost.put("ins_sql",   "DELETE FROM weights " +
                                                "WHERE user_cod = "+ USER_COD +" " +
                                                "AND weight_cod = "+ cod);
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
                        Toast.makeText(InfoWeightActivity.this, getResources().getString(R.string.successful_remove_weight), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Snackbar.make(findViewById(R.id.LinearInfoWeightActivity), getResources().getString(R.string.error_remove_weight), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.LinearInfoWeightActivity), getResources().getString(R.string.server_down), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_menu);
        if(drawer != null){
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);

            } else {
                startActivity(new Intent(getBaseContext(), ListWeightsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
            }
        }else{
            super.onBackPressed();

        }
    }
}
