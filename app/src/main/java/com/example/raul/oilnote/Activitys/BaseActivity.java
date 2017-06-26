package com.example.raul.oilnote.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.raul.oilnote.Objects.Worker;
import com.example.raul.oilnote.Objects.User;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Clase base.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    protected static Toolbar toolbar;
    protected Connection connection;
    protected User user;
    protected Worker worker;
    protected DrawerLayout drawer;
    protected ActionBarDrawerToggle toggle;
    protected NavigationView navigationView;
    protected ProgressDialog progressDialog;
    protected AlertDialog.Builder alert;
    protected JSONArray jSONArray;
    protected JSONObject jsonObject;
    protected SharedPreferences prefs;
    protected SharedPreferences.Editor editor;

    public void onCreate(Bundle paramBundle, int layout) {

        super.onCreate(paramBundle);
        setContentView(layout);

        // Toolbar:
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            toolbar.setTitle(R.string.name);
            setSupportActionBar(toolbar);
        }

        // DrawerLayout:
        drawer = (DrawerLayout) findViewById(R.id.drawer_menu);

        // ActionBarDrawerToggle:
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        // NavigationView:
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        if (navigationView != null){
            navigationView.setNavigationItemSelectedListener(this);
        }

        // Clase Conexión:
        connection      = new Connection();

        // Objetos JSON:
        jSONArray       = new JSONArray();
        jsonObject      = new JSONObject();

        // Preferencias:
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            /*case R.id.action_settings:
                Log.e("Pulsado","MenuTool");
                break;

            case R.id.action_add_worker:
                startActivity(new Intent(BaseActivity.this.getBaseContext(),AddWorkerActivity.class));
                break;*/

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {}

    // Opciones del Navigation Drawer:
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_menu);
        Intent intent;

        // Opciones del drawer:
        switch (item.getItemId()) {

            // Botón home del drawer:
            case R.id.home:
                if(!BaseActivity.this.getClass().equals(MainActivity.class)){
                    intent = new Intent(this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    BaseActivity.this.finish();
                }
                break;

            // Botón para añadir trabajador:
            case R.id.add_worker:
                if(!BaseActivity.this.getClass().equals(AddWorkerActivity.class)){
                    intent = new Intent(this,AddWorkerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            // Botón para acceder a la lista de trabajadores:
            case R.id.list_worker:
                if(!BaseActivity.this.getClass().equals(ListWorkerActivity.class)){
                    intent = new Intent(this,ListWorkerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            // Botón para acceder a la lista de jornales:
            case R.id.list_jornals:
                if(!BaseActivity.this.getClass().equals(ListJornalsActivity.class)){
                    intent = new Intent(this,ListJornalsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            // Botón para acceder a la lista de parcelas:
            case R.id.list_plots:
                if(!BaseActivity.this.getClass().equals(ListPlotsActivity.class)){
                    intent = new Intent(this,ListPlotsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            // Botón para acceder a la lista de pesajes:
            case R.id.list_weight:
                if(!BaseActivity.this.getClass().equals(ListWeightsActivity.class)){
                    intent = new Intent(this,ListWeightsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            // Botón para acceder a la lista de faltas:
            case R.id.list_missing:
                if(!BaseActivity.this.getClass().equals(ListMissingActivity.class)){
                    intent = new Intent(this,ListMissingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            // Botón para acceder a la actividad para añadir jornales:
            case R.id.add_jornal:
                if(!BaseActivity.this.getClass().equals(AddJornalActivity.class)){
                    intent = new Intent(this,AddJornalActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            // Botón para acceder a la actividad para añadir parcela:
            case R.id.add_plot:
                if(!BaseActivity.this.getClass().equals(AddPlotActivity.class)){
                    intent = new Intent(this,AddPlotActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            // Botón para acceder a la actividad para añadir pesajes:
            case R.id.add_weight:
                if(!BaseActivity.this.getClass().equals(AddWeightActivity.class)){
                    intent = new Intent(this,AddWeightActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            // Botón para acceder a la actividad para añadir gastos:
            case R.id.list_expense:
                if(!BaseActivity.this.getClass().equals(ListExpensesActivity.class)){
                    intent = new Intent(this,ListExpensesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            // Botón para acceder a la actividad de opciones:
            case R.id.nav_options:
                if(!BaseActivity.this.getClass().equals(OptionsActivity.class)){
                    intent = new Intent(this,OptionsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            // Botón para acceder a la actividad margenes de beneficio:
            case R.id.list_margin_benefits:
                if(!BaseActivity.this.getClass().equals(MarginBenefitsActivity.class)){
                    intent = new Intent(this,MarginBenefitsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;

            // Botón para cerrar la sesión:
            case R.id.nav_close_session:
                editor.putBoolean("is_login", false);
                editor.commit();
                System.exit(0);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_menu);
        if(drawer != null){
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);

            } else {
                startActivity(new Intent(getBaseContext(), MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
            }
        }else{
            super.onBackPressed();

        }
    }
}
