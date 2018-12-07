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
import android.widget.ListView;
import android.widget.ProgressBar;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.util.ArrayList;

import Entities.Found;
import Entities.Schedule;
import BusinessLogic.LogicClass;
import adapters.ScheduleAdapter;
import utility.Utility;

/**
 * Created by Richard.Ezama on 26/04/2016.
 */
public class SearchRoutes extends AppCompatActivity {
    private ProgressDialog prgDialog;
    String json = null, y = null;
    private UrlEncodedFormEntity uefa;
    private ProgressBar progress;
    private ScheduleAdapter adapter;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeContainer;
    private Intent i;
    private String search;
    private Toolbar toolbar;
    ArrayList<Schedule> items;


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
        toolbar.setTitle("Searching.....");
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
        toolbar.setNavigationIcon(R.drawable.arrowleft);
        i=getIntent();
        search=i.getStringExtra("search");
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
                new RequestTast().execute(search);
            }

        });
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);
        try {
            new RequestTast().execute(search);
        }
        catch (Exception er)
        {

        }

    }
    public class RequestTast extends AsyncTask<String, Integer, String> {
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
        protected String doInBackground(String... arg0) {
            return  LogicClass.SearchRoutes(SearchRoutes.this,arg0[0]);
        }
        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                swipeContainer.setRefreshing(false);
                if (result.startsWith("[{")) {
                    items = LogicClass.getScheduleList(SearchRoutes.this, result);
                    adapter = new ScheduleAdapter(SearchRoutes.this, items);
                    //Utility.shoAlert(result+list.size(), getActivity());
                } else {
                    Utility.shoAlert("", SearchRoutes.this);
                }
                rv.setAdapter(adapter);
                toolbar.setTitle(items.size() + " Buses Found For " + search);
            }
            catch (Exception er)
            {
                swipeContainer.setRefreshing(false);
            }
        }
    }
}
