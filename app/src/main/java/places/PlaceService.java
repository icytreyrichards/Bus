package places;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import BusinessLogic.LogicClass;

/**
 * Created by Richard.Ezama on 1/30/2018.
 */

public class PlaceService {
    static String API="AIzaSyA-gMwaBA8APYxFqFavZJDz_cgWJ3gITjM";
    static UrlEncodedFormEntity uefa;
    public static ArrayList<String> getPlaces(String input)
    {
        ArrayList<String> result=new ArrayList<>();
        String jsonResults="";
        try {
            String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
            String TYPE_AUTOCOMPLETE = "/autocomplete";
            String OUT_JSON = "/json";
            String url=PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON+"?key="+API+"&types=(regions)&input="+ URLEncoder.encode(input,"utf8");
            HttpClient client = LogicClass.getHttpClient();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> param = new ArrayList<NameValuePair>();

            uefa = new UrlEncodedFormEntity(param);
            post.setEntity(uefa);
            HttpResponse resp=client.execute(post);
            HttpEntity entity=resp.getEntity();
            jsonResults= EntityUtils.toString(entity);
            //Log.e("fcm",jsonResults);
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            // Log.d(TAG, jsonResults.toString());

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            result = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                result.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
        }
        return result;
    }
}
