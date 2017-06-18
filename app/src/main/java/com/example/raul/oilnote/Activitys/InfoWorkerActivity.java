package com.example.raul.oilnote.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.ImageHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.GlobalVars.USER_COD;

public class InfoWorkerActivity extends BaseActivity {

    protected ImageView imagen_worker, call, sms, whatsapp, share, delete;
    protected TextView name_worker, phone_worker;
    protected String cod, name, phone, photo;
    protected LinearLayout linearActions;
    protected AlertDialog.Builder alert;
    protected final static int EDIT = 0;
    protected Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.activity_info_worker);

        // Bundle:
        bundle          = getIntent().getExtras();

        // AlertDialog:
        alert = new AlertDialog.Builder(InfoWorkerActivity.this);
        alert.setCancelable(false);

        // String:
        cod             = bundle.getString("cod");
        name            = bundle.getString("name");
        phone           = bundle.getString("phone");
        photo           = bundle.getString("photo");

        // TextView:
        name_worker     = (TextView) findViewById(R.id.tv_info_worker_name);
        phone_worker    = (TextView) findViewById(R.id.tv_info_worker_phone);

        // ImagenView:
        imagen_worker   = (ImageView) findViewById(R.id.imagen);

        // LinearLayout:
        linearActions   = (LinearLayout) findViewById(R.id.LinearActions);

        // ImagenView:
        call            = (ImageView) findViewById(R.id.iv_call);
        sms             = (ImageView) findViewById(R.id.iv_sms);
        whatsapp        = (ImageView) findViewById(R.id.iv_whatsapp);
        share           = (ImageView) findViewById(R.id.iv_share);
        delete          = (ImageView) findViewById(R.id.iv_delete);

        // Instancia de los elementos para el onClick:
        call            .setOnClickListener(this);
        sms             .setOnClickListener(this);
        whatsapp        .setOnClickListener(this);
        share           .setOnClickListener(this);
        delete          .setOnClickListener(this);

        // Rellenar campos de la actividad:
        loadDataWorker();
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
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                intent.putExtra("photo",photo);

                startActivityForResult(intent, EDIT);

                break;

            // Opción para borrar al trabajador:
            case R.id.action_remove:

                alert.setTitle(R.string.attention);
                alert.setMessage(R.string.are_sure);
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

            case R.id.iv_share:

                break;

            // Opción para borrar al trabajador:
            case R.id.iv_delete:

                alert.setTitle(R.string.attention);
                alert.setMessage(R.string.are_sure);
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
            name    = data.getExtras().getString("name");
            phone   = data.getExtras().getString("phone");
            photo   = data.getExtras().getString("photo");
            // Y tratamos el resultado en función de si se lanzó para rellenar el
            // nombre o el apellido.
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

}
