package com.my.easybus;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import BusinessLogic.LogicClass;
import Entities.Seat;
import adapters.SeatAdapter;
import localdata.Data;
import utility.Utility;

public class Details extends AppCompatActivity implements
        View.OnClickListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener{
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private EditText datebox,seat;
    private ArrayList<Seat>seats=new ArrayList<Seat>();
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rv;
    private SeatAdapter adapter;
    private  ArrayList<String>takenseats=new ArrayList<>();
    private TextView from,to,time,bus,price,type;
    private int year, month, day;
    private Calendar calendar;
    private String json="",scheduleid="",t="",busname="",bustype="",layout="";
    private Button pay,check;
    private ProgressBar progressBar;
    private Intent i;
    private ImageView imglogo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i=getIntent();
        setContentView(R.layout.details);
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
        seat= (EditText)
                findViewById(R.id.editTextseat);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        datebox=(EditText)findViewById(R.id.editTextdate);
        pay=(Button)findViewById(R.id.buttonpayment);
        check=(Button)findViewById(R.id.buttoncheck);
        imglogo=(ImageView)findViewById(R.id.imageViewcustomprofilephoto);
        pay.setOnClickListener(this);
        check.setOnClickListener(this);
        rv = (RecyclerView)findViewById(R.id.rvhistory);
        rv.setHasFixedSize(true);
       // StaggeredGridLayoutManager st=new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
        datebox.setOnClickListener(this);
        json=i.getStringExtra("json");
        String source="",destination="";
        try {
            JSONObject o = new JSONObject(json);
             source = o.getString("source");
            t = o.getString("time");
            destination = o.getString("destination");
            String rid = o.getString("RouteID");
            String  id= o.getString("RecordID");
            scheduleid=id;
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

        }
        //getResources().getString(R.string.app_name)
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(busname+" "+source+"-"+destination+" "+t);

        toolbarTextAppernce();

        //default
        mLayoutManager = new GridLayoutManager(this, 6);
        if(bustype.contains("Ordinary")) {
            mLayoutManager = new GridLayoutManager(this, 6);
        }
        else {
            mLayoutManager = new GridLayoutManager(this, 5);
        }
        rv.setLayoutManager(mLayoutManager);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }
    private void LoadSeats(ArrayList<String> taken)
    {
       // Utility.showAlert(layout,Details.this);
        seats.clear();
        if(bustype.contains("Ordinary")) {
            seats = LogicClass.getOrdinarySeats(taken);
        }
        else
        {
            //Ordinary
            if ((layout.equals("Ordinary"))|| (layout.equals("ordinary"))) {
                seats = LogicClass.getExcecutiveSeats(taken);
            }
            else
            {
                seats = LogicClass.getExcecutiveSeatsClassic(taken);
            }
        }
        adapter=new SeatAdapter(this,seats,seat,bustype,layout);
        rv.setAdapter(adapter);
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
        datebox.setText(new StringBuilder().
                append(year+"-").append(m).append("-").append(d));
        new RequestTast().execute();
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonpayment)
        {
            String s=seat.getText().toString();
            Intent i=new Intent(this,Payment.class);
            i.putExtra("json",json);
            i.putExtra("seat",s);
            i.putExtra("date",datebox.getText().toString());
            if(!s.equals("")) {
                startActivity(i);
            }
            else
            {
                Utility.showAlert("Empty Seat",this);
            }
        }
        else if(v.getId()==R.id.editTextdate)
        {
            showDateDialog();
        }
        else if(v.getId()==R.id.buttoncheck)
        {
            new RequestTast().execute();
        }
    }

    public class RequestTast extends AsyncTask<Void, Integer, String> {
        String date="";
        @Override
        protected void onPreExecute() {
            date=datebox.getText().toString();
            takenseats.clear();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... arg0) {
            return  LogicClass.getBooked(Details.this,scheduleid,date,t,busname);
        }
        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.e("taken",result);
            progressBar.setVisibility(View.GONE);
            //Utility.showAlert(result,Details.this);
            //prgDialog.dismiss();
            if(result.startsWith("[{")) {

                    takenseats = LogicClass.getTakenlist(result);

            }
            else
            {
                //all seats available
            }

                LoadSeats(takenseats);

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
            Details.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new RequestTast().execute();
    }
}
