package network;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

@SuppressLint("NewApi")
//have different methods in there
public class InternetHandler extends AsyncTask<Void, Integer, String> {
	public UrlEncodedFormEntity uefa;
	public String url;
	static String json;
	static String result = null;
	public Tracker interface_result = null;

	public InternetHandler(UrlEncodedFormEntity uefa, String url,Tracker response) {
		interface_result = response;
		this.url=url;
		this.uefa=uefa;
	}

	@Override
	protected void onPreExecute() {
		interface_result.pre_execute();
		
	}

	@Override
	protected String doInBackground(Void... arg0) {
		String result = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setEntity(uefa);
		HttpResponse response;
		try {
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			Log.e("", result);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			interface_result.onError();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			interface_result.onError();
		}
		return result;

	}
	// Show Dialog Box with Progress bar
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		json = result;
		interface_result.onDone(json);

	}


}
