package utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import localdata.DBhelper;
import localdata.Data;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

@SuppressLint("NewApi")
public class Utility extends Activity {
	public UrlEncodedFormEntity uefa;
	public String url;
	public static int res = 0;
	public String json = "[{'response':'no data'}]";
	public String jsondata = "{'success':'0'}";
	public static NetWorkTask task;
	public static JsonTask jsontask;
	public static JsonTaskObjects stats;
	String result = "";

	public Utility(UrlEncodedFormEntity uefa, String url) {
		this.url = url;
		this.uefa = uefa;
		task = new NetWorkTask();
		task.execute();

	}

	public Utility(Context context, UrlEncodedFormEntity uefa, String url) {
		this.url = url;
		this.uefa = uefa;
		stats = new JsonTaskObjects();
		stats.execute();
	}

	public static int plays() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public String json() {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	public String jsonobject() {
		Log.e("the value am returning is ", jsondata);
		return jsondata;
	}

	public class NetWorkTask extends AsyncTask<Void, Integer, Integer> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			post.setEntity(uefa);
			HttpResponse response;
			try {
				response = client.execute(post);
				HttpEntity entity = response.getEntity();
				String data = EntityUtils.toString(entity);
				Log.e("gcm response from server", data);
				

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return res;

		}

		// Show Dialog Box with Progress bar
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		}

	}

	public class JsonTask extends AsyncTask<Void, Integer, String> {
		@Override
		protected void onPreExecute() {
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
				Log.e("horse mouth", result);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;

		}

		// Show Dialog Box with Progress bar
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			json = result;
			Log.e("after execution", result + " compare " + json);
		}

	}

	public class JsonTaskObjects extends AsyncTask<Void, Integer, String> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(Void... arg0) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			post.setEntity(uefa);
			HttpResponse response;
			try {
				response = client.execute(post);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
				Log.e("staticts data", result);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;

		}

		// Show Dialog Box with Progress bar
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			jsondata = result;
			jsondata.notify();

		}

	}

	public static Boolean Logout(Context context) {
		DBhelper h = new DBhelper(context);
		SQLiteDatabase db = h.getWritableDatabase();
		db.execSQL("delete from users");
		db.execSQL("delete from routes");
		db.execSQL("delete from prices");
		db.execSQL("delete from schedules");
		db.execSQL("delete from tickets");
		db.execSQL("delete from gcm");
		db.execSQL("delete from enterprise");
		db.execSQL("delete from status");
		db.execSQL("delete from pickups");
		db.execSQL("delete from liveTrip");
		//
		LoginManager.getInstance().logOut();
		//logoutTwitter(context);
		return true;
	}
	public static void logoutTwitter(Context context) {
		TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
		if (twitterSession != null) {
			ClearCookies(context);
			Twitter.getSessionManager().clearActiveSession();
			Twitter.logOut();
		}
	}

	public static void ClearCookies(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
			CookieManager.getInstance().removeAllCookies(null);
			CookieManager.getInstance().flush();
		} else {
			CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
			cookieSyncMngr.startSync();
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.removeAllCookie();
			cookieManager.removeSessionCookie();
			cookieSyncMngr.stopSync();
			cookieSyncMngr.sync();
		}
	}

	public static boolean isrecord(Context context, String query) {
		boolean result = false;
		DBhelper help = new DBhelper(context);
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cur = db.rawQuery(query, null);
		int row = cur.getCount();
		Log.e("records", "" + row);
		if (row > 0) {
			result = false;
		}

		else if (row == 0) {
			result = true;
		}

		cur.close();
		db.close();
		return result;
	}

	public static boolean isDownloadManagerAvailable(Context context) {
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				return false;
			}
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setClassName("com.android.providers.downloads.ui",
					"com.android.providers.downloads.ui.DownloadList");
			List<ResolveInfo> list = context.getPackageManager()
					.queryIntentActivities(intent,
							PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	public static String DownloadFromUrl(String DownloadUrl, String fileName,
			Context context) {
		String path = "";
		try {
			File root = android.os.Environment.getExternalStorageDirectory();
			path = root.getAbsolutePath() + "/musicstore/downloads";
			File dir = new File(root.getAbsolutePath()
					+ "/musicstore/downloads");
			// start checking this file with a thread
			if (dir.exists() == false) {
				dir.mkdirs();
			}
			URL url = new URL(DownloadUrl); // you can write here any link
			File file = new File(dir, fileName);

			long startTime = System.currentTimeMillis();
			Log.d("DownloadManager", "download begining");
			Log.d("DownloadManager", "download url:" + url);
			Log.d("DownloadManager", "downloaded file name:" + fileName);

			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(5000);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.flush();
			fos.close();
			Log.d("DownloadManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

		} catch (IOException e) {
			Log.d("DownloadManager", "Error: " + e);
		}
		return path + fileName;
	}

	public static boolean isInternetAvailable(Context context) {
		boolean result = false;
		NetworkInfo info = (NetworkInfo) ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null) {
			Log.d("internet", "no internet connection");
			result = false;
		} else {
			if (info.isConnected()) {
				Log.d("internet", " internet connection available...");
				result = true;
			} else {
				Log.d("internet", " internet connection");
				result = true;
			}

		}
		return result;

	}

	public static boolean isonline(Context context) {
		boolean result = false;
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {

			result = true;

		} else if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

			result = false;
		}
		return result;
	}

	public static void shoAlert(String message, Context context) {
		android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(
				context);
		alert.setMessage(message);
		alert.setTitle("Notice");
		alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		alert.show();
	}
	//////////////
	public static void showAlert(String message, Context context) {
		android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(
				context);
		alert.setMessage(message);
		alert.setTitle("Alert");
		alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		alert.show();
	}

	private static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	

	@SuppressLint("NewApi")
	public void downloadFile(String url, String songname, Context context) {
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(url));
		request.setDescription("downloading " + songname);
		request.setTitle(songname);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			request.allowScanningByMediaScanner();
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}
		request.setDestinationInExternalFilesDir(getApplicationContext(), null,
				songname + ".mp3");
		// get download service and enqueue file
		DownloadManager manager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		manager.enqueue(request);
	}

	public static String storagepath() {
		File root = android.os.Environment.getExternalStorageDirectory();
		String path = root.getAbsolutePath() + "/musicstore/music";
		File dir = new File(path);
		return path;
	}
	
}
