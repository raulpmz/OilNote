package com.example.raul.oilnote.Activitys;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.raul.oilnote.R;

public class ListWorkerActivity extends BaseActivity {

    protected ImageView image_worker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_list_worker);

        image_worker = (ImageView) findViewById(R.id.image_worker);

    }

}
