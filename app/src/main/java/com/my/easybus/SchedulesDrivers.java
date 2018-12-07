package com.my.easybus;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.util.ArrayList;

import BusinessLogic.LogicClass;
import Entities.Schedule;
import adapters.ScheduleAdapter;
import adapters.ScheduleAdapterDrivers;
import adapters.SpacesItemDecoration;
import localdata.Data;

@SuppressLint({ "NewApi", "InlinedApi" })
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class SchedulesDrivers extends AppCompatActivity {
    private ProgressDialog prgDialog;
    String json = null, y = null;
    private ListView list;
    private UrlEncodedFormEntity uefa;
    private ProgressBar progress;
    //private FloatingActionButton fab;
    private SwipeRefreshLayout swipeContainer;
    /////////////recycle view
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayoutManager llm;
    InterstitialAd mInterstitialAd;
    RequestTast task=new RequestTast();
    String source,destination,time;
    private RecyclerView rv;
    private ScheduleAdapterDrivers adapter;
    private Bundle bundle;
    private Toolbar toolbar;
    public SchedulesDrivers() {
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        prgDialog=new ProgressDialog(SchedulesDrivers.this);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrowleft);
        toolbar.inflateMenu(R.menu.closemenu);
        rv = (RecyclerView)findViewById(R.id.rv);
        swipeContainer=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(SchedulesDrivers.this);
        mLayoutManager = new GridLayoutManager(SchedulesDrivers.this, 1);
        StaggeredGridLayoutManager st=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(st);
        SpacesItemDecoration decoration = new SpacesItemDecoration(1);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new  RequestTast().execute();
            }

        });
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);

        try {
            bundle=getIntent().getExtras();
            this.source=bundle.getString("source");
            this.destination=bundle.getString("destination");
            this.time=bundle.getString("time");
            toolbar.setTitle("Searching "+source+"-"+destination);
            new  RequestTast().execute();
            /*
            AdView mAdView = (AdView)rootView. findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            */
        }
        catch (Exception er)
        {
            Log.e("adview",er.toString());
        }
    }

    public class RequestTast extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            prgDialog.setTitle("Connecting");
            prgDialog.setMessage("wait");
            prgDialog.setMax(10000);
            prgDialog.show();
            toolbar.setTitle(source+" to "+destination);
        }
        @Override
        protected String doInBackground(Void... arg0) {
            return  LogicClass.getSchedule(SchedulesDrivers.this,source,destination,time,"1");
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
          //  Utility.shoAlert(result,Schedules.this);
            super.onPostExecute(result);
            prgDialog.dismiss();
            swipeContainer.setRefreshing(false);
            if(result.startsWith("[{")) {
                ArrayList<Schedule> list = LogicClass.getScheduleList(SchedulesDrivers.this, result);
                Data.SaveSchedules(SchedulesDrivers.this,result,source,destination);
                adapter = new ScheduleAdapterDrivers(SchedulesDrivers.this, list);
                rv.setAdapter(adapter);
                //Utility.shoAlert(result+list.size(), getActivity());
            }
            else {
               // Utility.shoAlert(result, Schedules.this);
                local();
            }
        }
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void local()
    {
        String result=Data.getSchedules(SchedulesDrivers.this,source,destination);
        if(result.startsWith("[{")) {
            ArrayList<Schedule> list = LogicClass.getScheduleList(SchedulesDrivers.this,result);
            adapter = new ScheduleAdapterDrivers(SchedulesDrivers.this, list);
            rv.setAdapter(adapter);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.closemenu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == android.R.id.home) {
            finish();

        }
        else if (id == R.id.action_cancel) {
            SchedulesDrivers.this.finish();
        }
         return super.onOptionsItemSelected(menuItem);
    }
}