package com.my.easybus;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.facebook.*;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import BusinessLogic.LogicClass;
import localdata.Data;
import utility.Utility;

/**
 * Created by Richard.Ezama on 16/06/2016.
 */
public class FacebookLoginActivity extends AppCompatActivity {
    private LoginButton  loginButton;
    CallbackManager callbackManager;
    String fname="",email="",tel="",id="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        initialize();
        setContentView(R.layout.facebook);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                try {
                    // App code
                    com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
                    id = loginResult.getAccessToken().getUserId();
                    fname = profile.getName();
                    String firstname = profile.getFirstName();
                    String lastname = profile.getLastName();
                    //id = profile.getId();
                    Log.e("fullname",fname);
                    String photo = profile.getProfilePictureUri(200, 200).toString();
                    //new NetWork().execute();
                   Utility.showAlert(fname,FacebookLoginActivity.this);
                }
                catch (Exception er)
                {

                }

            }
            @Override
            public void onCancel() {
                // App code
            }
            @Override
            public void onError(FacebookException exception) {
                // App code
                Intent i=new Intent(FacebookLoginActivity.this,MainActivity.class);
                //startActivity(i);

            }
        });


    }

    private void initialize()
    {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.my.tafuta",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public class NetWork extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... arg0) {
            return LogicClass.SignUpFB(fname,email, id,"2016");
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("res",result);
            Utility.showAlert(result,FacebookLoginActivity.this);
            if(!result.startsWith("{")) {
                Utility.showAlert(result,FacebookLoginActivity.this);
            }
            else {
                try {
                    JSONObject obj = new JSONObject(result);
                    String fullname=obj.getString("fullname");
                    String email=obj.getString("email");
                    String telephone=obj.getString("telephone");
                    String userid=obj.getString("UserID");
                    String roleid=obj.getString("RoleID");
                    Data.SaveData(FacebookLoginActivity.this,result,"enterprise");

                    if (LogicClass.LocalRegistryreg(FacebookLoginActivity.this, fullname, telephone, userid, email,"5")) {
                        Intent i=new Intent(FacebookLoginActivity.this,MainActivity.class);
                        startActivity(i);
                    }

                } catch (Exception er) {

                }
            }
        }
    }
}
