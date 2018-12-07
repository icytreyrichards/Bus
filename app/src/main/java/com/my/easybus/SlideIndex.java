package com.my.easybus;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
//import com.synnapps.carouselview.CarouselView;
//import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import BusinessLogic.LogicClass;
import Entities.Price;
import circleimageview.CircleTransform;
import io.fabric.sdk.android.Fabric;
import localdata.Data;
import utility.Utility;


import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

public class SlideIndex extends AppCompatActivity implements
        View.OnClickListener,GoogleApiClient.OnConnectionFailedListener
       {
    ///CarouselView carouselView;
   // int[] sampleImages = {R.drawable.iveco, R.drawable.vip};
    private Button reg,login;
    private TextView welcome,tips;
    private Calendar calendar;
    private int year,month,day,hour;
    private SignInButton signInButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN=8;

    private String id,fname,email;
    private LoginButton loginButton;
    CallbackManager callbackManager;

    //This is your KEY and SECRET
    //And it would be added automatically while the configuration
    private static final String TWITTER_KEY = "rdIidtgroEMWjnI4Q4FqaYpj1";
    private static final String TWITTER_SECRET = "V3rXxOjl8ddGJRni6STFMXNP1gRdvrK4eHLgOjAt09xhWVhy6y";
   TwitterLoginButton twitterLoginButton;
           TwitterSession session;
           ProgressBar progress;
           ImageView imgview;
           @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //Initializing TwitterAuthConfig, these two line will also added automatically while configuration we did
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.indexpage);
               progress=(ProgressBar)findViewById(R.id.progressBar);
               imgview=(ImageView)findViewById(R.id.imageView2);
                       Picasso.with(this).load(R.drawable.bus).transform(new CircleTransform())
                               .error(R.drawable.logo).fit()
                               .into(imgview);
        //Initializing twitter login button
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitterLogin);

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
        callbackManager = CallbackManager.Factory.create();
       gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(this);

        //carouselView = (CarouselView) findViewById(R.id.carouselView);
        //facebook=(Button)findViewById(R.id.buttonfacebook);
        reg=(Button)findViewById(R.id.buttonregister);
        login=(Button)findViewById(R.id.buttonlogin);
       // pricebutton=(Button)findViewById(R.id.buttonprices);
        welcome=(TextView)findViewById(R.id.textviewwelcome);
       // termsbutton=(Button) findViewById(R.id.buttonterms);

        tips=(TextView)findViewById(R.id.textviewtips);
        //carouselView.setPageCount(sampleImages.length);
       // carouselView.setImageListener(imageListener);
        reg.setOnClickListener(this);
        login.setOnClickListener(this);
        //pricebutton.setOnClickListener(this);
        //termsbutton.setOnClickListener(this);
        //facebook.setOnClickListener(this);
        initialize();
        if(isLoggedIn())
        {
            Intent log = new Intent(this, MainActivity.class);
            String role=Data.role(this).trim();
            Utility.shoAlert(role,this);
             if((role.equals("1")) || (role.equals("2"))
                    || (role.equals("3")))
            {
                log = new Intent(this, BookingActivity.class);
            }
            else if((role.equals("3")))
            {
                log = new Intent(this, BookingActivity.class);
            }
            else {
                 log = new Intent(this, MainActivity.class);
             }
           // log = new Intent(this, MainActivity.class);

            //here send this server
//new FCM().execute();
            startActivity(log);
            finish();
        }
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        int x= calendar.get(Calendar.HOUR_OF_DAY);
        hour=calendar.get(Calendar.AM_PM);
       if((hour==1)&&(x<15))
       {
           welcome.setText("Good Afternoon");
       }
       else  if((hour==1)&&(x>15))
       {
           welcome.setText("Good Evening");
       }
       else  if((hour==1)&&(x>20))
       {
           welcome.setText("Good Night");
       }
        else
       {
           welcome.setText("Hii There");
       }
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
                Intent i=new Intent(SlideIndex.this,MainActivity.class);
                //startActivity(i);

            }
        });

    }

    @Override
    public void onClick(View v) {
         if (v.getId()==R.id.buttonlogin)
        {
            Intent i=new Intent(this,Login.class);
            i.putExtra("cmd","log");
            //Intent i=new Intent(this,IndexActivity.class);
            startActivity(i);
            finish();
        }
        else  if (v.getId()==R.id.buttonregister)
         {
             Intent i=new Intent(this,IndexActivity.class);
             i.putExtra("cmd","reg");
             //Intent i=new Intent(this,IndexActivity.class);
             startActivity(i);
             finish();
         }
         else  if (v.getId()==R.id.buttonprices)
         {
             Intent i=new Intent(this, Prices.class);
             startActivity(i);
             //finish();
         }
         else  if (v.getId()==R.id.buttonterms)
         {
             Utility.showAlert("Terms and Conditions",this);
         }
         /*
         else  if (v.getId()==R.id.login_button)
         {
             Intent i=new Intent(this,FacebookLoginActivity.class);
             //startActivity(i);
             //finish();
         }
         */
         else  if (v.getId()==R.id.sign_in_button)
         {

             if(mGoogleApiClient.isConnected())
             {

                 //Toast.makeText(this,"connected", Toast.LENGTH_SHORT).show();
             }
               signIn();

         }

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public boolean isLoggedIn() {
        Boolean res=false;
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(Data.is_LocalRecord(SlideIndex.this))
        {
              res=true;
        }
       // Utility.shoAlert(res+"",this);
        return  res;// accessToken != null;
    }
    private void initialize()
    {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.my.easybus",
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.e("handleSignInResult:",result.isSuccess()+"");

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            fname=acct.getDisplayName();
            email=acct.getEmail();
            id=acct.getId();
            //Utility.showAlert(fname,SlideIndex.this);
            new NetWork().execute();
            //send this to server
          //Toast.makeText(this, acct.getIdToken(),Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Authorisation Failed",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                    if (obj.getString("data").equals("available")) {
                        String fullname = obj.getString("fullname");
                        String email = obj.getString("email");
                        String telephone = obj.getString("telephone");
                        String userid = obj.getString("UserID");
                        String roleid = obj.getString("RoleID");
                        Data.SaveData(SlideIndex.this, result, "enterprise");
                        LogicClass.LocalRegistryreg(SlideIndex.this, fullname, telephone, userid, email, roleid);
                        Intent user = new Intent(SlideIndex.this, MainActivity
                                .class);
                        if (roleid.equals("5")) {
                            user = new Intent(SlideIndex.this, HeroActivity.class);
                        } else {
                            user = new Intent(SlideIndex.this, BookingActivity.class);
                        }
                        startActivity(user);
                        SlideIndex.this.finish();

                    } else {
                        Toast.makeText(SlideIndex.this, "Login Failed",
                                Toast.LENGTH_LONG).show();
                    }
                }

                } catch(Exception er){
                    Utility.showAlert("login failed", SlideIndex.this);
                }
        }
    }
    private void SignOut()
    {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
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
           public class FCM extends AsyncTask<Void, Integer, String> {
               @Override
               protected void onPreExecute() {
                   // TODO Auto-generated method stub
                   super.onPreExecute();
               }

               @Override
               protected String doInBackground(Void... arg0) {
                   String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                   return LogicClass.FCM(SlideIndex.this,refreshedToken);
               }

               @Override
               protected void onPostExecute(String result) {
                   super.onPostExecute(result);

               }
           }

}

