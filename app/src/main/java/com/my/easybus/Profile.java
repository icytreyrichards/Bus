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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import BusinessLogic.LogicClass;
import circleimageview.CircleTransform;
import localdata.Data;
import utility.Utility;

public class Profile extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    //private ListView list;
    //private ContactBeanAdapter adapter;
    private ProfileSectionsPageAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private int PROFILE = 1;
    public ProgressBar progress;
    private String image;
    private ImageView profilepic;
    private String url;
    private String decodedstring;
    private Intent i;
    public Profile() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        i=getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
       // dynamicToolbarColor();
        toolbarTextAppernce();
        mSectionsPagerAdapter = new ProfileSectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        progress=(ProgressBar)findViewById(R.id.progressBar1);
        profilepic=(ImageView)findViewById(R.id.profilepic);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     //selectImageFromGallery();
            }
        });
        try {
            setProfile();
        }
        catch (Exception er)
        {
            Log.e("error",er.toString());
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // TODO Auto-generated method stub
                int id = item.getItemId();
                if (id == android.R.id.home) {
                    finish();
                    return true;
                }
                return false;
            }
        });
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
    private void setProfile()
    {
        Log.e("local uid",Data.User_Id(Profile.this));

            Picasso.with(this).load(R.drawable.logo)
                    .error(R.drawable.error).fit()
                    .into(profilepic);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == PROFILE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                profilepic.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                image = imgDecodableString;
                Bitmap bitmap=BitmapFactory
                        .decodeFile(imgDecodableString);
                decodedstring=getStringImage(bitmap);
                Toast.makeText(this, image,
                        Toast.LENGTH_LONG).show();
                new UploadTask().execute(imgDecodableString);
                cursor.close();
                // Set the Image in ImageView after decoding the String
            }else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

        }
    }
    @SuppressWarnings("deprecation")

    private class UploadTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... arg) {
           //String response = LogicClass.uploadImage(Profile.this,decodedstring);
           return LogicClass.UploadProfilePicture(Profile.this,image);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.setVisibility(View.GONE);
            try {

                JSONObject o=new JSONObject(result);
                String title=o.getString("title");
                String photo=Data.server+o.getString("photo");
                if(title.contains("successfully")) {
                    Data.UpdateProfilePicture(Profile.this,photo);
                }
                Utility.showAlert(title,Profile.this);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public void selectImageFromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PROFILE);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}
