package com.my.easybus;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import BusinessLogic.LogicClass;
import Entities.BusTime;
import Entities.Route;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import localdata.Data;
import utility.Utility;

@SuppressLint({ "NewApi", "InlinedApi" })
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class startBook extends Fragment implements View.OnClickListener,OnLocationUpdatedListener {
    private ProgressDialog prgDialog;
    InterstitialAd mInterstitialAd;
    public String source, destination, time;
    private Spinner spinnersource, spinnerdest, spinnerTime;
    private Button find;
    private ArrayAdapter<String> sourceadapter, destadapter;
    private ArrayList<String> sourcelist, destinationlist, timelist;
    private ProgressBar progressbar;
    private SpinnerAdapter sourceadapter2, destadapter2, timeadapter;
    private FloatingActionButton fab;
    public startBook() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//Utility.showAlert(Data.getEnterpriseCompanyID(getActivity()),getActivity());
        long mLocTrackingInterval = 1000 * 5; // 5 sec
        float trackingDistance = 100;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;
        //LatLng latlng=LogicClass.LatLong(getActivity());
        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance)
                .setInterval(mLocTrackingInterval);
        SmartLocation.with(getActivity())
                .location()
                .continuous()
                .config(builder.build())
                .start(this);
         View rootView = inflater.inflate(R.layout.startbook,
                container, false);
        sourcelist = new ArrayList<>();
        destinationlist = new ArrayList<>();
        timelist = new ArrayList<>();
        prgDialog = new ProgressDialog(getActivity());
        spinnersource = (Spinner) rootView.findViewById(R.id.spinnersource);
        spinnerdest = (Spinner) rootView.findViewById(R.id.spinnerdestination);
        spinnerTime = (Spinner) rootView.findViewById(R.id.spinnertime);
        fab=(FloatingActionButton)rootView.findViewById(R.id.floatingActionButton);
        progressbar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        find = (Button) rootView.findViewById(R.id.buttonsearch);
        find.setOnClickListener(this);
        fab.setOnClickListener(this);
        new RequestTast().execute();
        try {
            AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId("ca-app-pub-1548171484455270/2116532748");
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                }
            });
            mInterstitialAd.loadAd(adRequest);
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    //dont load and make it annoying
                }
            });
        } catch (Exception er) {
            Log.e("adview", er.toString());
        }
        return rootView;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        if (visible) {

        }
        super.setMenuVisibility(visible);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonsearch) {
            time = spinnerTime.getSelectedItem().toString().trim();
            source = sourcelist.get(spinnersource.getSelectedItemPosition());
            destination = destinationlist.get(spinnerdest.getSelectedItemPosition());
            if (source.equals("") && destination.equals("")) {
                source = "";
                destination = "";
            }
            Bundle bundle = new Bundle();
            bundle.putString("source", source);
            bundle.putString("destination", destination);
            getSchedules fra = new getSchedules();
            fra.setArguments(bundle);

            Intent i = new Intent(getActivity(), Schedules.class);
            i.putExtra("source", source);
            i.putExtra("destination", destination);
            i.putExtra("time", time);
            i.putExtras(bundle);
            startActivity(i);


        }

        if(v.getId()==R.id.floatingActionButton)
        {
           //start the card activity
        }
    }

    public class RequestTast extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            prgDialog.setTitle("Finding available routes");
            prgDialog.setMessage("wait");
            prgDialog.setMax(10000);
            //prgDialog.show();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... arg0) {

            return LogicClass.getRoutes(getActivity());
        }

        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Utility.showAlert(result,getActivity());
            //prgDialog.dismiss();
            progressbar.setVisibility(View.GONE);
            sourcelist.clear();
            destinationlist.clear();
            try {
                ArrayList<Route> slist = new ArrayList<>();
                ArrayList<Route> dlist = new ArrayList<>();

                ArrayList<ArrayList<Route>> list = LogicClass.getRouteList(result);
                ArrayList<BusTime> tlist = LogicClass.getTimeList(result, getActivity());
                //Utility.shoAlert("shedules "+tlist.size(), getActivity());

                if (list.size() > 0) {
                    Data.SaveData(getActivity(), result, "routes");
                    slist = list.get(0);
                    dlist = list.get(1);

                    for (int x = 0; x < slist.size(); x++) {
                        sourcelist.add(slist.get(x).source);
                    }
                    for (int x = 0; x < dlist.size(); x++) {
                        destinationlist.add(dlist.get(x).destination);
                    }

                    try {
                        for (int x = 0; x < tlist.size(); x++) {
                            timelist.add(tlist.get(x).title);
                        }
                    } catch (Exception er) {

                    }

                    timeadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, timelist);
                    sourceadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sourcelist);
                    destadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, destinationlist);
                    sourceadapter2 = new adapters.SpinnerAdapter(getActivity(), sourcelist);
                    destadapter2 = new adapters.SpinnerAdapter(getActivity(), destinationlist);

                    spinnersource.setAdapter(sourceadapter2);
                    spinnerdest.setAdapter(destadapter2);
                    spinnerTime.setAdapter(timeadapter);

                } else {
                    local();
                }
            } catch (Exception er) {
                Utility.shoAlert(er.toString(), getActivity());
            }

        }

    }

    private void local() {
        String result = Data.getLocalRoutes(getActivity());
        sourcelist.clear();
        destinationlist.clear();
        timelist.clear();
        try {
            ArrayList<Route> slist = new ArrayList<>();
            ArrayList<Route> dlist = new ArrayList<>();

            ArrayList<ArrayList<Route>> list = LogicClass.getRouteList(result);
            ArrayList<BusTime> tlist = LogicClass.getTimeList(result, getActivity());
            //Utility.shoAlert("shedules "+tlist.size(), getActivity());

            if (list.size() > 0) {
                Data.SaveData(getActivity(), result, "routes");
                slist = list.get(0);
                dlist = list.get(1);

                for (int x = 0; x < slist.size(); x++) {
                    sourcelist.add(slist.get(x).source);
                }
                for (int x = 0; x < dlist.size(); x++) {
                    destinationlist.add(dlist.get(x).destination);
                }

                try {
                    for (int x = 0; x < tlist.size(); x++) {
                        timelist.add(tlist.get(x).title);
                    }
                } catch (Exception er) {

                }

                timeadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, timelist);
                sourceadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sourcelist);
                destadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, destinationlist);
                sourceadapter2 = new adapters.SpinnerAdapter(getActivity(), sourcelist);
                destadapter2 = new adapters.SpinnerAdapter(getActivity(), destinationlist);

                spinnersource.setAdapter(sourceadapter2);
                spinnerdest.setAdapter(destadapter2);
                spinnerTime.setAdapter(timeadapter);

            }
        } catch (Exception er) {

        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        LatLng latlng=new LatLng(location.getLatitude(),location.getLongitude());
       //Utility.showAlert(latlng.latitude+" long "+latlng.longitude,getActivity());

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        SmartLocation.with(getActivity()).location().stop();
    }
}