package com.example.raul.oilnote.Activitys;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.CustomRadioGroups;

public class TypeJornalActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected CustomRadioGroups rg = new CustomRadioGroups ();
    protected RadioButton[] rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_jornal);

        // Toolbar:
        toolbar         = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            toolbar.setTitle(R.string.name);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // CustomRadioGroups:
        rg = new CustomRadioGroups ();

        // Array RadioButtons:
        rb = new RadioButton [5];

        // Añadimos los RadioButtons al RadioGroup:
        rb[0] = (RadioButton)findViewById(R.id.rb_seis_quince);
        rb[1] = (RadioButton)findViewById(R.id.rb_seis_treinta);
        rb[2] = (RadioButton)findViewById(R.id.rb_siete_descanso);
        rb[3] = (RadioButton)findViewById(R.id.rb_siete);
        rb[4] = (RadioButton)findViewById(R.id.rb_custom);

        rg.createRadioGroup(rb);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_add).setVisible(true);
        menu.findItem(R.id.action_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            // En el caso de elegir la opción de añadir jornal:
            case R.id.action_add:

                break;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
