package com.example.raul.oilnote.Activitys;

import android.os.Bundle;

import android.util.Log;


import com.example.raul.oilnote.R;

import static com.example.raul.oilnote.Utils.GlobalVars.*;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);

        Log.e("CODIGO USUARIO","" + USER_COD);
        Log.e("NOMBRE USUARIO","" + USER_NAME);
        Log.e("CORREO USUARIO","" + USER_EMAIL);
        Log.e("CONTRASEÑA USUARIO","" + USER_PASSWORD);
    }


}
