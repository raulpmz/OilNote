package com.example.raul.oilnote.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.raul.oilnote.R;

public class OptionsActivity extends BaseActivity {

    LinearLayout linearSegurity, linearJornal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_options);

        // LinearLayout:
        linearSegurity  = (LinearLayout) findViewById(R.id.LinearSegurity);

        linearSegurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OptionsActivity.this, ChangePassword.class));
            }
        });

        linearJornal    = (LinearLayout) findViewById(R.id.LinearTypeJornal);

        linearJornal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OptionsActivity.this, TypeJornalActivity.class));
            }
        });


    }
}
