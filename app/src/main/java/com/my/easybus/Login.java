package com.my.easybus;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;


import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import circleimageview.CircleTransform;
import io.fabric.sdk.android.Fabric;
import localdata.DBhelper;
import localdata.Data;
import BusinessLogic.LogicClass;
import utility.Utility;

/**
 * Created by Richard.Ezama on 12/04/2016.
 */
public class Login extends AppCompatActivity implements View.OnClickListener {
    private Button login;
    private TextView register,forgotten;
    private ProgressBar progress;

    private EditText tel, pin;
    private ProgressDialog prgDialog;
    // Progress Dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    private String json;
    private String username, password;

    private static final String ARG_SECTION_NUMBER = "section_number";
    ImageView imgview;

    private String id,fname,email;
    private LoginButton loginButton;
    CallbackManager callbackManager;
    //This is your KEY and SECRET
    //And it would be added automatically while the configuration
    private static final String TWITTER_KEY = "rdIidtgroEMWjnI4Q4FqaYpj1";
    private static final String TWITTER_SECRET = "V3rXxOjl8ddGJRni6STFMXNP1gRdvrK4eHLgOjAt09xhWVhy6y";
    TwitterLoginButton twitterLoginButton;
    TwitterSession session;
    public Login() {
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        FacebookSdk.sdkInitialize(Login.this);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(Login.this, new Twitter(authConfig));
        imgview=(ImageView)findViewById(R.id.imageView2);

        login = (Button) findViewById(R.id.buttonlogin);
        tel = (EditText) findViewById(R.id.editTextemaillogin);
        pin = (EditText) findViewById(R.id.editTextpasswordlogin);
        register = (TextView) findViewById(R.id.textViewsignup);
        forgotten = (TextView) findViewById(R.id.textViewforgotten);
        progress = (ProgressBar) findViewById(R.id.progressBar1);
        register.setOnClickListener(this);
        forgotten.setOnClickListener(this);
        //Initializing twitter login button
        twitterLoginButton = (TwitterLoginButton)findViewById(R.id.twitterLogin);

        //Adding callback to the button
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //If login succeeds passing the Calling the login method and passing Result object
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                //If failure occurs while login handle it here
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setOnClickListener(this);
        login.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                try {
                    com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
                    id = loginResult.getAccessToken().getUserId();
                    fname = profile.getName();
                    String firstname = profile.getFirstName();
                    String lastname = profile.getLastName();

                    email="";
                    //id = profile.getId();
                    Log.e("fullname",fname);
                    String photo = profile.getProfilePictureUri(200, 200).toString();
                    new NetWork().execute();
                    //Utility.showAlert(fname,SlideIndex.this);
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
                Intent i=new Intent(Login.this,MainActivity.class);
                //startActivity(i);

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonlogin) {
            if (tel.getText().toString().isEmpty()) {
                tel.setError("email missing");
            } else if (pin.getText().toString().isEmpty()) {
                pin.setError("password missing");
            } else if ((tel.getText().toString().isEmpty())
                    && (tel.getText().toString().isEmpty())) {
                tel.setError("email missing");
                pin.setError("password missing");
            } else if ((!tel.getText().toString().isEmpty())
                    && (!tel.getText().toString().isEmpty())) {
                username = tel.getText().toString();
                password = pin.getText().toString();
                new LoginTask().execute();
            }
        }
       else  if (v.getId() == R.id.textViewsignup) {
            Intent i=new Intent(this,IndexActivity.class);
            i.putExtra("cmd","reg");
            startActivity(i);
           finish();
        }
        else if (v.getId() == R.id.textViewforgotten) {
            Intent i=new Intent(this,IndexActivity.class);
            i.putExtra("cmd","forgot");
            startActivity(i);
            finish();
        }

    }
    public class LoginTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // showDialog(progress_bar_type);
            progress.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            return LogicClass.Login(username,password);
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progress.setVisibility(View.GONE);
            //Utility.showAlert(result,Login.this);
            try
            {
                JSONArray jarray = new JSONArray(result);
                int x = 0;
                for (x = 0; x < jarray.length(); x++) {
                    JSONObject obj = jarray.getJSONObject(x);
                    if (obj.getString("data").equals("available")) {
                        String fullname=obj.getString("fullname");
                        String email=obj.getString("email");
                        String telephone=obj.getString("telephone");
                        String userid=obj.getString("UserID");
                        String roleid=obj.getString("RoleID");
                       // Utility.shoAlert(roleid,Login.this);
                        Data.SaveData(Login.this,result,"enterprise");
                        LogicClass.LocalRegistryreg(Login.this,fullname, telephone, userid, email,roleid);
                            Intent user = new Intent(Login.this, MainActivity.class);
                            if(roleid.equals("5"))
                            {
                                //customers directed there
                                user = new Intent(Login.this, MainActivity.class);
                            }
                            else if((roleid.equals("6")))
                            {
                                user = new Intent(Login.this, DriversActivity.class);
                            }
                            else
                            {
                                //this should be for booking officers
                                user = new Intent(Login.this, BookingActivity.class);
                            }
                           Login.this.startActivity(user);
                           Login.this.finish();

                    } else {
                        Utility.showAlert(result,Login.this);

                        Toast.makeText(Login.this, "Login Failed",
                                Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Utility.showAlert("error contact system admin"+result,Login.this);
                e.printStackTrace();
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           // Utility.showAlert(result,Login.this);
        }

    }
    private void login(Result<TwitterSession> result)
    {
        session=result.data;
        retrofit2.Call<User> userResult = Twitter.getApiClient(session).getAccountService().verifyCredentials(true,false);
        userResult.enqueue(new Callback<User>() {

            @Override
            public void failure(TwitterException e) {

            }
            @Override
            public void success(Result<User> userResult) {
                User user = userResult.data;
                String twitterImage = user.profileImageUrl;
                try {
                    Log.d("imageurl", user.profileImageUrl);
                    fname= user.name;
                    email=user.email;
                    id=String.valueOf(user.getId()).trim();
                    new NetWork().execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });
    }
    public class NetWork extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... arg0) {
            return LogicClass.SignUpFB(fname,email, id,"2016");
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("res", result);
            progress.setVisibility(View.GONE);
            try {
                JSONArray jarray = new JSONArray(result);
                int x = 0;
                for (x = 0; x < jarray.length(); x++) {
                    JSONObject obj = jarray.getJSONObject(x);
                    String fullname=obj.getString("fullname");
                    String email=obj.getString("email");
                    String telephone=obj.getString("telephone");
                    String userid=obj.getString("UserID");
                    String roleid=obj.getString("RoleID");
                    Data.SaveData(Login.this,result,"enterprise");
                    if(LogicClass.LocalRegistryreg(Login.this,fullname, telephone, userid, email,roleid))
                    {
                        Intent user = new Intent(Login.this, MainActivity.class);
                        if(roleid.equals("5"))
                        {
                            user = new Intent(Login.this, MainActivity.class);

                        }
                        else
                        {
                            user = new Intent(Login.this, BookingActivity.class);

                        }
                        startActivity(user);
                        finish();
                    }
                }

            } catch(Exception er){
                Utility.showAlert("login failed", Login.this);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
