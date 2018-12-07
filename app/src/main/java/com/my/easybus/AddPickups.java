package com.my.easybus;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import BusinessLogic.LogicClass;
import Entities.RouteData;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import localdata.Data;
import utility.Utility;

public class AddPickups extends AppCompatActivity implements View.OnClickListener,OnLocationUpdatedListener {
    private Spinner spinnerroute;
    private Button book;
    private ProgressBar progressbar;
    private ArrayAdapter<String>pickupadapter;
    private ArrayList<String>pickuplist;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private EditText cname;
    private String name="",latitude,longitude,routeid;
    private ArrayList<RouteData>items;
    private ArrayList<String>names;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartGPS();
        pickuplist=new ArrayList<>();
        setContentView(R.layout.addpickup);
        spinnerroute=(Spinner) findViewById(R.id.spinnerpickups);
        progressbar=(ProgressBar)findViewById(R.id.progressBar1);
        book=(Button)findViewById(R.id.buttonbook);
        cname=(EditText)findViewById(R.id.editTextcontactname) ;
         collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Pick Ups");
         toolbarTextAppernce();
        pickupadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pickuplist);
        spinnerroute.setAdapter(pickupadapter);
        book.setOnClickListener(this);
        String role=Data.getEnterpriseRole(this).trim();
new RequestData().execute();
    }

    @Override
    public void onClick(View v) {
if(v.getId()==R.id.buttonbook)
{

    try
    {
        name=cname.getText().toString();
        routeid=LogicClass.JSON (items.get(spinnerroute.getSelectedItemPosition()).json).getString("RouteID");
        RequestTast task=new RequestTast();
        task.execute();
    }
    catch (Exception er)
    {

    }
}
    }
    public class RequestTast extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... arg0) {

            return  LogicClass.addPickup(AddPickups.this,name,latitude,longitude,routeid);
        }
        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressbar.setVisibility(View.GONE);
            //prgDialog.dismiss();
            Utility.shoAlert(result,AddPickups.this);
            //book.setEnabled(false);
        }
    }

    private void dynamicToolbarColor() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark));
            }
        });
    }

    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }
    public class RequestData extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressbar.setVisibility(View.VISIBLE);
            String result=Data.getData(AddPickups.this,"pickups");
            FillRoutes(result,true);
        }
        @Override
        protected String doInBackground(Void... arg0) {

            return  LogicClass.getCompanyRoutes(AddPickups.this);
        }
        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
           // Utility.showAlert(result,AddPickups.this);
            progressbar.setVisibility(View.GONE);
            FillRoutes(result,true);

        }
    }
protected void FillRoutes(String result,Boolean first)
{
    items=LogicClass.getRoutesList(result);
    if(items.size()>0)
    {
        if(first) {
            Data.SaveData(AddPickups.this, result, "pickups");
        }
    }
    names=LogicClass.getRoutesNames(AddPickups.this,items);
    pickupadapter=new ArrayAdapter<String>(AddPickups.this,android.R.layout.simple_spinner_dropdown_item,names);
    spinnerroute.setAdapter(pickupadapter);
}
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        SmartLocation.with(this).location().stop();
    }

    @Override
    public void onLocationUpdated(Location location) {
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        //Utility.Toast(Order.this,latlng.latitude+" long "+latlng.longitude);
        latitude = String.valueOf(latlng.latitude);
        longitude = String.valueOf(latlng.longitude);
    }

    protected void StartGPS() {
        TurnGPS();
        long mLocTrackingInterval = 1000 * 5; // 5 sec
        float trackingDistance = 0;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance)
                .setInterval(mLocTrackingInterval);

        SmartLocation.with(this)
                .location()
                .continuous()
                .config(builder.build())
                .start(this);
    }

    void TurnGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("GPS REQUIRED")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
