package com.nd.countrylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;

public class ActivityDetails extends AppCompatActivity {
    String API_URL = "";
    CommonUtils mCommonUtils;
    CollapsingToolbarLayout collapsingToolbar;

    String mStrFlagURL = "";
    String mStrCountryName = "";
    String mStrCountryCode2 = "";
    String mStrCountryCode3 = "";

    TextView mTextViewCapital;
    TextView mTextViewRegion;
    TextView mTextViewPopulation;
    TextView mTextViewLanguage;
    TextView mTextViewCurrency;
    TextView mTextViewPhoneCode;
    TextView mTextViewWebDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCommonUtils = new CommonUtils(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mTextViewCapital = (TextView) findViewById(R.id.txt_capital_value);
        mTextViewRegion = (TextView) findViewById(R.id.txt_val_region);
        mTextViewPopulation = (TextView) findViewById(R.id.txt_val_population);
        mTextViewLanguage = (TextView) findViewById(R.id.txt_val_language);
        mTextViewCurrency = (TextView) findViewById(R.id.txt_val_currency);
        mTextViewPhoneCode = (TextView) findViewById(R.id.txt_val_phone_code);
        mTextViewWebDomain = (TextView) findViewById(R.id.txt_val_web_domain);

        if (getIntent() != null) {
            Intent intent = getIntent();
            mStrFlagURL = intent.getStringExtra("flag_url");
            mStrCountryName = intent.getStringExtra("country_name");
            mStrCountryCode2 = intent.getStringExtra("country_code_2");
            mStrCountryCode3 = intent.getStringExtra("country_code_3");

            setCollapsingToolbar();
            API_URL = mCommonUtils.APIURL + "alpha/" + mStrCountryCode2;

            if (mCommonUtils.check_Internet()) {
                fetchCountryData();
            } else {
                Toast.makeText(this, "No internet connection.", Toast.LENGTH_LONG).show();
            }
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

    private void fetchCountryData() {
        StringRequest request = new StringRequest(API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setCountryData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    private void setCountryData(String response) {
        if (response != null && !response.isEmpty()) {
            // Log.e("response", " : " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);

                String mStrCapitalName = jsonObject.getString("capital");
                String mStrRegionName = jsonObject.getString("region");
                String mStrSubRegionName = jsonObject.getString("subregion");
                String mStrPopulation = jsonObject.getString("population");

                String mStrLanguage = "";
                String mStrCurrency = "";
                String mStrWebDomain = "";
                String mStrCallingCode = "";

                if (mStrCapitalName != null && !mStrCapitalName.isEmpty()) {
                    mTextViewCapital.setText(mStrCapitalName);
                }

                if (mStrRegionName != null && !mStrRegionName.isEmpty()
                    && mStrSubRegionName != null && !mStrSubRegionName.isEmpty()) {
                    mTextViewRegion.setText(mStrRegionName + ", " + mStrSubRegionName);
                }

                if (mStrPopulation != null && !mStrPopulation.isEmpty()) {
                    mTextViewPopulation.setText(mStrPopulation);
                }

                JSONArray mLanguages = jsonObject.getJSONArray("languages");
                if (mLanguages != null && mLanguages.length() > 0) {
                    for (int i = 0; i < mLanguages.length(); i++) {
                        if (i != (mLanguages.length() - 1)) {
                            mStrLanguage += mLanguages.getJSONObject(i).getString("name") + ", ";
                        } else {
                            mStrLanguage += mLanguages.getJSONObject(i).getString("name");
                        }
                    }

                    mTextViewLanguage.setText(mStrLanguage);
                }

                JSONArray mCurrency = jsonObject.getJSONArray("currencies");
                if (mCurrency != null && mCurrency.length() > 0) {
                    for (int i = 0; i < mCurrency.length(); i++) {
                        if (i != (mCurrency.length() - 1)) {
                            mStrCurrency += mCurrency.getJSONObject(i).getString("symbol") + " - " + mCurrency.getJSONObject(i).getString("name") + " ("+ mCurrency.getJSONObject(i).getString("code") +")\n";
                        } else {
                            mStrCurrency += mCurrency.getJSONObject(i).getString("symbol") + " - " + mCurrency.getJSONObject(i).getString("name") + " ("+ mCurrency.getJSONObject(i).getString("code") +")";
                        }
                    }

                    mTextViewCurrency.setText(mStrCurrency);
                }

                JSONArray mWebDomain = jsonObject.getJSONArray("topLevelDomain");
                if (mWebDomain != null && mWebDomain.length() > 0) {
                    for (int i = 0; i < mWebDomain.length(); i++) {
                        if (i != (mWebDomain.length() - 1)) {
                            mStrWebDomain += mWebDomain.get(i) + ", ";
                        } else {
                            mStrWebDomain += mWebDomain.get(i);
                        }
                    }

                    mTextViewWebDomain.setText(mStrWebDomain);
                }

                JSONArray mCallingCode = jsonObject.getJSONArray("callingCodes");
                if (mCallingCode != null && mCallingCode.length() > 0) {
                    for (int i = 0; i < mCallingCode.length(); i++) {
                        if (i != (mCallingCode.length() - 1)) {
                            mStrCallingCode += "+" + mCallingCode.get(i) + ", ";
                        } else {
                            mStrCallingCode += "+" + mCallingCode.get(i);
                        }
                    }

                    mTextViewPhoneCode.setText(mStrCallingCode);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
