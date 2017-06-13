package com.example.raul.oilnote.Activitys;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.ImageHelper;

public class InfoWorkerActivity extends BaseActivity {

    protected TextView name_worker, phone_worker;
    protected String cod, name, phone, photo;
    protected ImageView imagen_worker, call, sms, whatsapp, share, delete;
    protected LinearLayout linearActions;
    protected Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.activity_info_worker);

        // Bundle:
        bundle          = getIntent().getExtras();

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
                intent = new Intent(Intent.ACTION_VIEW);

                intent.setData(Uri.parse("tel:" + phone));
                intent.setType("vnd.android-dir/mms-sms");
                intent.putExtra("address", phone);
                intent.putExtra("sms_body", "");

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
            case R.id.iv_delete:

                break;

        }
    }
}
