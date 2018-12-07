package BusinessLogic;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.my.easybus.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import Entities.BusTime;
import Entities.Menu;
import Entities.Pickups;
import Entities.Price;
import Entities.Route;
import Entities.RouteData;
import Entities.Schedule;
import Entities.Seat;
import Entities.Status;
import Entities.Ticket;
import Entities.Topics;
import encrypt.EncryptFile;
import encrypt.Vendor;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import localdata.DBhelper;
import localdata.Data;
import network.MyHttpClient;
import network.NetworkAdmin;
import track.TrackNow;
import utility.Utility;

/**
 * Created by Richard.Ezama on 20/04/2016.
 */
public class LogicClass {
    private static Context context;
    public static Location location;

    public LogicClass(Context context)
    {
this.context=context;
    }
    static UrlEncodedFormEntity uefa;

    public static String UpdateRecords(Context context,String fullname,String email,String telephone)
    {
        String result="";
        final String url2 = Data.server + "update.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("UserID",Data.User_Id(context) ));
        param.add(new BasicNameValuePair("fullname",fullname ));
        param.add(new BasicNameValuePair("email",email));
        param.add(new BasicNameValuePair("telephone",telephone));
        try {
            uefa = new UrlEncodedFormEntity(param);
            // result= NetworkAdmin.jsonarray(uefa,url2);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
            Data.UpdateUsers(context,fullname,telephone,email);

        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String SignUp(String fullname,String email,String telephone,String password,String countryCode)
    {
       String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","register" ));
        param.add(new BasicNameValuePair("fullname",fullname ));
        param.add(new BasicNameValuePair("email",email));
        param.add(new BasicNameValuePair("countryCode",countryCode));
        param.add(new BasicNameValuePair("telephone",telephone));
        param.add(new BasicNameValuePair("pin",password));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String forgottenPassword(String email)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","forgotten" ));
        param.add(new BasicNameValuePair("email",email));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String SignUpFB(String fullname,String email,String id,String password)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();

        param.add(new BasicNameValuePair("cmd","register_fb" ));
        param.add(new BasicNameValuePair("fullname",fullname ));
        param.add(new BasicNameValuePair("email",email));
        param.add(new BasicNameValuePair("id",id));
        param.add(new BasicNameValuePair("pin",password));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String FCM(Context context,String id)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();

        param.add(new BasicNameValuePair("cmd","FCM" ));
        param.add(new BasicNameValuePair("id",id ));
        param.add(new BasicNameValuePair("UserID",Data.UserID(context) ));
       try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
           Log.e("fcm",result);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String getMyTickets(Context context,String status)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","getTickets"));
        param.add(new BasicNameValuePair("status",status));
        param.add(new BasicNameValuePair("UserID",Data.UserID(context) ));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
            Log.e("response",result);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String getCompanyTickets(Context context,String status,String search)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","getCompanyTickets"));
        param.add(new BasicNameValuePair("status",status));
        param.add(new BasicNameValuePair("search",search));
        param.add(new BasicNameValuePair("UserID",Data.UserID(context) ));
        param.add(new BasicNameValuePair("CompanyID",Data.getEnterpriseCompanyID(context) ));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
            Log.e("response",result);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String getCompanyRoutes(Context context)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","getCompanyRoutes"));
        param.add(new BasicNameValuePair("UserID",Data.UserID(context) ));
        param.add(new BasicNameValuePair("CompanyID",Data.getEnterpriseCompanyID(context) ));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
            Log.e("response",result);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String searchCompanyTickets(Context context,String status,String search)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","searchCompanyTickets"));
        param.add(new BasicNameValuePair("status",status));
        param.add(new BasicNameValuePair("search",search));
        param.add(new BasicNameValuePair("UserID",Data.UserID(context) ));
        param.add(new BasicNameValuePair("CompanyID",Data.getEnterpriseCompanyID(context) ));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
            Log.e("response",result);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String processTicket(Context context,String ticket,String status)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","processTicket"));
        param.add(new BasicNameValuePair("status",status));
        param.add(new BasicNameValuePair("UserID",Data.UserID(context) ));
        param.add(new BasicNameValuePair("CompanyID",Data.getEnterpriseCompanyID(context)));
        param.add(new BasicNameValuePair("TicketID",ticket));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
            Log.e("response",result);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String getTelephoneCodes(Context context)
    {
        String result="";
        final String url2 = Data.server + "telephoneCodes.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity).trim();
            Log.e("response",result);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String Login(String email,String password)
    {
        String result="";
        final String url = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","login" ));
        param.add(new BasicNameValuePair("username",email ));
        param.add(new BasicNameValuePair("password",password));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
            Log.e("login res from service",result);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String ChangePassword(Context context ,String oldpwd,String newpwd)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("user_id",Data.User_Id(context) ));
        param.add(new BasicNameValuePair("oldpassword",oldpwd));
        param.add(new BasicNameValuePair("newpassword",newpwd));
        param.add(new BasicNameValuePair("change_password","yes"));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String getLostandFound(Context context)
    {
        String result="";
        final String url2 = Data.server + "getData.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        //param.add(new BasicNameValuePair("UserID",Data.User_Id(context) ));
        try {
            uefa = new UrlEncodedFormEntity(param);
            //result= NetworkAdmin.jsonarray(uefa,url2);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result=e.toString();
        }
        return result;
    }
    public static String getSchedule(Context context,String source,String destination,String time,String code)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","getSchedule"));
        param.add(new BasicNameValuePair("source",source));
        param.add(new BasicNameValuePair("destination",destination));
        param.add(new BasicNameValuePair("CompanyID",Data.getEnterpriseCompanyID(context)));
        param.add(new BasicNameValuePair("time",time));
        param.add(new BasicNameValuePair("code",code));
        param.add(new BasicNameValuePair("UserID",Data.UserID(context)));

        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    public static String getBooked(Context context,String schedule,String date,String time,String bus)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","getBooked"));
        param.add(new BasicNameValuePair("schedule",schedule));
        param.add(new BasicNameValuePair("time",time));
        param.add(new BasicNameValuePair("bus",bus));
        param.add(new BasicNameValuePair("date",date));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String startJourney(Context context,String schedule,int starting)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        if(starting==0)
        {

            param.add(new BasicNameValuePair("cmd","endTrip"));
        }
        else
        {
            param.add(new BasicNameValuePair("cmd","startTrip"));
        }
        param.add(new BasicNameValuePair("schedule",schedule));
        param.add(new BasicNameValuePair("status",starting+""));
        param.add(new BasicNameValuePair("UserID",Data.UserID(context)));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String startTracking(final Context context,final String schedule,final String latitude,final String longitude,
                                       final String bearing,
                                       final String speed,final String TripID)
    {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {

                String result="";
                final String url2 = Data.server + "Api.php";
                HttpClient client = getHttpClient();
                HttpPost post = new HttpPost(url2);
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("cmd","startTracking"));
                param.add(new BasicNameValuePair("schedule",schedule));
                param.add(new BasicNameValuePair("latitude",latitude));
                param.add(new BasicNameValuePair("longitude",longitude));
                param.add(new BasicNameValuePair("bearing",bearing));
                param.add(new BasicNameValuePair("speed",speed));
                param.add(new BasicNameValuePair("TripID",TripID));
                param.add(new BasicNameValuePair("UserID",Data.UserID(context)));
                try {
                    uefa = new UrlEncodedFormEntity(param);
                    post.setEntity(uefa);
                    HttpResponse res=client.execute(post);
                    HttpEntity entity=res.getEntity();
                    result=EntityUtils.toString(entity);
                    Log.e("update result",result);
                }  catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return result;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("result is ",s);

                try
                {


                }
                catch (Exception er)
                {
                    Utility.shoAlert(er.toString(),context);
                }
            }
        }.execute();







        return result;
    }
    public static String sendMessage(Context context,String schedule,String message)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","sendMessage"));
        param.add(new BasicNameValuePair("schedule",schedule));
        param.add(new BasicNameValuePair("message",message));
        param.add(new BasicNameValuePair("UserID",Data.UserID(context)));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String getFavourites(Context context)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("UserID",Data.User_Id(context)));
        param.add(new BasicNameValuePair("cmd","myfavourites"));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String BookNow(Context context,String route,String schedule,String seat,
                                 String date,String payment,String pickup,String bus,String amount,
                                 String time,String company,String cname,String ctel,String bookingtype,
                                 int charge,int vat,int sub)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","book"));
        param.add(new BasicNameValuePair("BookingMethod",bookingtype));
        param.add(new BasicNameValuePair("CustomerID",Data.User_Id(context)));
        param.add(new BasicNameValuePair("RouteID",route));
        param.add(new BasicNameValuePair("telephone",Data.telephone(context)));
        param.add(new BasicNameValuePair("ScheduleID",schedule));
        param.add(new BasicNameValuePair("CompanyID",company));
        param.add(new BasicNameValuePair("seat",seat));
        param.add(new BasicNameValuePair("date",date));
        param.add(new BasicNameValuePair("pickup",pickup));
        param.add(new BasicNameValuePair("payment",payment));
        param.add(new BasicNameValuePair("bus",bus));
        param.add(new BasicNameValuePair("time",time));
        param.add(new BasicNameValuePair("amount",amount));
        param.add(new BasicNameValuePair("charge",charge+""));
        param.add(new BasicNameValuePair("sub",sub+""));
        param.add(new BasicNameValuePair("vat",vat+""));
        param.add(new BasicNameValuePair("cname",cname));
        param.add(new BasicNameValuePair("ctel",ctel));

        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String getRoutes(Context context)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","getRoutes"));
        param.add(new BasicNameValuePair("CompanyID",Data.getEnterpriseCompanyID(context)));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String SearchRoutes(Context context,String search)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","SearchRoutes"));
        param.add(new BasicNameValuePair("UserID",Data.User_Id(context) ));
        param.add(new BasicNameValuePair("search",search));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String getStatus(Context context)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","getStatus" ));
        param.add(new BasicNameValuePair("RoleID",Data.role(context) ));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String getHistory(Context context,String UserID)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("UserID",UserID ));
        param.add(new BasicNameValuePair("cmd","getHistory" ));
        try {
            uefa = new UrlEncodedFormEntity(param);
            result= NetworkAdmin.jsonarray(uefa,url2);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<String> getTakenlist(String data)
    {
        ArrayList<String>result=new ArrayList<>();
        try {
            JSONArray jarray = new JSONArray(data);
            for(int x=0; x< jarray.length();x++)
            {
                JSONObject o=jarray.getJSONObject(x);
                String seat=o.getString("seat");
                result.add(seat);
            }
        }
        catch (JSONException er)
        {

        }
        catch (Exception er)
        {

        }
        return result;
    }
    public static String UploadProfilePicture(Context context, String path) {
        Log.e("post response", "started");
       String  url = Data.server + "profileupload.php";
        String responseString ="sending request failed";
        HttpClient httpclient = getHttpClient();
        HttpPost httppost = new HttpPost(url);
        try {
            Log.e("post response", "started");
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            File sourceFile = new File(path);
            entity.addPart("images", new FileBody(sourceFile));
            entity.addPart("submit", new StringBody(""));
            entity.addPart("UserID",
                    new StringBody(Data.User_Id(context)));
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            responseString="tried execution";
            if (statusCode == 200) {
                responseString = EntityUtils.toString(r_entity);
               // responseString="okay so whats up";
                Log.e("post response", responseString);
            } else {
                responseString = "error";
            }
        } catch (ClientProtocolException e) {
            responseString = e.toString();
            Log.e("clientprotocal", "error");
        } catch (IOException e) {
            responseString = e.toString();
            Log.e("error io", "error");
        }

        return responseString;

    }
    public static ArrayList<BusTime> getTimeList(String data,Context context)
    {
        Log.e("json",data);

        ArrayList<BusTime>result=new ArrayList<>();
        result.add(new BusTime("All","0"));

        try {
            JSONArray timearray=null;
            JSONArray jarray=new JSONArray(data);
            for(int x=0; x<jarray.length();x++) {
                 timearray =jarray.getJSONArray(2);
                 }
            for(int x=0; x<timearray.length();x++) {
                JSONObject o = timearray.getJSONObject(x);
                String time = o.getString("time");
                BusTime t=new BusTime(time,time);
                result.add(t);
                //Utility.showAlert(timearray.toString(),context);

            }
        }
        catch (JSONException er)
        {
            Log.e("json error",er.toString());
        }

        return result;
    }
    public static ArrayList<RouteData> getRoutesList(String data) {
        Log.e("json", data);
        ArrayList<RouteData> result = new ArrayList<>();
        try {
            JSONArray jarray = new JSONArray(data);
            for(int x=0; x<jarray.length();x++) {
                JSONObject o = jarray.getJSONObject(x);
                result.add(new RouteData(o.toString()));
            }
        }
        catch (Exception er)
        {

        }
        return  result;
    }
    public static ArrayList<String> getRoutesNames(Context ctx, ArrayList<RouteData> data) {
        ArrayList<String> result = new ArrayList<>();

        try {
            String company=Data.getEnterpriseCompanyID(ctx);
            for(int x=0; x<data.size();x++) {
                JSONObject o =new JSONObject(data.get(x).json);
                String name=o.getString("source")+" To "+o.getString("destination");
                if(company.equals("1"))
                {
                    name=o.getString("source")+" To "+o.getString("destination")+" "+o.getString("CompanyName");
                }
                result.add(name);
            }
        }
        catch (Exception er)
        {

        }
        return  result;
    }

    public static ArrayList<Pickups> getPickups(String data) {
        Log.e("json", data);
        ArrayList<Pickups> result = new ArrayList<>();
        try {
            JSONArray jarray = new JSONArray(data);
            for(int x=0; x<jarray.length();x++) {
                JSONObject o = jarray.getJSONObject(x);
                result.add(new Pickups(o.toString()));
            }
        }
        catch (Exception er)
        {

        }
        return  result;
    }
    public static ArrayList<String> getPickupsList(Context ctx, ArrayList<Pickups> data) {
        ArrayList<String> result = new ArrayList<>();

        try {
            String company=Data.getEnterpriseCompanyID(ctx);
            for(int x=0; x<data.size();x++) {
                JSONObject o =new JSONObject(data.get(x).json);
                String name=o.getString("pickup");
                result.add(name);
            }
        }
        catch (Exception er)
        {

        }
        return  result;
    }

        public static ArrayList<ArrayList<Route>> getRouteList(String data)
    {
        Log.e("json",data);
        ArrayList<ArrayList<Route>>result=new ArrayList<>();
        ArrayList<Route>slist=new ArrayList<>();
        ArrayList<Route>dlist=new ArrayList<>();

        try {
            JSONArray sourcearray=null,destarray=null;

                       JSONArray jarray=new JSONArray(data);
                       for(int x=0; x<jarray.length();x++) {
                           sourcearray =jarray.getJSONArray(0);
                           destarray =jarray.getJSONArray(1);
                    }
            for(int x=0; x<sourcearray.length();x++) {
                JSONObject o=sourcearray.getJSONObject(x);
                String source = o.getString("source");
                String buscompany = o.getString("CompanyName");
                String destination = o.getString("destination");
                String id = o.getString("RouteID");
                Route route=new Route(source,destination,id);
                slist.add(route);
            }
            for(int x=0; x<destarray.length();x++) {
                JSONObject o=destarray.getJSONObject(x);
                String source =  o.getString("source");
                String buscompany = o.getString("CompanyName");
                String destination = o.getString("destination");
                String id = o.getString("RouteID");
                Route route=new Route(source,destination,id);
                dlist.add(route);
            }
            result.add(slist);
            result.add(dlist);
        }
        catch (JSONException er)
        {
            Log.e("json error",er.toString());
        }
        return result;
    }
    public static ArrayList<Schedule> getScheduleList(Context context, String data)
    {
        Log.e("json",data);
        ArrayList<Schedule>result=new ArrayList<>();
        try {
            JSONArray jarray=new JSONArray(data);
            for(int x=0; x<jarray.length();x++) {
                JSONObject o =jarray.getJSONObject(x);
                String source = o.getString("source");
                String time = o.getString("time");
                String destination = o.getString("destination");
                String rid = o.getString("RouteID");
                String  id= o.getString("RecordID");
                String  bus= o.getString("CompanyName");
                String  productName= o.getString("ProductName");
                String  price= o.getString("price");
                Schedule schedule=new Schedule(source,destination,time,rid,id,bus,o.toString(),price,productName);
                result.add(schedule);
            }


        }
        catch (JSONException er)
        {
            //Utility.showAlert(er.toString(),context);
            Log.e("json error",er.toString());
        }
        return result;
    }
    public static ArrayList<Ticket> getTicketList(String data)
    {
        Log.e("json",data);
        ArrayList<Ticket>result=new ArrayList<>();
        try {
            JSONArray jarray=new JSONArray(data);
            for(int x=0; x<jarray.length();x++) {
                JSONObject o =jarray.getJSONObject(x);
                String source = o.getString("source");
                String destination = o.getString("destination");
                String time = o.getString("time");
                String rid = o.getString("RouteID");
                String id = o.getString("RecordID");
                String bus = o.getString("CompanyName");
                String price = o.getString("price");
                String ticket = o.getString("TicketID");
                String status = o.getString("status");
                Ticket schedule=new Ticket(source,destination,time,rid,id,bus,o.toString(),price,ticket,status);
                result.add(schedule);
            }


        }
        catch (JSONException er)
        {
            Log.e("json error",er.toString());
        }
        return result;
    }
    public static ArrayList<Ticket> getTicket(String data)
    {
        Log.e("json",data);
        ArrayList<Ticket>result=new ArrayList<>();
        try {
                JSONObject o =new JSONObject(data);
                String source = o.getString("source");
                String destination = o.getString("destination");
                String time = o.getString("time");
                String rid = o.getString("RouteID");
                String id = o.getString("RecordID");
                String bus = o.getString("CompanyName");
                String price = o.getString("price");
                String ticket = o.getString("TicketID");
                String status = o.getString("status");
                Ticket schedule=new Ticket(source,destination,time,rid,id,bus,o.toString(),price,ticket,status);
                result.add(schedule);

        }
        catch (JSONException er)
        {
            Log.e("json error",er.toString());
        }
        return result;
    }
    public static ArrayList<Status> getStatusList(String data)
    {
        Log.e("status json",data);
        ArrayList<Status>result=new ArrayList<>();
        try {
            JSONArray jarray=new JSONArray(data);
            for(int x=0;x<jarray.length();x++) {
                JSONObject o =jarray.getJSONObject(x);
                String code = o.getString("Code");
                String id = o.getString("StatusID");
                Status item = new Status(o.toString(),code, id);
                result.add(item);
            }
        }
        catch (JSONException er)
        {
            Log.e("json error",er.toString());
        }
        return result;
    }
    public static ArrayList<String> getStatusNames(ArrayList<Status> list)
    {

        ArrayList<String>result=new ArrayList<>();
        try {
           for(int x=0;x<list.size();x++)
           {
               result.add(list.get(x).Status);
           }

        }
        catch (Exception er)
        {
            Log.e("json error",er.toString());
        }
        return result;
    }




    public static ArrayList<Price> getPriceList(String data)
    {
        Log.e("json",data);
        ArrayList<Price>result=new ArrayList<>();
        try {
            JSONArray jarray=new JSONArray(data);
            for(int x=0; x<jarray.length();x++) {
                JSONObject o =jarray.getJSONObject(x);
                String source = o.getString("source");
                String destination = o.getString("destination");
                String  bus= o.getString("CompanyName");
                String  price= o.getString("price");
                String  product= o.getString("ProductName");
                Price schedule=new Price(source,destination,bus,price,product);
                result.add(schedule);
            }


        }
        catch (JSONException er)
        {
            Log.e("json error",er.toString());
        }
        return result;
    }
    public static String getPrices(Context context)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","getPrices"));
        try {
            uefa = new UrlEncodedFormEntity(param);
            result= NetworkAdmin.jsonarray(uefa,url2);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result.trim();
    }

    public static String verifyTickets(Context context,String ticket)
    {
        String result="";
        final String url2 = Data.server + "addFriend.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("UserID",Data.User_Id(context) ));
        param.add(new BasicNameValuePair("cmd","verifyTickets"));
        param.add(new BasicNameValuePair("TicketID",ticket));
        try {
            uefa = new UrlEncodedFormEntity(param);
            result= NetworkAdmin.jsonarray(uefa,url2);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    private static String result="";
    public static String SendGCM(final String regid, final String title)
    {
                 final String url2 = Data.server + "send_gcm.php";
                HttpClient client = getHttpClient();
                HttpPost post = new HttpPost(url2);
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("regid",regid));
                param.add(new BasicNameValuePair("message",title));
                param.add(new BasicNameValuePair("UserID",Data.User_Id(context)));
                try {
                    uefa = new UrlEncodedFormEntity(param);
                    result= NetworkAdmin.jsonarray(uefa,url2);
                }  catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

        return result;
    }


    public static String SaveLostVolley(Context context, final String title, final String category, final String description, final String latitude, final String longitude, final String contactname, final String telephone, final String location, final Bitmap bitmap)
    {
        result="Uploaded and  Saved";
        final String url = Data.server + "report.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        result=s;
                        Log.e("server",s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                       // loading.dismiss();
                        try {
                            result = volleyError.getMessage().toString();
                        }
                        catch (NullPointerException er)
                        {

                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<String, String>();
                String image=getStringImage(bitmap);
                //Adding parameters
                try {


                        JSONObject o = new JSONObject();
                        o.put("title", title);
                        o.put("category", category);
                        o.put("latitude",latitude);
                        o.put("longitude",longitude);
                        o.put("contact_name",contactname);
                        o.put("contact_telephone",telephone);
                        o.put("location",location);
                        o.put("description", description);

                       /*
                        params.put("title", title);
                        params.put("category", category);
                        params.put("latitude", latitude);
                        params.put("longitude", longitude);
                        params.put("contact_name", contactname);
                        params.put("contact_telephone", telephone);
                        params.put("location", location);
                        params.put("description", description);
                        */
                        params.put("images", image);
                        params.put("json",o.toString());

                    }
                    catch (Exception er)
                    {

                    }
                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to the queue
        requestQueue.add(stringRequest);
        return  result;
    }

   public static  String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static ArrayList<Topics> getCategories(String []cats, String []title)
    {
        ArrayList<Topics>result=new ArrayList<>();
        for(int x=0; x< cats.length; x++)
        {
            result.add(new Topics(cats[x],title[x], R.drawable.logo));
        }
        return result;
    }
    public static String UploadData(Context context, final String title, final String category, final String description, final String latitude, final String longitude, final String contactname, final String telephone, final String location, final Bitmap bitmap)
    {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String result="";
        final String url2 = Data.server + "report.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        try
        {
        JSONObject o = new JSONObject();
        o.put("title", title);
        o.put("category", category);
        o.put("latitude",latitude);
        o.put("longitude",longitude);
        o.put("contact_name",contactname);
        o.put("contact_telephone",telephone);
        o.put("location",location);
        o.put("description", description);
            o.put("UserID", Data.UserID(context));
                       /*
                        params.put("title", title);
                        params.put("category", category);
                        params.put("latitude", latitude);
                        params.put("longitude", longitude);
                        params.put("contact_name", contactname);
                        params.put("contact_telephone", telephone);
                        params.put("location", location);
                        params.put("description", description);
                              params.put("images", image);
        params.put("json",o.toString());
                        */
        param.add(new BasicNameValuePair("json",o.toString()));
        param.add(new BasicNameValuePair("images",getStringImage(bitmap)));
            uefa = new UrlEncodedFormEntity(param);
            //result= NetworkAdmin.jsonarray(uefa,url2);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
            Log.e("server",result);
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            result=e.toString();
            e.printStackTrace();
        }
        return result;
    }
    public static String UploadApache(Context context, final String title, final String category, final String description, final String latitude, final String longitude, final String contactname, final String telephone, final String location, final String path) {
        Log.e("post response", "started");
        String  url = Data.server + "profileupload.php";
        String responseString ="sending request failed";
        HttpClient httpclient = getHttpClient();
        HttpPost httppost = new HttpPost(url);
        try {
            Log.e("post response", "started");
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            JSONObject o = new JSONObject();
            o.put("title", title);
            o.put("category", category);
            o.put("latitude",latitude);
            o.put("longitude",longitude);
            o.put("contact_name",contactname);
            o.put("contact_telephone",telephone);
            o.put("location",location);
            o.put("description", description);
            File sourceFile = new File(path);
            entity.addPart("images", new FileBody(sourceFile));
            entity.addPart("submit", new StringBody(""));
            entity.addPart("json", new StringBody(o.toString()));
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            responseString="tried execution";
            if (statusCode == 200) {
                responseString = EntityUtils.toString(r_entity);
                // responseString="okay so whats up";
                Log.e("post response", responseString);
            } else {
                responseString = "error";
            }
        } catch (ClientProtocolException e) {
            responseString = e.toString();
            Log.e("clientprotocal", "error");
        } catch (IOException e) {
            responseString = e.toString();
            Log.e("error io", "error");
        }
        catch (JSONException er)
        {

        }

        return responseString;

    }

    public static String SaveLostApache(Context context, final String title, final String category, final String description, final String latitude, final String longitude, final String contactname, final String telephone, final String location) {
        Log.e("post response", "started");
        String  url = Data.server + "reportlost.php";
        String responseString ="";
        HttpClient httpclient = getHttpClient();
        HttpPost httppost = new HttpPost(url);
        try {
            Log.e("post response", "started");
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            JSONObject o = new JSONObject();
            o.put("title", title);
            o.put("category", category);
            o.put("latitude",latitude);
            o.put("longitude",longitude);
            o.put("contact_name",contactname);
            o.put("contact_telephone",telephone);
            o.put("location",location);
            o.put("description", description);
            entity.addPart("submit", new StringBody(""));
            entity.addPart("json", new StringBody(o.toString()));
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            responseString="tried execution";
            if (statusCode == 200) {
                responseString = EntityUtils.toString(r_entity);
                // responseString="okay so whats up";
                Log.e("post response", responseString);
            } else {
                responseString = "error";
            }
        } catch (ClientProtocolException e) {
            responseString = e.toString();
            Log.e("clientprotocal", "error");
        } catch (IOException e) {
            responseString = e.toString();
            Log.e("error io", "error");
        }
        catch (JSONException er)
        {

        }

        return responseString;

    }
    public static String delete(final Context context, final String id) {
        final String  url = Data.server + "delete.php";
        String responseString ="";
        final HttpClient client = getHttpClient();
        HttpPost httppost = new HttpPost(url);
        new AsyncTask<Void,Void,String>()
        {
            @Override
            protected String doInBackground(Void... params) {
                HttpPost post = new HttpPost(url);
                post.setEntity(uefa);
                HttpResponse response;
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("UserID",Data.UserID(context) ));
                Log.e("id",Data.UserID(context));
                param.add(new BasicNameValuePair("delete","" ));
                param.add(new BasicNameValuePair("id",id));
                try {
                    uefa = new UrlEncodedFormEntity(param);
                    response = client.execute(post);
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                    Log.e("delete response", result);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //result=s;
                Log.e("on post", s);
            }
        }.execute();

        return result;

    }
    public static Boolean LocalRegistryreg(Context context, String fullname, String telephone,
                                    String user_id, String email,String roleid) {
        Boolean result = false;
        try {

            DBhelper help = new DBhelper(context);
            SQLiteDatabase db = help.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("fullname", fullname);
            values.put("telephone", telephone);
            values.put("user_id", user_id);
            values.put("email", email);
            values.put("roleid", roleid);
            db.insert("users", null, values);
            result=true;
            //db.execSQL("insert into users (fullname,telephone,email,user_id,roleid) values ('"+fullname+"','"+telephone+"','"+email+"','"+user_id+"','"+roleid+"')");
//           result=true;

        } catch (Exception er) {

        }
        return  result;
    }
    public static int getSeatNumberExecutive(int gridnumber)
    {
        int result=1;
        switch (gridnumber)
        {
            case 1:
                result=1;
                break;
            case 2:
                result=2;
                break;
            ///////////////////////second row
            case 6:
                result=3;
                break;
            case 7:
                result=4;
                break;
           //////////////empty

            case 9:
                result=5;
                break;
            case 10:
                result=6;
                break;
            //////////////////////////////////third row///////////////////
            /*
            entrance
            case 11:
                result=7;
                break;
            case 12:
                result=7;
                break;
                */
            //////////////////13 empty

            case 14:
                result=7;
                break;
            case 15:
                result=8;
                break;

            /////////////////////////////////////////row aftaer entrance

            case 16:
                result=9;
                break;
            /*
            staff
            case 17:
                result=9;
                break;
                empty
            case 18:
                result=10;
                break;
*/
            case 19:
                result=10;
                break;
            case 20:
                result=11;
                break;
/////////////////////////////////////////////////////////row
            case 21:
                result=12;
                break;
            case 22:
                result=13;
                break;
            ////////23 empty
            case 24:
                result=14;
                break;
            case 25:
                result=15;
                break;

            ////////////////////////////////////////row
            case 26:
                result=16;
                break;
            case 27:
                result=17;
                break;
///////////////////28 empty
            case 29:
                result=18;
                break;
            case 30:
                result=19;
                break;

            //////////////////////////////////sixth row///////////////////

            case 31:
                result=20;
                break;
            case 32:
                result=21;
                break;
/////////////////33 is empty

            case 34:
                result=22;
                break;
            case 35:
                result=23;
                break;

            //////////////////////////////////seventh row///////////////////
            case 36:
                result=24;
                break;
            case 37:
                result=25;
                break;
            /////////////38 corridor
            case 39:
                result=26;
                break;

            case 40:
                result=27;
                break;
//////////////////////////////////////row

            case 41:
                result=28;
                break;
            case 42:
                result=29;
                break;
///empty 43
            case 44:
                result=30;
                break;
            case 45:
                result=31;
                break;
////////////////////////////////////////////row

            case 46:
                result=32;
                break;
            case 47:
                result=33;
                break;
            //////empty
            case 49:
                result=34;
                break;
            case 50:
                result=35;
                break;
//////////////////////////////// row
            case 51:
                result=36;
                break;
            case 52:
                result=37;
                break;
            //53 empty
            case 54:
                result=38;
                break;
            case 55:
                result=39;
                break;

            //////////////////////////////////10th row///////////////////
            case 56:
                result=40;
                break;
            case 57:
                result=41;
                break;
            case 58:
                result=42;
                break;
////////////////////////////// 58  is empty
            case 59:
                result=43;
                break;
            case 60:
                result=44;
                break;
/*
            //////////////////////////////////11th row///////////////////
            case 61:
                result=44;
                break;
            case 62:
                result=45;
                break;

            case 63:
                result=46;
                break;
            case 64:
                result=47;
                break;
            case 65:
                result=48;
                break;
*/
            default:
                result=1;
        }
        return result;
    }
    public static int getSeatNumberExecutiveClassic(int gridnumber)
    {
        int result=1;
        switch (gridnumber)
        {
            /*
            case 1:
                result=1;
                break;
            case 2:
                result=2;
                break;
                */
            ///////////////////////second row
            case 6:
                result=1;
                break;
            /*
            case 7:
                result=4;
                break;
                */
            //////////////empty



            case 9:
                result=2;
                break;
            case 10:
                result=3;
                break;
            //////////////////////////////////third row///////////////////

            case 11:
                result=4;
                break;
            case 12:
                result=5;
                break;

            //////////////////13 empty

            case 14:
                result=6;
                break;
            case 15:
                result=7;
                break;

            /////////////////////////////////////////row aftaer entrance

            case 16:
                result=8;
                break;
            case 17:
                result=9;
                break;
           ///18 corr
            case 19:
                result=10;
                break;
            case 20:
                result=11;
                break;


            /////////////////////////////////////////////////////////row
            case 21:
                result=12;
                break;
            case 22:
                result=13;
                break;
            ////////23 empty
            case 24:
                result=14;
                break;
            case 25:
                result=15;
                break;

            ////////////////////////////////////////row
            case 26:
                result=16;
                break;
            case 27:
                result=17;
                break;
///////////////////28 empty
            case 29:
                result=18;
                break;
            case 30:
                result=19;
                break;

            //////////////////////////////////sixth row///////////////////

            case 31:
                result=20;
                break;
            case 32:
                result=21;
                break;
/////////////////33 is empty

            case 34:
                result=22;
                break;
            case 35:
                result=23;
                break;

            //////////////////////////////////seventh row///////////////////
            case 36:
                result=24;
                break;
            case 37:
                result=25;
                break;
            /////////////38 corridor
            case 39:
                result=26;
                break;

            case 40:
                result=27;
                break;
//////////////////////////////////////row

            case 41:
                result=28;
                break;
            case 42:
                result=29;
                break;
///empty 43
            case 44:
                result=30;
                break;
            case 45:
                result=31;
                break;
////////////////////////////////////////////row

            case 46:
                result=32;
                break;
            case 47:
                result=33;
                break;
            //////empty
            case 49:
                result=34;
                break;
            case 50:
                result=35;
                break;
//////////////////////////////// row
            case 51:
                result=36;
                break;
            case 52:
                result=37;
                break;
            //53 empty
            case 54:
                result=38;
                break;
            case 55:
                result=39;
                break;

            //////////////////////////////////10th row///////////////////
            case 56:
                result=40;
                break;
            case 57:
                result=41;
                break;
////////////////////////////// 58  is empty
            case 59:
                result=42;
                break;
            case 60:
                result=43;
                break;
            default:
                result=1;
        }
        return result;
    }
    public static int getSeatNumberOrdinary(int gridnumber)
    {
        int result=1;
        switch (gridnumber)
        {
            case 1:
                result=1;
                        break;
            case 2:
                result=2;
                break;
            ///////////////////////second row
            case 7:
                result=3;
                break;
            case 8:
                result=4;
                break;
//////////////////////////////////////9 corridor
            case 10:
                result=5;
                break;
            case 11:
                result=6;
                break;
            case 12:
                result=7;
                break;
            //////////////////////////////////third row///////////////////
            case 13:
                result=8;
                break;
            case 14:
                result=9;
                break;
//////////////////////////////15 corridor
            case 16:
                result=10;
                break;
            case 17:
                result=11;
                break;
            case 18:
                result=12;
                break;


            //////////////////////////////////fouth row///////////////////
            case 19:
                result=13;
                break;
            case 20:
                result=14;
                break;
////////////////////////////////////////////////////corridor 21
            case 22:
                result=15;
                break;
            case 23:
                result=16;
                break;
            case 24:
                result=17;
                break;

            //////////////////////////////////fith row///////////////////
            /*
            case 25:
                result=;
                break;
            case 26:
                result=19;
                break;
                */
//entrance
            /////////27 empty corr
            case 28:
                result=18;
                break;
            case 29:
                result=19;
                break;
            case 30:
                result=20;
                break;


            //////////////////////////////////sixth row///////////////////

            //staff
            /*
            case 31:
                result=21;
                break;
            */
            case 32:
                result=21;
                break;
           ///////////////33 corr

            case 34:
                result=22;
                break;
            case 35:
                result=23;
                break;
            case 36:
                result=24;
                break;

            //////////////////////////////////seventh row///////////////////
            case 37:
                result=25;
                break;
            case 38:
                result=26;
                break;
/////////////////////////39 corr
            case 40:
                result=27;
                break;
            case 41:
                result=28;
                break;
            case 42:
                result=29;
                break;

            //////////////////////////////////eighth row///////////////////
            case 43:
                result=30;
                break;
            case 44:
                result=31;
                break;
//////////////////////45 is cor
            case 46:
                result=32;
                break;
            case 47:
                result=33;
                break;
            case 48:
                result=34;
                break;

            //////////////////////////////////9th row///////////////////
            case 49:
                result=35;
                break;
            case 50:
                result=36;
                break;
////////////////////////////////51 is croo
            case 52:
                result=37;
                break;
            case 53:
                result=38;
                break;
            case 54:
                result=39;
                break;

            //////////////////////////////////10th row///////////////////
            case 55:
                result=40;
                break;
            case 56:
                result=41;
                break;
///////57 cor
            case 58:
                result=42;
                break;
            case 59:
                result=43;
                break;
            case 60:
                result=44;
                break;

            //////////////////////////////////11th row///////////////////
            case 61:
                result=46;
                break;
            case 62:
                result=46;
                break;
///////////63 cor
            case 64:
                result=47;
                break;
            case 65:
                result=48;
                break;
            case 66:
                result=49;
                break;

            //////////////////////////////////12th row///////////////////
            case 67:
                result=50;
                break;
            case 68:
                result=51;
                break;
//////////////////69 cor
            case 70:
                result=52;
                break;
            case 71:
                result=53;
                break;
            case 72:
                result=54;
                break;


            //////////////////////////////////13th row///////////////////
            case 73:
                result=55;
                break;
            case 74:
                result=56;
                break;
////////////75 cor
            case 76:
                result=57;
                break;
            case 77:
                result=58;
                break;
            case 78:
                result=59;
                break;

            //////////////////////////////////14th row///////////////////
            case 79:
                result=60;
                break;
            case 80:
                result=61;
                break;
            case 81:
                result=62;
                break;

            case 82:
                result=63;
                break;
            case 83:
                result=64;
                break;
            case 84:
                result=65;
                break;

            default:
                result=1;

        }
        return result;
    }
    public static Boolean isTaken(ArrayList<String> taken,int seat) {
    Boolean result=false;
        for(int u=0; u<taken.size(); u++)
        {
            if(seat==Integer.valueOf(taken.get(u)))
            {
                result=true;
            }
        }
        return result;
    }
    public static ArrayList<Seat>getOrdinarySeats(ArrayList<String> taken)
    {
        ArrayList<Seat>result=new ArrayList<>();
        for(int x=1; x<=84; x++)
        {
            int seatNo=getSeatNumberOrdinary(x);
            if(x <=2)
            {
                if(isTaken(taken,seatNo)) {
                    result.add(new Seat("Seat " + seatNo, "taken"));
                }
                else
                {
                    result.add(new Seat("Seat " + seatNo, "available"));
                }
            }
            if((x==3) || (x==4) || (x==5))
            {
                result.add(new Seat("","empty"));
            }
            if((x==6))
            {
                result.add(new Seat("","driver"));
            }
            if ((x >6) && (x<85)) {

                if((x==9) || (x==15) || (x==21)||(x==27) || (x==33) || (x==39)
                        ||(x==45) || (x==51) || (x==57) ||(x==63) || (x==69) || (x==75))
                {
                    result.add(new Seat("","empty"));
                }
                else if((x==25) || (x==26))
                {
                    //these is door
                    result.add(new Seat("Door","door"));
                }

                else  if((x==31))
                {
                    //staff
                    result.add(new Seat("staff","staff"));
                }
                else {
                    if(isTaken(taken,seatNo)) {
                        result.add(new Seat("Seat " + seatNo, "taken"));
                    }
                    else
                    {
                        result.add(new Seat("Seat " + seatNo, "available"));
                    }
                }
            }

        }
        return  result;
    }

    public static ArrayList<Seat>getExcecutiveSeats(ArrayList<String> taken)
    {
        ArrayList<Seat>result=new ArrayList<>();
        for(int x=1; x<=60; x++)
        {
            int seatNo=getSeatNumberExecutive(x);
            if(x <=2)
            {
                if(isTaken(taken,seatNo)) {
                    result.add(new Seat("Seat " + seatNo, "taken"));
                }
                else
                {
                    result.add(new Seat("Seat " + seatNo, "available"));
                }
            }
            if((x==3) || (x==4))
            {
                result.add(new Seat("","empty"));
            }
            else if((x==5))
            {
                result.add(new Seat("","driver"));
            }
            else  if (x >5) {
                int z=3;
                int inc=5;

                if((x==8) || (x==13) || (x==18)  || (x==23) || (x==28)||(x==33) || (x==38) || (x==43)|| (x==48)
                        || (x==53))
                {
                    result.add(new Seat("","empty"));
                }

                /*
                if((x==z))
                {
                    result.add(new Seat("","empty"));
                }
                */
                else if((x==11) || (x==12))
                {
                    //these is door
                    result.add(new Seat("Door","door"));
                }

                else if((x==17))
                {
                    //staff
                    result.add(new Seat("staff","staff"));
                }
                else {
                    if(isTaken(taken,seatNo)) {
                        result.add(new Seat("Seat " + seatNo, "taken"));
                    }
                    else
                    {
                        result.add(new Seat("Seat " + seatNo, "available"));
                    }
                }
                z+=inc;
            }

        }
        return  result;
    }


    public static ArrayList<Seat>getExcecutiveSeatsClassic(ArrayList<String> taken)
    {
        ArrayList<Seat>result=new ArrayList<>();
        for(int x=1; x<=60; x++)
        {
            int seatNo=getSeatNumberExecutiveClassic(x);
            if(x <=2)
            {
                if(isTaken(taken,seatNo)) {
                    result.add(new Seat("Seat " + seatNo, "door"));
                }
                else
                {
                    result.add(new Seat("Seat " + seatNo, "door"));
                }
            }
            if((x==3) || (x==4))
            {
                result.add(new Seat("","empty"));
            }
            else if((x==5))
            {
                result.add(new Seat("","driver"));
            }
            else  if (x >5) {
                 if((x==8) || (x==13) || (x==18)  || (x==23) || (x==28)||(x==33) || (x==38) || (x==43)|| (x==48)
                        || (x==53) || (x==58))
                {
                    result.add(new Seat("","empty"));
                }

                else if((x==7))
                {
                  //staff
                    result.add(new Seat("","staff"));
                }
                else {
                    if(isTaken(taken,seatNo)) {
                        result.add(new Seat("Seat " + seatNo, "taken"));
                    }
                    else
                    {
                        result.add(new Seat("Seat " + seatNo, "available"));
                    }
                }
            }

        }
        return  result;
    }
    public static Location getLocation(Context context)
    {

        long mLocTrackingInterval = 1000 * 5; // 5 sec
        float trackingDistance = 0;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance)
                .setInterval(mLocTrackingInterval);
        SmartLocation.with(context)
                .location()
                .continuous()
                .config(builder.build())
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location2) {
                        location=location2;
                        Log.e("here","i reached");
                    }
                });
        SmartLocation.with(context).location().stop();
        SmartLocation.with(context).activity().stop();
        return  location;
    }
    public static LatLng LatLong(Context context)
    {
       Location loc=getLocation(context);
       return  new LatLng(loc.getLatitude(),loc.getLongitude());
    }
    public static JSONObject JSON(String result)
    {
        JSONObject obj=null;
        try {
             obj = new JSONObject(result);
        }
        catch (Exception er)
        {

        }
        return obj;
    }
    public static JSONArray JARRAY(String result)
    {
        JSONArray obj=null;
        try {
            obj = new JSONArray(result);
        }
        catch (Exception er)
        {

        }
        return obj;
    }
    public static String getTicketDetails(Context context,String ticket)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","getTicketdetails"));
        param.add(new BasicNameValuePair("UserID",Data.UserID(context) ));
        param.add(new BasicNameValuePair("CompanyID",Data.getEnterpriseCompanyID(context)));
        param.add(new BasicNameValuePair("TicketID",ticket));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
            Log.e("ticket details  "+ticket,result);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String addPickup(Context context,String name,String latitude,String longitude,String route)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","addPickUp"));
        param.add(new BasicNameValuePair("UserID",Data.User_Id(context) ));
        param.add(new BasicNameValuePair("CompanyID",Data.getEnterpriseCompanyID(context) ));
        param.add(new BasicNameValuePair("RouteID",route));
        param.add(new BasicNameValuePair("address",name));
        param.add(new BasicNameValuePair("latitude",latitude));
        param.add(new BasicNameValuePair("longitude",longitude));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static ArrayList<Menu> getMenuList()
    {
        ArrayList<Menu>result=new ArrayList<>();
        result.add(new Menu("Book",R.drawable.book));
        result.add(new Menu("Checkin",R.drawable.checkin));
        result.add(new Menu("Tickets",R.drawable.ticket));
        result.add(new Menu("About us",R.drawable.info));
        result.add(new Menu("Contact Us",R.drawable.callphone));
        result.add(new Menu("Exit",R.drawable.power));
        return result;
    }
    public static double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        double res=Radius * c;
        Log.e("dist","distance in km between is "+res);
        return res;

    }
    public static  HttpClient getHttpClient()
    {
        return new MyHttpClient().getNewHttpClient();
    }
    public static String paymentAPI(Context context,String amount,String description,String ticket)
    {
        String result="";
        //then the site posts to pegasus
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        Vendor vendor=new Vendor();
        String dataTosign=vendor.VendorCode
                + vendor.MerchantId +
                amount + description + vendor.CURRENCY +
                vendor.REDIRECT + ticket;
        String digitalKey= EncryptFile.encryptSha256(dataTosign);
        param.add(new BasicNameValuePair("VENDORCODE",vendor.VendorCode));
        param.add(new BasicNameValuePair("MERCHANTCODE",vendor.MerchantId));
        param.add(new BasicNameValuePair("PASSWORD",vendor.PASSWORD));
        param.add(new BasicNameValuePair("VENDOR_TRANID",ticket));
        param.add(new BasicNameValuePair("ITEM_TOTAL",amount));
        param.add(new BasicNameValuePair("ITEM_DESCRIPTION",description));
        param.add(new BasicNameValuePair("RETURN_URL",vendor.REDIRECT));
        param.add(new BasicNameValuePair("DIGITAL_SIGNATURE",digitalKey));
        param.add(new BasicNameValuePair("CURRENCY",amount));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String TrackNow(Context context,String schedule,String trip)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","TrackNow"));
        param.add(new BasicNameValuePair("UserID",Data.UserID(context) ));
        param.add(new BasicNameValuePair("schedule",schedule));
        param.add(new BasicNameValuePair("TripID",trip));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static String RateTrip(Context context,String trip,String rate,String comment)
    {
        String result="";
        final String url2 = Data.server + "Api.php";
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url2);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("cmd","RateTrip"));
        param.add(new BasicNameValuePair("UserID",Data.UserID(context) ));
        param.add(new BasicNameValuePair("trip",trip));
        param.add(new BasicNameValuePair("rate",rate));
        param.add(new BasicNameValuePair("comment",comment));
        try {
            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse res=client.execute(post);
            HttpEntity entity=res.getEntity();
            result=EntityUtils.toString(entity);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
