package com.my.easybus;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import BusinessLogic.LogicClass;
import Entities.Ticket;
import adapters.SpacesItemDecoration;
import adapters.TicketAdapter;
import localdata.Data;
import utility.Utility;

@SuppressLint({ "NewApi", "InlinedApi" })
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class SearchTickets extends AppCompatActivity {
    private ProgressDialog prgDialog;
    ArrayList<Ticket> items = new ArrayList<>();
    String json = null, y = null,search="";
    private ListView list;
    private UrlEncodedFormEntity uefa;
    private ProgressBar progress;
    //private FloatingActionButton fab;
    private SwipeRefreshLayout swipeContainer;
    /////////////recycle view
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private TicketAdapter rvadapter ;
    InterstitialAd mInterstitialAd;
    RequestTast task=new RequestTast();
    Intent i;
    String status="0";
    public SearchTickets() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getschedule);
        prgDialog=new ProgressDialog(SearchTickets.this);
        i=getIntent();
        search=i.getStringExtra("search");
        rv = (RecyclerView)findViewById(R.id.rv);
        //rv2 = (RecyclerView)rootView.findViewById(R.id.rvhorizontal);
        swipeContainer=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(SearchTickets.this);
        mLayoutManager = new GridLayoutManager(SearchTickets.this, 1);
        StaggeredGridLayoutManager st=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager hr=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rv.setLayoutManager(st);
        // rv2.setLayoutManager(hr);
        SpacesItemDecoration decoration = new SpacesItemDecoration(1);
        rv.addItemDecoration(decoration);
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

                new RequestTast().execute();

            }

        });
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);

        try {
            task.execute();
            AdView mAdView = (AdView)findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);}
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
            prgDialog.setTitle("Loading Tickets..");
            prgDialog.setMessage("please wait");
            prgDialog.setMax(10000);
            // prgDialog.show();
            //swipeContainer.setRefreshing(true);

        }
        @Override
        protected String doInBackground(Void... arg0) {
            String res=LogicClass.getCompanyTickets(SearchTickets.this,"0",search);
            Log.e("server tickets",res);
            return res;
        }
        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Utility.showAlert(result,SearchTickets.this);
            // prgDialog.dismiss();
            // items=LogicClass.getTicketList(result);
            if(!result.startsWith("[{"))
            {
                //result=Data.getData(SearchTickets.this,"tickets");
            }
            Load(result);
            swipeContainer.setRefreshing(false);
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }
    protected void Load(String result)
    {
        items.clear();
        items=LogicClass.getTicketList(result);

        //Utility.showAlert(result,SearchTickets.this);
        if(items.size()==0)
        {
            Toast.makeText(SearchTickets.this,"No Pending Tickets" ,Toast.LENGTH_LONG).show();
            //result=Data.getData(SearchTickets.this,"tickets");
        }
        else {
            Data.SaveData(SearchTickets.this,result,"tickets");
        }
        rvadapter = new TicketAdapter(SearchTickets.this, items);
        rv.setAdapter(rvadapter);
    }
}
