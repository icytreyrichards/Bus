package com.my.easybus;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import BusinessLogic.LogicClass;
import Entities.Seat;
import adapters.SeatAdapter;
import localdata.Data;
import track.TrackNow;
import utility.Utility;
public class DetailsDriver extends AppCompatActivity implements
        View.OnClickListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener,OnMapReadyCallback {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView from,to,time,bus,price,type,tripDetails;
    private int year, month, day,starting=1;
    private Calendar calendar;
    private String json="",scheduleid="",t="",busname="",bustype="",layout="";
    private Button pay,check,buttonnotify;
    private ProgressBar progressBar;
    private Intent i;
    private ImageView imglogo;
    private EditText notifyText;
    private Boolean cancamera = false;
    private GoogleMap map;
    private LatLng latlng=new LatLng(0,0);
    private String location,snippet;
    private SupportMapFragment fm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i=getIntent();
        setContentView(R.layout.detailsdrivers);
        setMap();
         from = (TextView)
                findViewById(R.id.textViewcustomsource);
        to= (TextView)
                findViewById(R.id.textViewcustomdestination);
        time= (TextView)
                findViewById(R.id.textViewcustomtime);
        bus= (TextView)
                findViewById(R.id.textViewcustomcompany);
        price= (TextView)
                findViewById(R.id.textViewcustomprice);
        type= (TextView)
                findViewById(R.id.textViewcustomtype);
        tripDetails=(TextView)findViewById(R.id.tripDetails);
        notifyText=(EditText)findViewById(R.id.notifyText);
        buttonnotify=(Button)findViewById(R.id.buttonnotify);
        buttonnotify.setOnClickListener(this);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        pay=(Button)findViewById(R.id.buttonpayment);
        check=(Button)findViewById(R.id.buttoncheck);
        imglogo=(ImageView)findViewById(R.id.imageViewcustomprofilephoto);
        pay.setOnClickListener(this);
        check.setOnClickListener(this);
        json=i.getStringExtra("json");
        String source="",destination="";
        try {
            JSONObject o = new JSONObject(json);
            String start=o.getString("TPStat");
            //Utility.shoAlert(start+"  "+json,this);
            if(start.equals("1"))
            {
                pay.setText("End Trip");
               // starting=1;
            }
            if(start.equals("0"))
            {
                pay.setVisibility(View.GONE);
                buttonnotify.setVisibility(View.GONE);
                notifyText.setVisibility(View.GONE);
                //tripDetails
                String company=o.getString("CompanyName");
                String route=o.getString("source")+"-"+o.getString("destination");
                String time=o.getString("time");
                String title=company+" departure time "+time;
                String stampTrip=o.getString("stampTrip");
                String stamp=o.getString("endstamp");
                String line=title+"\n"+" Trip Started "+stampTrip+" Trip Ended "+stamp;
                String lat=o.getString("latitude");
                String lng=o.getString("longitude");
                tripDetails.setText(line);
                location=line;
                snippet=route;
                latlng=new LatLng(Double.valueOf(lat),Double.valueOf(lng));
            }
            source = o.getString("source");
            t = o.getString("time");
            destination = o.getString("destination");
            String rid = o.getString("RouteID");
            scheduleid=o.getString("ScheduleID");

            String  b= o.getString("CompanyName");
            bustype=o.getString("ProductName");
            layout=o.getString("layout");
            String photo= Data.server+"manager/"+o.getString("logo");

            Picasso.with(this).load(photo)
                    .error(R.drawable.logo).fit()
                    .into(imglogo);

            busname=b;
            String  p= o.getString("price");
            String  pickups= o.getString("pickups");
            //Utility.showAlert(pickups,this);
            from.setText("From "+source);
            to.setText("To "+destination);
            time.setText("Departure Time "+t);
            price.setText(p+" shs");
            bus.setText(b);
            type.setText(bustype);
        }
        catch (JSONException er)
        {
                     Utility.shoAlert(er.toString(),this);
        }
        //getResources().getString(R.string.app_name)
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(busname+" "+source+"-"+destination+" "+t);

        toolbarTextAppernce();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }
    void setMap()
    {
        if (!isGooglePlayServicesAvailable()) {

        } else {
            MarkerOptions options = new MarkerOptions();
               fm = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            // Getting Map for the SupportMapFragment
            fm.getMapAsync(this);
        }
    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog
                                      view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        showDate(year,monthOfYear+1,dayOfMonth);
    }
    void showDateDialog()
    {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }
    private void showDate(int year, int month, int day) {

        String m=month+"",d=day+"";
        if(month <10)
        {
            m="0"+month;
        }

        if (day <10)
        {
          d="0"+day;
        }

    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonpayment)
        {
          new StartTast().execute();
        }
        else  if(v.getId()==R.id.buttonnotify)
        {
            new NotifyTssk().execute();
        }

    }
    public class StartTast extends AsyncTask<Void, Integer, String> {
        String date="";
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... arg0) {
            return  LogicClass.startJourney(DetailsDriver.this,scheduleid,starting);
        }
        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            starting=1;
            pay.setText("End Trip");
            super.onPostExecute(result);
            Log.e("Journey Started",result);
            progressBar.setVisibility(View.GONE);
            if(result.contains("{")) {
                try {
                    JSONObject o = new JSONObject(result);
                    String trip = o.getString("TripID");
                    String msg = o.getString("msg");
                    Data.startTrip(DetailsDriver.this, scheduleid, trip);
                    Utility.shoAlert(msg,DetailsDriver.this);
                }
                catch (Exception er)
                {

                }
            }
            else
            {
                Utility.shoAlert(result,DetailsDriver.this);
            }
        }

    }
    public class NotifyTssk extends AsyncTask<Void, Integer, String> {
        String message="";
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            message=notifyText.getText().toString();
        }
        @Override
        protected String doInBackground(Void... arg0) {
            return  LogicClass.sendMessage(DetailsDriver.this,scheduleid,message);
        }
        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            Utility.shoAlert(result,DetailsDriver.this);
            super.onPostExecute(result);
            Log.e("Journey Started",result);
            progressBar.setVisibility(View.GONE);


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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.closemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_close) {
            DetailsDriver.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                //Utility.Toast(Request.this,"moving");
                cancamera = true;
                //SetMap(mapdata, 5000);
            }

        });
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //do something
                cancamera=false;
            }
        });
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });
        map.setMyLocationEnabled(true);
        drawCircle(latlng);
        MarkerOptions options = new MarkerOptions();
        options.title(location);
        options.snippet(snippet);
        options.position(latlng);
        Circle circle = map.addCircle(new CircleOptions()
                .center(latlng)
                .radius(1000) // Converting Miles into Meters...
                .strokeColor(Color.RED)
                .strokeWidth(2));
        // Add new marker to the Google Map Android API V2
        if (isInBounds(circle, latlng)) {
            options.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        } else {
            options.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        map.addMarker(options);

    }
    Boolean isInBounds(Circle circle, LatLng latlong) {
        Boolean res = false;
        float[] distance = new float[2];

        Location.distanceBetween(latlong.latitude, latlong.longitude, circle.getCenter().latitude, circle.getCenter().longitude, distance);

        if (distance[0] <= circle.getRadius()) {
            res = true;
        } else {
            res = false;
        }
        return res;
    }
    private void drawCircle(LatLng point) {

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(20);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        map.addCircle(circleOptions);

    }
}
