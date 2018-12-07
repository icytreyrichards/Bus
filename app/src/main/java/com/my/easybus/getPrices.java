package com.my.easybus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.util.ArrayList;

import BusinessLogic.LogicClass;
import Entities.Price;
import adapters.PriceAdapter;
import localdata.Data;
import utility.Utility;
public class getPrices extends Fragment {
    private ProgressDialog prgDialog;
    String json = null, y = null;
    private UrlEncodedFormEntity uefa;
    private ProgressBar progress;
    private PriceAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private Intent i;
    ArrayList<Price> items;

    /////////////recycle view
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.prices,
                container, false);
        prgDialog=new ProgressDialog(getActivity());
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        rv = (RecyclerView)rootView.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        //rv.setLayoutManager(llm);
        mLayoutManager = new GridLayoutManager(getActivity(),1);
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

        return rootView;
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
            return  LogicClass.getPrices(getActivity());
        }
        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Utility.shoAlert(result,getActivity());
            swipeContainer.setRefreshing(false);
            if(result.startsWith("[{")) {
                items = LogicClass.getPriceList(result);
                Data.SaveData(getActivity(),result,"prices");
                adapter = new PriceAdapter(getActivity(),items);
                rv.setAdapter(adapter);
            }
            else {
                //Utility.shoAlert(result,getActivity());
            }


        }
    }
}
