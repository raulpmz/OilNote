package com.example.raul.oilnote.Activitys;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.raul.oilnote.Objects.Laborer;
import com.example.raul.oilnote.Objects.User;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Clase base.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    protected Connection connection;
    protected User user;
    protected Laborer laborer;
    protected DrawerLayout drawer;
    protected static Toolbar toolbar;
    protected ActionBarDrawerToggle toggle;
    protected NavigationView navigationView;
    protected ProgressDialog progressDialog;
    protected String url_query, url_insert;
    protected JSONArray jSONArray;
    protected JSONObject jsonObject;

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

        //Clase Conexión:
        connection  = new Connection();

        // Url:
        url_query   = "http://iesayala.ddns.net/raulpmz/imprime.php";
        url_insert  = "http://iesayala.ddns.net/raulpmz/escribe.php";

        // ProgressDialog:
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);

        jSONArray       = new JSONArray();
        jsonObject      = new JSONObject();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.e("Pulsado","MenuTool");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {}

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_menu);
        switch (item.getItemId()) {
            /*case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;*/
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }

    }
}
