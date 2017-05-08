package com.example.raul.oilnote;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.raul.oilnote.Objects.Laborer;
import com.example.raul.oilnote.Objects.User;
import com.example.raul.oilnote.Utils.Connection;

/**
 * Clase base.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Connection connection;
    private User user;
    private Laborer laborer;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
