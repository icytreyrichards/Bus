package com.my.easybus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.squareup.picasso.Picasso;

import circleimageview.CircleTransform;
import localdata.Data;
import utility.Utility;

/**
 * Created by Richard.Ezama on 16/04/2016.
 */
public class IndexActivity extends AppCompatActivity {
    public Toolbar toolbar;
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i=getIntent();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.index);
        //Utility.Logout(this);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(isLoggedIn())
        {
           // Utility.showAlert(Data.is_LocalRecord(this).toString(),this);
            Intent log = new Intent(this, MainActivity.class);
            String role=Data.role(this);
            //Utility.shoAlert(role,this);
            if((role.equals("1")) || (role.equals("2"))
                    || (role.equals("3")))
            {
                log = new Intent(this, BookingActivity.class);
            }
            else if((role.equals("6")))
            {
                log = new Intent(this, DriversActivity.class);
            }
            else {
                log = new Intent(this, MainActivity.class);
            }
           //log = new Intent(this, DriversActivity.class);
            // log = new Intent(this, MainActivity.class);
            //here send this server
//new FCM().execute();
           startActivity(log);
           finish();
        }
    else {
            String cmd=i.getStringExtra("cmd");
            if(cmd.equals("log")) {
                Intent i=new Intent(this,Login.class);
                i.putExtra("cmd","reg");
                startActivity(i);
                finish();
            }
            else  if(cmd.equals("reg"))
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new Register(), "").commit();
            }
            else  if(cmd.equals("forgot"))
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new Forgotten(), "").commit();
            }
            else {

            }
        }

    }
    int times=0;
    @Override
    public void onBackPressed() {

       times++;
        if(times==1)
        {
            Intent i=new Intent(this,Login.class);
            i.putExtra("cmd","reg");
            finish();
        }
        else if(times>1)
        {
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Utility.showAlert("About Us",this);
            return true;
        }


        else if (id == R.id.action_contact) {
            Utility.showAlert("Contact Us",this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean isLoggedIn() {
       AccessToken accessToken= AccessToken.getCurrentAccessToken();

        Boolean res=false;
        //res=accessToken != null;
        if(Data.is_LocalRecord(this))
        {
            res=true;
        }
        return res;
    }
}