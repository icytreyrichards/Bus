package com.my.easybus;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import BusinessLogic.GoogleMapsTasks;
import BusinessLogic.LogicClass;
import Entities.Pickups;
import Entities.RouteData;
import encrypt.Vendor;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import localdata.Data;
import map.*;
import map.DirectionsJSONParser;
import payment.PostData;
import utility.Utility;

public class Payment extends AppCompatActivity implements View.OnClickListener,
        OnLocationUpdatedListener {
    private TextView from, to, time, bus, seatno, ordinary, price, type,txtCost,txtVat,tvTotal,tvticket;
    private Spinner spinnerpayment, spinnerpickup;
    private Button book, reserve;
    Intent i;
    private String seat, date, pickup, payment, schedule, route, cost = "", busname, times, busid = "", bustype = "", cnametext = "", cteltext = "";
    private ProgressBar progressbar;
    private ArrayAdapter<String> paymentadapter, pickupadapter;
    private ArrayList<String> paymentlist;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private EditText cname, ctel;
    private String BookingMethod = "1", role = "5";
    private ArrayList<Pickups> items;
    private ArrayList<String> names;
    private LatLng origin,kla;
    private ImageView imglogo;
    private int charge=2000,vat=0,sub_total,ticket_cost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StartGPS();
        kla=new LatLng(0.3476,32.5825);
        i = getIntent();
        paymentlist = new ArrayList<>();
        setContentView(R.layout.payments);
        from = (TextView)
                findViewById(R.id.textViewcustomsource);
        to = (TextView)
                findViewById(R.id.textViewcustomdestination);
        time = (TextView)
                findViewById(R.id.textViewcustomtime);
        bus = (TextView)
                findViewById(R.id.textViewcustomcompany);
        price = (TextView)
                findViewById(R.id.textViewcustomprice);
        type = (TextView)
                findViewById(R.id.textViewcustomtype);
        seatno = (TextView)
                findViewById(R.id.textViewseatno);
        txtCost = (TextView)
                findViewById(R.id.txtCost);
        txtVat = (TextView)
                findViewById(R.id.txtVat);
        tvTotal = (TextView)
                findViewById(R.id.txtTotal);
        tvticket = (TextView)
                findViewById(R.id.txtcostt);
        imglogo=(ImageView)findViewById(R.id.imageViewcustomprofilephoto);

        book = (Button) findViewById(R.id.buttonbook);
        reserve = (Button) findViewById(R.id.buttonreserve);
        ordinary = (TextView)
                findViewById(R.id.textViewtype);
        spinnerpayment = (Spinner) findViewById(R.id.spinnerpayment);
        spinnerpickup = (Spinner) findViewById(R.id.spinnerpickups);
        progressbar = (ProgressBar) findViewById(R.id.progressBar1);
        cname = (EditText) findViewById(R.id.editTextcontactname);
        ctel = (EditText) findViewById(R.id.editTextcontacttelephone);
        String json = i.getStringExtra("json");
        seat = i.getStringExtra("seat");
        date = i.getStringExtra("date");
        seatno.setText("Seat #" + seat);
        Utility.showAlert(json,this);
        role = Data.role(this).trim();
        if (role.equals("5")) {
            reserve.setVisibility(View.GONE);
        }
        String roleid = role;
        if (roleid.equals("1") || roleid.equals("2")) {
            reserve.setVisibility(View.VISIBLE);
        }

        String pickups = "", loc = "", source = "", destination = "";
        try {
            JSONObject o = new JSONObject(json);
            String photo=Data.server+"manager/"+o.getString("logo");
            source = o.getString("source");
            String t = o.getString("time");
            times = t;
            destination = o.getString("destination");
            route = o.getString("RouteID");
            schedule = o.getString("RecordID");
            String b = o.getString("CompanyName");
            busname = o.getString("CompanyName");
            busid = o.getString("CompanyID");
            loc = o.getString("location");
            pickups = o.getString("pickups");
            String p = o.getString("price");
            ticket_cost=Integer.parseInt(p);
            bustype = o.getString("ProductName");
            cost = p;
            from.setText("From " + source);
            to.setText("To " + destination);
            time.setText("Departure Time " + t);
            price.setText(p + " shs");
            bus.setText(b);
            type.setText(bustype);
            Picasso.with(this).load(photo)
                    .error(R.drawable.logo).fit()
                    .into(imglogo);
        } catch (JSONException er) {
            Utility.showAlert(json+er.toString(), this);
        }
        cname.setText(Data.fullname(this));
        ctel.setText(Data.telephone(this));

        //getResources().getString(R.string.app_name)
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(busname + " " + source + "-" + destination);
        collapsingToolbarLayout.setTitle(busname + " " + source + "-" + destination + " " + times);
        toolbarTextAppernce();
        paymentlist.add("Cash on Delivery");
        paymentlist.add("Mobile Money");
        Log.e("pickups json", pickups);
        items = LogicClass.getPickups(pickups);
        names = LogicClass.getPickupsList(this, items);
        if (items.size() == 0) {
            names.add(busname + " Main Office");
        }
        paymentadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, paymentlist);
        pickupadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        spinnerpayment.setAdapter(paymentadapter);
        spinnerpickup.setAdapter(pickupadapter);
        book.setOnClickListener(this);
        reserve.setOnClickListener(this);
        String role = Data.getEnterpriseRole(this).trim();
        if (!role.equals("5")) {
            reserve.setVisibility(View.VISIBLE);
        } else {
            reserve.setVisibility(View.GONE);
        }
        vat=(18*charge)/100;
        sub_total=charge+vat+ticket_cost;
        txtCost.setText(charge+" Shs Service Charge");
        txtVat.setText(vat+" Shs Vat");
        tvticket.setText((ticket_cost)+" Shs Rate");
        tvTotal.setText((sub_total)+" Shs Total Charge");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonbook) {
            try {
                seat = seatno.getText().toString().split("#")[1];
                payment = spinnerpayment.getSelectedItem().toString();
                //pickup = LogicClass.JSON(items.get(spinnerpickup.getSelectedItemPosition()).json).getString("pickupID");
                pickup = spinnerpickup.getSelectedItem().toString();
                cnametext = cname.getText().toString();
                cteltext = ctel.getText().toString();
                RequestTast task = new RequestTast();
                task.execute();
            } catch (Exception er) {

            }
        } else if (v.getId() == R.id.buttonreserve) {
            BookingMethod = "2";
            seat = seatno.getText().toString().split("#")[1];
            payment = spinnerpayment.getSelectedItem().toString();
            pickup = spinnerpickup.getSelectedItem().toString();
            cnametext = cname.getText().toString();
            cteltext = ctel.getText().toString();

            RequestTast task = new RequestTast();
            task.execute();

            //cost
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
            Log.e("bus", busid);
            Log.e("cost", cost);
            return LogicClass.
                    BookNow(Payment.this, route, schedule, seat,
                            date, payment, pickup, busname, cost, times, busid, cnametext, cteltext,
                            BookingMethod,charge,vat,sub_total);
        }
        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressbar.setVisibility(View.GONE);
            //prgDialog.dismiss();
            book.setEnabled(false);
           if(!result.contains(" sorry an error")) {
               try {
                   JSONObject o = new JSONObject(result);
                   String VendorTranId = o.getString("receipt");
                   String TicketRef = o.getString("TicketRef");
                   String BookRef = o.getString("BookRef");

                   Utility.shoAlert(o.getString("title"), Payment.this);
                   if (spinnerpayment.getSelectedItemPosition() == 1) {
                       Vendor vendor = new Vendor();
                       //post to the payment api here
                       PostData post = new PostData(Payment.this, progressbar, charge + "", bustype + " Ticket " + busname,
                               VendorTranId);
                       //post.startPost();

                   }
               } catch (Exception er) {

               }
           }
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
    public void onLocationUpdated(Location location) {
        origin = new LatLng(location.getLatitude(), location.getLongitude());
        double distance=LogicClass.CalculationByDistance(origin,kla);
        //start google task here
        if(distance<10)
        {
            charge=1000;
        }
        else if(distance<5)
        {
            charge=1000;
        }
        else  if(distance>10)
        {
            charge=1000;
        }
        else  if(distance>20)
        {
            charge=1000;
        }
        else
        {
            charge=1000;
        }
        vat=(18*charge)/100;
        vat=0;
        sub_total=charge+vat+ticket_cost;
        txtCost.setText(charge+" Shs Service Charge");
        txtVat.setText(vat+" Shs Vat");
        tvTotal.setText((sub_total)+" Shs Total Charge");
        String url = GoogleMapsTasks.getDirectionsUrl(origin, kla);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
        //Utility.shoAlert(cost+"",this);
    }
    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                map.DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";



            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) { // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);

                }
              //  Utility.shoAlert(distance,Payment.this);



            }
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = GoogleMapsTasks.downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    protected void StartGPS() {
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

}