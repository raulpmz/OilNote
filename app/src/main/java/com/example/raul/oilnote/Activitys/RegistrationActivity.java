package com.example.raul.oilnote.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.raul.oilnote.R;

public class RegistrationActivity extends BaseActivity {

    // Variables:
    private TextView tv_login;
    private EditText et_user, et_mail, et_password;
    private Button btn_registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // TextView:
        tv_login            = (TextView) findViewById(R.id.tv_login);

        // EditText:
        et_user             = (EditText) findViewById(R.id.et_user);
        et_mail             = (EditText) findViewById(R.id.et_mail);
        et_password         = (EditText) findViewById(R.id.et_mail);

        // Button:
        btn_registration    = (Button) findViewById(R.id.btn_registration);

        // onClick:
        tv_login            .setOnClickListener(this);
        btn_registration    .setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            // En el caso de la selección del botón registrar abrimos RegistrationActivity:
            case R.id.btn_registration:
                break;

            // En el caso de la selección del botón entrar abrimos MainActivity:
            case R.id.tv_login:
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
