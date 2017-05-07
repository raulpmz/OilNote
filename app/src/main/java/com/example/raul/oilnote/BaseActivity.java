package com.example.raul.oilnote;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.raul.oilnote.Utils.Connection;

/**
 * Created by raul on 6/5/17.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Connection connection;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
