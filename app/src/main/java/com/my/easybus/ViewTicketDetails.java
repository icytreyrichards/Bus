package com.my.easybus;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.CaptureActivity;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.util.ArrayList;

import BusinessLogic.CodeGenerator;
import BusinessLogic.LogicClass;
import Entities.Status;
import Entities.Ticket;
import adapters.SpacesItemDecoration;
import adapters.TicketAdapter;
import localdata.Data;
import okhttp3.internal.Util;
import utility.Utility;

@SuppressLint({ "NewApi", "InlinedApi" })
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class ViewTicketDetails extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog prgDialog;
    ArrayList<Ticket> items = new ArrayList<>();
    String json = null, y = null;
    private ListView list;
    private UrlEncodedFormEntity uefa;
    private ProgressBar progress;
    //private FloatingActionButton fab;
    /////////////recycle view
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private TicketAdapter rvadapter ;
    InterstitialAd mInterstitialAd;
    RequestTast task=new RequestTast();
    private Spinner spinneremployees;
    private ArrayList<Status> statusList;
    private ArrayList<String>statusNames;
    private ArrayAdapter<String> adapter;
    private EditText mtxtMessage;
    private Button btnpost,btnscan;
    private  String message,status="",ticket,ticketCode="",telephone="";
    private ImageView imgbitmap;
    private CardView cardadmin;
    private TextView tv;
    private Toolbar toolbar;
    private TextView txAddressDetails;
    public ViewTicketDetails() {
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewtickets);
        mtxtMessage=(EditText)findViewById(R.id.editTextmessage);
        btnpost=(Button)findViewById(R.id.buttonpost);
        btnscan=(Button)findViewById(R.id.buttonscan);
        spinneremployees=(Spinner)findViewById(R.id.spinnerproducer);
        imgbitmap=(ImageView)findViewById(R.id.imageViewbarcode);
        cardadmin=(CardView)findViewById(R.id.cardadmin);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrowleft);
        tv=(TextView)findViewById(R.id.tv);
        txAddressDetails=(TextView)findViewById(R.id.txAddressDetails);
        json=getIntent().getStringExtra("json");

        if(Data.role(this).equals("5"))
        {
            cardadmin.setVisibility(View.GONE);
        }
        try
        {
            telephone=LogicClass.JSON(json).getString("contactTelephone");
            String email=LogicClass.JSON(json).getString("email");
            String contact=LogicClass.JSON(json).getString("contactName");
            String invoice=LogicClass.JSON(json).getString("InvoiceNo");
            String invoicestatus=LogicClass.JSON(json).getString("invoiceStatus");

            String address="Telephone: "+telephone+" , Email: "+email+" Name "+contact+
                    " InvoiceNo : "+invoice+" Invoice Status "+invoicestatus;

            txAddressDetails.setText(address);

        }
        catch (Exception er)
        {
            Utility.shoAlert(json,this);
         //Utility.shoAlert(er.toString(),this);
        }
        ticketCode=getIntent().getStringExtra("ticket");
        String ticketdata[]=ticketCode.split("#");
        ticket=ticketdata[1];
        tv.setText("Code: "+ticket);
        toolbar.setTitle("Ticket "+ticketCode);

        prgDialog=new ProgressDialog(this);
        rv = (RecyclerView)findViewById(R.id.rv);
        //rv2 = (RecyclerView)rootView.findViewById(R.id.rvhorizontal);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        mLayoutManager = new GridLayoutManager(this, 1);
        StaggeredGridLayoutManager st=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager hr=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rv.setLayoutManager(st);
        // rv2.setLayoutManager(hr);
        SpacesItemDecoration decoration = new SpacesItemDecoration(1);
        rv.addItemDecoration(decoration);
        btnpost.setOnClickListener(this);
        btnscan.setOnClickListener(this);
        try {
            task.execute();
        }
        catch (Exception er)
        {
            Log.e("adview",er.toString());
        }
        try {
            Bitmap barcode = CodeGenerator.encodeAsBitmap(ticket, BarcodeFormat.CODE_128, 600, 300);
            imgbitmap.setImageBitmap(barcode);
        }
        catch (Exception er)
        {
            Utility.showAlert(er.toString(),this);
        }
       // Utility.showAlert(Data.role(this),this);
        String role=Data.role(this).trim();
        int roleid=Integer.valueOf(role);
        if(roleid ==1 || roleid==2) {
            spinneremployees.setVisibility(View.VISIBLE);
            btnpost.setVisibility(View.VISIBLE);
            btnscan.setVisibility(View.VISIBLE);
        }
        else
        {
            spinneremployees.setVisibility(View.GONE);
            btnpost.setVisibility(View.GONE);
            btnscan.setVisibility(View.GONE);
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
        }
        @Override
        protected String doInBackground(Void... arg0) {
          String res =LogicClass.getStatus(ViewTicketDetails.this);
            Log.e("resp resp ",res);
            return  res;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            items.clear();
            items=LogicClass.getTicket(json);
            statusList=LogicClass.getStatusList(result);
            statusNames=LogicClass.getStatusNames(statusList);
           // Utility.showAlert(statusList.get(0).Status+" id "+statusList.get(0).StatusID+"",ViewTicketDetails.this);

            if(items.size()==0)
            {
                Toast.makeText(ViewTicketDetails.this,"no data must be an error" ,Toast.LENGTH_LONG).show();
                //loadLocal();
            }
            else {
                Data.SaveData(ViewTicketDetails.this,result,"status");
                //Data.SaveData(getActivity(),result,"getschedule");
                rvadapter = new TicketAdapter(ViewTicketDetails.this, items);
                rv.setAdapter(rvadapter);
            }
            if(statusList.size()==0)
            {
                statusList=LogicClass.getStatusList(Data.getData(ViewTicketDetails.this,"status"));
                statusNames=LogicClass.getStatusNames(statusList);
            }
            else
            {
                Data.SaveData(ViewTicketDetails.this,result,"status");
            }
            adapter=new  ArrayAdapter<String>(ViewTicketDetails.this,android.R.layout.simple_dropdown_item_1line, statusNames);
            spinneremployees.setAdapter(adapter);
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonpost)
        {

            message=mtxtMessage.getText().toString();
            status=statusList.get(spinneremployees.getSelectedItemPosition()).StatusID;
           if(Data.role(this).equals("1") || Data.role(this).equals("2")) {
               new RequestTastPost().execute(ticket);
           }
           else
           {
               Utility.showAlert("Action Not Allows",this);
           }
        }
        else if(v.getId()==R.id.buttonscan) {
            Intent intent = new Intent(getApplicationContext(),CaptureActivity.class);
            intent.setAction("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, 0);
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
            return  LogicClass.processTicket(ViewTicketDetails.this,arg0[0],status);
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Utility.showAlert(result,ViewTicketDetails.this);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                new RequestTastPost().execute(contents);
                Log.d("scan data", "contents: " + contents);
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("scan data", "RESULT_CANCELED");
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ticketdetails, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_call) {
            Intent calling=new Intent(Intent.ACTION_CALL);
            calling.setData(Uri.parse("tel:"+telephone));
            startActivity(calling);
            return true;
        }
        else   if (id == android.R.id.home) {
finish();
        }

        return super.onOptionsItemSelected(item);
    }

}