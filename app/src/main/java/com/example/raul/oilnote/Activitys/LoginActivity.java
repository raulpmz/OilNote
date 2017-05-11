package com.example.raul.oilnote.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.raul.oilnote.R;

public class LoginActivity extends BaseActivity {

    private Button btn_enter, btn_registration;
    private EditText et_mail, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Botones:
        btn_enter           = (Button) findViewById(R.id.btn_enter);
        btn_registration    = (Button) findViewById(R.id.btn_registration);

        // EditText:
        et_mail             = (EditText) findViewById(R.id.et_mail);
        et_password         = (EditText) findViewById(R.id.et_password);

        // onClick:
        btn_enter           .setOnClickListener(this);
        btn_registration    .setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            // En el caso de la selección del botón entrar abrimos MainActivity:
            case R.id.btn_enter:
                startActivity(new Intent(this, MainActivity.class));
                break;

            // En el caso de la selección del botón registrar abrimos RegistrationActivity:
            case R.id.btn_registration:
                startActivity(new Intent(this,RegistrationActivity.class));
                break;
        }
    }

    // Verificamos si el usuario y la contraseña son correctos:
    public void loginVerification(){

    }


}
