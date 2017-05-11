package com.example.raul.oilnote.Activitys;

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

import com.example.raul.oilnote.Objects.Laborer;
import com.example.raul.oilnote.Objects.User;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Connection;

/**
 * Clase base.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    protected Connection connection;
    protected User user;
    protected Laborer laborer;
    protected DrawerLayout drawer;
    protected static Toolbar toolbar;
    protected ActionBarDrawerToggle toggle;
    protected NavigationView navigationView;

    public void onCreate(Bundle paramBundle, int layout) {

        super.onCreate(paramBundle);
        setContentView(layout);

        connection = new Connection();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            toolbar.setTitle(R.string.name);
            setSupportActionBar(toolbar);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_menu);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        if (navigationView != null){
            navigationView.setNavigationItemSelectedListener(this);
        }
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
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_menu);
        switch (item.getItemId()) {
            case R.id.nav_camera:
                Log.e("Pulsado","MenuDrawer");
                break;
            case R.id.nav_gallery:
                Log.e("Pulsado","MenuDrawer");
                break;
            case R.id.nav_slideshow:
                Log.e("Pulsado","MenuDrawer");
                break;
            case R.id.nav_manage:
                Log.e("Pulsado","MenuDrawer");
                break;
            case R.id.nav_share:
                Log.e("Pulsado","MenuDrawer");
                break;
            case R.id.nav_send:
                Log.e("Pulsado","MenuDrawer");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_menu);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
