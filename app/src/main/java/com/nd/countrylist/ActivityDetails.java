package com.nd.countrylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

public class ActivityDetails extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbar;

    private String mStrFlagURL = "";
    private String mStrCountryName = "";
    private String mStrCountryCode2 = "";
    private String mStrCountryCode3 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        if (getIntent() != null ) {
            Intent intent = getIntent();
            mStrFlagURL = intent.getStringExtra("flag_url");
            mStrCountryName = intent.getStringExtra("country_name");
            mStrCountryCode2 = intent.getStringExtra("country_code_2");
            mStrCountryCode3 = intent.getStringExtra("country_code_3");

            setCollapsingToolbar();
        }
    }

    private void setCollapsingToolbar() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);

        if (mStrCountryName != null && !mStrCountryName.isEmpty()) {
            collapsingToolbar.setTitle(mStrCountryName);
        }

        if (mStrFlagURL != null && !mStrFlagURL.isEmpty()) {
            GlideApp.with(ActivityDetails.this)
                    .load(mStrFlagURL)
                    .into(imageView);
        }
    }

}
