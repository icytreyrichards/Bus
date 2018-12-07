package com.my.easybus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import BusinessLogic.LogicClass;
import track.TrackNow;
import utility.Utility;

/**
 * Created by Richard.Ezama on 6/2/2018.
 */

public class RateTrip extends AppCompatActivity {
    private Button rateBtn;
    private EditText rateText;
    private RatingBar rateBar;
    private String trip="";
    private Intent i;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog=new ProgressDialog(this);
        i=getIntent();
        trip=i.getStringExtra("trip");
        //Utility.shoAlert(trip,this);
        setContentView(R.layout.rate_trip);
        rateBtn=(Button)findViewById(R.id.btnRate);
        rateText=(EditText)findViewById(R.id.text);
        rateBar=(RatingBar)findViewById(R.id.rateBar);
        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateNow();
            }
        });

    }
    void rateNow()
    {
        dialog.setTitle("Rating");
        dialog.setMessage("Please Wait");
        final String rate=rateBar.getRating()+"";
        final String text=rateText.getText().toString();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.show();;
            }

            @Override
            protected String doInBackground(Void... voids) {
                String res = LogicClass.RateTrip(RateTrip.this,trip,rate,text);
                Log.e("tracker",res);
                return res;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Utility.shoAlert(s,RateTrip.this);

            }
        }.execute();
    }
}
