package com.nd.countrylist;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nd.countrylist.Adapter.DividerItemDecoration;
import com.nd.countrylist.Adapter.ListDataAdapter;
import com.nd.countrylist.Parser.CountryList;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListDataAdapter.ListDataAdapterListener {
    RecyclerView recyclerView;
    List<CountryList> countryList;
    ListDataAdapter mAdapter;
    SearchView searchView;

    CommonUtils mCommonUtils;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor prefsEditor;

    String API_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Country List");

        mCommonUtils = new CommonUtils(this);
        mSharedPreferences = getSharedPreferences(getApplicationContext().getPackageName(), 0);
        prefsEditor = mSharedPreferences.edit();

        recyclerView = findViewById(R.id.recycler_view);
        countryList = new ArrayList<>();
        mAdapter = new ListDataAdapter(this, countryList, this);

        API_URL = mCommonUtils.APIURL + "all";

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, 0));
        recyclerView.setAdapter(mAdapter);

        if (mCommonUtils.check_Internet()) {
            fetchCountryList();
        } else {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_LONG).show();
        }
    }

    private void fetchCountryList() {
        JsonArrayRequest request = new JsonArrayRequest(API_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<CountryList> items = new Gson().fromJson(response.toString(), new TypeToken<List<CountryList>>() {
                        }.getType());

                        countryList.clear();
                        countryList.addAll(items);
                        mAdapter.notifyDataSetChanged();

                        setDataInLocalStorage();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        setSearchView(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setSearchView(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mAdapter != null) mAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (mAdapter != null) mAdapter.getFilter().filter(query);
                return true;
            }
        });
    }

    @Override
    public void onItemSelected(CountryList item) {
        Intent mIntent = new Intent(MainActivity.this, ActivityDetails.class);
        mIntent.putExtra("country_name", item.getName());
        mIntent.putExtra("flag_url", item.getFlag());
        mIntent.putExtra("country_code_2", item.getAlpha2Code());
        mIntent.putExtra("country_code_3", item.getAlpha3Code());
        startActivity(mIntent);
    }

    public void setDataInLocalStorage() {
        // prefsEditor.putString("country_list", countryList.toString());
        // prefsEditor.commit();
    }
}
