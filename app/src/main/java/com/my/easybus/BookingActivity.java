package com.my.easybus;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.client.android.CaptureActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import BusinessLogic.LogicClass;
import accelerometer.AccelerometerListener;
import accelerometer.AccelerometerManager;
import localdata.DBhelper;
import localdata.Data;
import utility.Utility;

public class BookingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AccelerometerListener {
    private ImageView profilepic;
    private TextView title;
    private  String regId;
    MaterialSearchView searchView;
    private Bundle bundle=new Bundle();
    private ProgressDialog prgDialog;
    private   Toolbar toolbar;
    private  Intent s=new Intent(BookingActivity.this,SearchTickets.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_process);
        //request
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            requestCamera();
        }
        //Utility.showAlert("done",this);
        prgDialog=new ProgressDialog(this);
        try {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view =navigationView.getHeaderView(0);
            Menu menu = navigationView.getMenu();
            MenuItem myaccount = menu.findItem(R.id.nav_account);
            if(!Data.is_LocalRecord(this))
            {
                myaccount.setVisible(false);
            }

        profilepic=(ImageView)view.findViewById(R.id.imageViewprofilepic);
        navigationView.setNavigationItemSelectedListener(this);
           new GCMTast().execute();
        }
        catch (Exception er)
        {

        }
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String search) {
                //Do some magic

                AlertDialog.Builder alert=new AlertDialog.Builder(BookingActivity.this);
                alert.setPositiveButton("Search Routes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s=new Intent(BookingActivity.this,SearchRoutes.class);
                        s.putExtra("search",search);
                      //  s.putExtra("",)
                        startActivity(s);
                    }
                });
                alert.setNeutralButton("Search Tickets", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s=new Intent(BookingActivity.this,SearchTickets.class);
                        s.putExtra("search",search);
                        startActivity(s);
                    }
                });
                alert.show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });


        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_booking, menu);
/*
        mSearchView = (SearchView) menu.findItem(R.id.menu_search)
                .getActionView();
                */
        //setupSearchView();
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setVoiceSearch(true); //or false

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new HomeProcess(), "").commit();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Bundle bundle=new Bundle();
            bundle.putString("source","");
            bundle.putString("destination","");
            getSchedules fra=new getSchedules();
            fra.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fra, "").commit();
            return true;
        }
        else if (id == R.id.action_info) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new MiniProfile(), "").commit();
            return true;
        }
        else if (id == R.id.action_update) {
            Intent i=new Intent(BookingActivity.this,UpdateProfile.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_routes) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new startBook(), "").commit();
            return true;
        }
        else if (id == R.id.action_scan) {
            Intent intent = new Intent(getApplicationContext(),CaptureActivity.class);
            intent.setAction("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new HomeProcess(), "").commit();
        }
        else  if (id == R.id.nav_book) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new startBook(), "").commit();
        }
        else if (id == R.id.nav_addpickups) {
            Intent i=new Intent(BookingActivity.this,AddPickups.class);
            startActivity(i);
            return true;
        }
        else  if (id == R.id.nav_tickets) {
            ViewTickets acc=new ViewTickets();
            Bundle b=new Bundle();
            //pending
            b.putString("status","1");
            b.putString("search","");
            acc.setArguments(b);
            getSupportFragmentManager().beginTransaction();
        }
        else  if (id == R.id.nav_payments) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new getPrices(), "").commit();
        }

        else if (id == R.id.nav_favourites) {
//get my recent schedules

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new getFavourites(), "").commit();
        }
        else if (id == R.id.nav_logout) {
            DBhelper help = new DBhelper(this);
            if(Data.is_LocalRecord(this)) {
                SQLiteDatabase db = help.getReadableDatabase();
                Utility.Logout(this);
            }
            finish();
        }
        if(Data.is_LocalRecord(this)) {
             if (id == R.id.nav_account) {
                 /*
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new ProfileFragment(), "").commit();
                        */
                 Intent i=new Intent(this,Profile.class);
                 startActivity(i);
            }
        }
        else if (id == R.id.nav_settings) {

            Intent i=new Intent(this,Password.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupSearchView() {
        //mSearchView.setIconifiedByDefault(true);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager
                    .getSearchablesInGlobalSearch();

            // Try to use the "applications" global search provider
            SearchableInfo info = searchManager
                    .getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            //mSearchView.setSearchableInfo(info);
        }
        //mSearchView.setOnQueryTextListener(this);
        //mSearchView.setOnCloseListener(this);
    }
    public void onShake(float force) {
        //notify
       /* Toast.makeText(getBaseContext(), "Motion detected phone droped",
                Toast.LENGTH_SHORT).show();
        //Log.e("gcm result",LogicClass.SendGCM(regId,"Suspicious Movement Detected","un usual motion detected",Data.fullname(this),"motion"));
        */
    }
    public void onAccelerationChanged(float x, float y, float z) {

    }
    @Override
    public void onResume() {
        super.onResume();
        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isSupported(this)) {

            //Start Accelerometer Listening
            AccelerometerManager.startListening(this);
        }
    }
    @Override
    public void onStop() {
        super.onStop();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();


        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Sensor", "Service  distroy");

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();

            Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        else if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                new RequestTastPost().execute(contents);
                Log.d("scan data", "contents: " + contents);
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("scan data", "RESULT_CANCELED");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public class GCMTast extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... arg0) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            return LogicClass.FCM(BookingActivity.this,refreshedToken);
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Log.e("gcm result", LogicClass.SendGCM(regId,"Welcome How are you"));
           //Utility.showAlert(result,BookingActivity.this);
        }
    }

    public class RequestTastPost extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            prgDialog.setTitle("Loading Tickets..");
            prgDialog.setMessage("please wait");
            prgDialog.setMax(10000);
            prgDialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {
            return  LogicClass.getTicketDetails(BookingActivity.this,arg0[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Utility.showAlert(result,BookingActivity.this);
            /*
            //get the json
            try {
               ArrayList<Ticket> ticket = LogicClass.getTicketList(result);
                Intent i=new Intent(BookingActivity.this, ViewTicketDetails.class);
                if(ticket.size()>0) {
                    i.putExtra("json", ticket.get(0).json);
                    i.putExtra("ticket", ticket.get(0).ticketid);
                   startActivity(i);
                }
                else
                {
                       Utility.showAlert(result,BookingActivity.this);
                }
            }
            catch (Exception e)
            {

            }
            */
        }
    }
    void requestCamera() {

        //only for higher version
        String[] perms = {"android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.ACCESS_FINE_LOCATION","android.permission.CAMERA",
                "android.permission.CALL_PHONE"};

        int permsRequestCode = 200;

        ActivityCompat.requestPermissions(this, perms, permsRequestCode);
    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {


        switch (permsRequestCode) {

            case 200:
                if (grantResults.length > 0) {
                    boolean islocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (islocation) {
                        //camera allowed
                    }
                } else {
                    //requestLocation();
                }
                //Utility.showAlert(" read "+read +" write "+write,Login.this);

                break;

        }
    }
}
