package network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import BusinessLogic.LogicClass;

@SuppressLint("NewApi") public class NetworkAdmin {
	public UrlEncodedFormEntity uefa;
	public String url;
	static String json;
	private static int intresponse;
	private static ArrayList<String> albumid, albums;
	static String result = "";
	//public static HttpClient client = LogicClass.getHttpClient();
	public static HttpClient client =new DefaultHttpClient();
	public NetworkAdmin(Context context, UrlEncodedFormEntity uefa, String url) {
		this.url = url;
		this.uefa = uefa;
		//client = LogicClass.getHttpClient();
	}
	public static String jsonarray(final UrlEncodedFormEntity uefa, final String url) {
		new AsyncTask<Void,Void,String>()
		{
			@Override
			protected String doInBackground(Void... params) {
				HttpPost post = new HttpPost(url);
				post.setEntity(uefa);
				HttpResponse response;
				try {
					response = client.execute(post);
					HttpEntity entity = response.getEntity();
					result = EntityUtils.toString(entity);
					Log.e("network admin response", result);
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
}
