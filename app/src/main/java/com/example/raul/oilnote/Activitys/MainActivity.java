package com.example.raul.oilnote.Activitys;

import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ScrollView;


import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.CustomScrollView;

import static com.example.raul.oilnote.Utils.GlobalVars.*;

public class MainActivity extends BaseActivity {

    protected CalendarView calendar;
    protected CustomScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);

        Log.e("CODIGO USUARIO","" + USER_COD);
        Log.e("NOMBRE USUARIO","" + USER_NAME);
        Log.e("CORREO USUARIO","" + USER_EMAIL);
        Log.e("CONTRASEÑA USUARIO","" + USER_PASSWORD);

        // CalendarView:
        calendar        = (CalendarView) findViewById(R.id.calendar);
        //scrollView       = (CustomScrollView) findViewById(R.id.Scroll);

        Log.e("calendar.getWidth","" + calendar.getWidth());



    }


}
