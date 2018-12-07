package com.my.easybus;

import java.util.ArrayList;
import Entities.Found;
import Entities.Schedule;
import adapters.LostandFoundAdapter;
import adapters.ScheduleAdapter;
import adapters.SpacesItemDecoration;
import localdata.Data;
import BusinessLogic.LogicClass;
import utility.Utility;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

@SuppressLint({ "NewApi", "InlinedApi" })
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class getSchedules extends Fragment {
    private ProgressDialog prgDialog;
    String json = null, y = null;
    private ListView listview;
    private UrlEncodedFormEntity uefa;
    private ProgressBar progress;
    //private FloatingActionButton fab;
    private SwipeRefreshLayout swipeContainer;
    /////////////recycle view
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    InterstitialAd mInterstitialAd;
    RequestTast task=new RequestTast();
    String source,destination,time;
    private ScheduleAdapter adapter;
    private Bundle bundle;

    private  ArrayList<Schedule> list;
    public getSchedules() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.getschedule,
                container, false);
        prgDialog=new ProgressDialog(getActivity());
        rv = (RecyclerView)rootView.findViewById(R.id.rv);

        //rv2 = (RecyclerView)rootView.findViewById(R.id.rvhorizontal);
        swipeContainer=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        StaggeredGridLayoutManager st=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(st);
       // rv2.setLayoutManager(hr);
        SpacesItemDecoration decoration = new SpacesItemDecoration(1);
        //rv.addItemDecoration(decoration);
        //rv2.addItemDecoration(decoration);
        //swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        /*
        fab=(FloatingActionButton)rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new startBook(), "").commit();
            }
        });
*/
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
            bundle=getArguments();
            this.source=bundle.getString("source");
            this.destination=bundle.getString("destination");
            this.time=bundle.getString("time");
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
        return rootView;
    }
    @Override
    public void setMenuVisibility(final boolean visible) {
        if (visible) {

        }
        super.setMenuVisibility(visible);
    }
    public class RequestTast extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            prgDialog.setTitle("Connecting");
            prgDialog.setMessage("wait");
            prgDialog.setMax(10000);
           // prgDialog.show();
            swipeContainer.setRefreshing(true);
        }
        @Override
        protected String doInBackground(Void... arg0) {

            return  LogicClass.getSchedule(getActivity(),source,destination,time,"1");
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {

                // Utility.shoAlert(result, getActivity());
                // prgDialog.dismiss();
                swipeContainer.setRefreshing(false);
                if (result.startsWith("[{")) {
                    Data.SaveSchedules(getActivity(), source, destination, result);
                } else {
                    //Utility.shoAlert(result, getActivity());
                    // local();
                    result = Data.getSchedules(getActivity(), source, destination);
                }
                //Utility.shoAlert(result, getActivity());
                list = LogicClass.getScheduleList(getActivity(), result);
                adapter = new ScheduleAdapter(getActivity(), list);
                rv.setAdapter(adapter);
                swipeContainer.setRefreshing(false);
            }
            catch (Exception er)
            {
                swipeContainer.setRefreshing(false);
            }
        }
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }
    private void local()
    {
        String result=Data.getSchedules(getActivity(),source,destination);
        if(result.startsWith("[{")) {
           list = LogicClass.getScheduleList(getActivity(),result);
            adapter = new ScheduleAdapter(getActivity(), list);
            rv.setAdapter(adapter);
        }
    }


}