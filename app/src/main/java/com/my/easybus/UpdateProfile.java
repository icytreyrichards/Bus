package com.my.easybus;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import BusinessLogic.LogicClass;
import localdata.Data;
import utility.Utility;

public class UpdateProfile extends AppCompatActivity {
    private EditText fullname, email, phone;
    private ProgressBar progress;
    private String fname,em,tel;
    private Button register;
    private Toolbar toolbar;
    public UpdateProfile() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Update Profile");
        setSupportActionBar(toolbar);
        fullname = (EditText)findViewById(R.id.editTextfullname);
        email = (EditText)findViewById(R.id.editTextemail);
        phone = (EditText)findViewById(R.id.editTextphone);
        fullname.setText(Data.fullname(this));
        email.setText(Data.email(this));
        phone.setText(Data.telephone(this));
        progress = (ProgressBar)findViewById(R.id.progressBar1);
        register=(Button)findViewById(R.id.buttonregisteraccount);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             new UpdateTask().execute();
            }
        });
    }

    private class UpdateTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
            fname=fullname.getText().toString();
            tel=phone.getText().toString();
            em=email.getText().toString();
        }
        @Override
        protected String doInBackground(String... arg) {
           //String response = LogicClass.uploadImage(Profile.this,decodedstring);
           return LogicClass.UpdateRecords(UpdateProfile.this,fname,em,tel);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.setVisibility(View.GONE);
            try {
                Data.UpdateUsers(UpdateProfile.this,fname,em,tel);
                Utility.showAlert(result,UpdateProfile.this);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}
