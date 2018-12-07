package com.my.easybus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.util.ArrayList;

import BusinessLogic.LogicClass;
import Entities.Price;
import Entities.Schedule;
import adapters.PriceAdapter;
import adapters.ScheduleAdapter;
import utility.Utility;

/**
 * Created by Richard.Ezama on 26/04/2016.
 */
public class Prices extends AppCompatActivity {
    private ProgressDialog prgDialog;
    String json = null, y = null;
    private UrlEncodedFormEntity uefa;
    private ProgressBar progress;
    private PriceAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private Intent i;
    private Toolbar toolbar;
    ArrayList<Price> items;


    /////////////recycle view
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        prgDialog=new ProgressDialog(this);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Prices");
        toolbar.setLogo(R.drawable.arrowleft);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case android.R.id.home:
                        finish();
                }
                return true;
            }
        });
        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        //rv.setLayoutManager(llm);
        mLayoutManager = new GridLayoutManager(this,1);
        rv.setLayoutManager(mLayoutManager);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RequestTast().execute();
            }

        });
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);
        try {
            new RequestTast().execute();
        }
        catch (Exception er)
        {

        }

    }
    public class RequestTast extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
            prgDialog.setTitle("Retrieving List");
            prgDialog.setMessage("wait");
            prgDialog.setMax(10000);
            //prgDialog.show();
            */
        }

        @Override
        protected String doInBackground(Void... arg0) {

            return  LogicClass.getPrices(Prices.this);
        }
        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            swipeContainer.setRefreshing(false);
            Utility.shoAlert(result,Prices.this);
            if(result.startsWith("[{")) {
                items = LogicClass.getPriceList(result);
                adapter = new PriceAdapter(Prices.this,items);
                rv.setAdapter(adapter);
            }
            else {
                Utility.shoAlert(result,Prices.this);
            }


        }
    }
}
