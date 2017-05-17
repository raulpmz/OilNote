package com.example.raul.oilnote.Activitys;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.RoundedImageView;

public class ListWorkerActivity extends BaseActivity {

    protected RoundedImageView image_worker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.contact_list_adapter);

        image_worker = (RoundedImageView) findViewById(R.id.image_worker);
        image_worker.setImageDrawable(getResources().getDrawable(R.drawable.twitter_logo_on_black_background));
    }

}
