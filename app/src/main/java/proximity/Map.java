package proximity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import localdata.Data;
import map.DirectionsJSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import utility.Utility;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.my.easybus.R;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class Map extends AppCompatActivity implements
		OnMyLocationChangeListener, LocationSource, OnLocationChangedListener,
		LocationListener ,OnMapReadyCallback {

	GoogleMap map;
	ArrayList<LatLng> markerPoints;
	private String json = null;
	private LatLng myposition;
	private Toolbar toolbar;
	private LatLng origin;
	private String reporter = null;
	private String fault_id = null;
	private String distkm, time;
	private String loc, status;
	private double homelat = 0, homelng = 0, latitude, longitude;
	String notificationTitle;
	String notificationContent;
	String tickerMessage;

	private UrlEncodedFormEntity uefa = null;
	LocationManager locationManager;
	PendingIntent pendingIntent;
	SharedPreferences sharedPreferences;
	private LatLng point;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.themap);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.inflateMenu(R.menu.map_menu);
		toolbar.setTitleTextColor(Color.WHITE);
		//toolbar.setBackgroundColor(getResources().getColor(R.color.bluish));
		toolbar.setNavigationIcon(R.drawable.ic_drawer);
		toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				int id = item.getItemId();
				
				return false;
			}
		});
		if (!isGooglePlayServicesAvailable()) {
			// finish();
		} else {
			MarkerOptions options = new MarkerOptions();

			Intent i = getIntent();
			json = i.getStringExtra("json");

			// Initializing
			markerPoints = new ArrayList<LatLng>();
			// Getting reference to SupportMapFragment of the activity_main
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			// Getting Map for the SupportMapFragment
			fm.getMapAsync(this);
			map.setOnMyLocationChangeListener(this);
			// map.setLocationSource(this);
			// Enable MyLocation Button in the Map
			map.setMyLocationEnabled(true);
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			String bestProvider = locationManager.getBestProvider(criteria,
					true);
			Location location = locationManager
					.getLastKnownLocation(bestProvider);
			// Opening the sharedPreferences object
			sharedPreferences = getSharedPreferences("location", 0);

			// Getting stored latitude if exists else return 0
			String lat = sharedPreferences.getString("lat", "0");

			// Getting stored longitude if exists else return 0
			String lng = sharedPreferences.getString("lng", "0");

			// Getting stored zoom level if exists else return 0
			String zoom = sharedPreferences.getString("zoom", "0");

			if (location != null) {
				onLocationChanged(location);
			}
			locationManager
					.requestLocationUpdates(bestProvider, 20000, 0, this);
			if (location != null) {
				origin = new LatLng(location.getLatitude(),
						location.getLongitude());
			}

			JSONObject obj;
			try {
				Log.e("map received", json);
				obj = new JSONObject(json);
				reporter = obj.getString("user_id");
				status = obj.getString("status");
				fault_id = obj.getString("id");

				latitude = Double.valueOf(obj.getString("latitude"));
				longitude = Double.valueOf(obj.getString("longitude"));
				point = new LatLng(latitude, longitude);
				loc = obj.getString("location");
				if (status.equalsIgnoreCase("pending")) {
					options.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				} else if (status.equalsIgnoreCase("assigned")) {
					options.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
				} else if (status.equalsIgnoreCase("in proggress")) {
					options.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
				} else if (status.equalsIgnoreCase("done")) {
					options.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				}
				// loc
				options.title(loc);
				options.snippet(status);
				options.position(new LatLng(latitude, longitude));
				options.snippet(status);
				// Add new marker to the Google Map Android API V2
				map.addMarker(options);
				toolbar.setTitle(loc);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// get my location
			// Setting onclick event listener for the map
			// myposition=new LatLng(latitude, longitude);
			// Moving CameraPosition to previously clicked position
			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,
					longitude)));

			// Setting the zoom level in the map
			map.animateCamera(CameraUpdateFactory.zoomTo(20));
			map.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					// TODO Auto-generated method stub
					if ((homelat != marker.getPosition().latitude)
							&& (homelng != marker.getPosition().longitude)) {
						LatLng dest = marker.getPosition();
						LatLng point = marker.getPosition();

						String url = getDirectionsUrl(origin, dest);
						DownloadTask downloadTask = new DownloadTask();
						downloadTask.execute(url);
						Intent proximityIntent = new Intent(
								"proximity.proximity");
						proximityIntent.putExtra("json", json);

						// Creating a pending intent which will be invoked by
						// LocationManager when the specified region is
						// entered or exited
						pendingIntent = PendingIntent.getActivity(
								getBaseContext(), 0, proximityIntent,
								Intent.FLAG_ACTIVITY_NEW_TASK);

						// Setting proximity alert
						// The pending intent will be invoked when the device
						// enters or exits the region 20 meters
						// away from the marked point
						// The -1 indicates that, the monitor will not be
						// expired
						locationManager.addProximityAlert(point.latitude,
								point.longitude, 20, -1, pendingIntent);

						/**
						 * Opening the editor object to write data to
						 * sharedPreferences
						 */
						SharedPreferences.Editor editor = sharedPreferences
								.edit();

						/**
						 * Storing the latitude of the current location to the
						 * shared preferences
						 */
						editor.putString("lat", Double.toString(point.latitude));

						/**
						 * Storing the longitude of the current location to the
						 * shared preferences
						 */
						editor.putString("lng",
								Double.toString(point.longitude));

						/** Storing the zoom level to the shared preferences */
						editor.putString("zoom",
								Float.toString(map.getCameraPosition().zoom));

						/** Saving the values stored in the shared preferences */
						editor.commit();

						Toast.makeText(getBaseContext(),
								"Proximity Alert is added", Toast.LENGTH_SHORT)
								.show();
					}

					return false;
				}
			});
			if (lat != null) {
				HandleProximity();

			}
		}

	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			MarkerOptions markerOptions = new MarkerOptions();
			String distance = "";
			String duration = "";

			if (result.size() < 1) {
				Toast.makeText(getBaseContext(), "No Points",
						Toast.LENGTH_SHORT).show();
				return;
			}

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					if (j == 0) { // Get distance from the list
						distance = (String) point.get("distance");
						continue;
					} else if (j == 1) { // Get duration from the list
						duration = (String) point.get("duration");
						continue;
					}

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);
				lineOptions.color(Color.RED);

			}
			String durastance = "Distance:" + distance + ", Duration:"
					+ duration;
			distkm = distance;
			time = duration;
			map.clear();
			// ////
			markerOptions.snippet("Distance " + distance + " time to drive "
					+ time);
			markerOptions.title(loc);
			markerOptions.position(lineOptions.getPoints().get(
					lineOptions.getPoints().size() - 1));
			// Drawing polyline in the Google Map for the i-th route
			map.addPolyline(lineOptions);
			map.addMarker(markerOptions);

			// /////////

			markerOptions.snippet(status);
			markerOptions.title(loc);
			markerOptions.position(lineOptions.getPoints().get(0));
			// Drawing polyline in the Google Map for the i-th route
			map.addPolyline(lineOptions);
			map.addMarker(markerOptions);
			homelat = lineOptions.getPoints().get(
					lineOptions.getPoints().size() - 1).latitude;
			homelng = lineOptions.getPoints().get(
					lineOptions.getPoints().size() - 1).latitude;

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_menu, menu);
		return true;
	}

	@Override
	public void onMyLocationChange(Location location) {
		// TODO Auto-generated method stub
		myposition = new LatLng(location.getLatitude(), location.getLongitude());
		origin = new LatLng(location.getLatitude(), location.getLongitude());

	}

	@Override
	public void activate(OnLocationChangedListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		myposition = new LatLng(location.getLatitude(), location.getLongitude());

	}

	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
			return false;
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	private void set_status(String status) {
		String url = Data.server + "set_status.php";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("status", status));
		params.add(new BasicNameValuePair("user_id", Data.User_Id(Map.this)));
		params.add(new BasicNameValuePair("fault_id", fault_id));
		try {
			uefa = new UrlEncodedFormEntity(params);
			Utility ut = new Utility(uefa, url);
			String result = ut.jsonobject();
			Log.e("online response", result);
			JSONObject o = new JSONObject(result);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void drawCircle(LatLng point) {

		// Instantiating CircleOptions to draw a circle around the marker
		CircleOptions circleOptions = new CircleOptions();

		// Specifying the center of the circle
		circleOptions.center(point);

		// Radius of the circle
		circleOptions.radius(20);

		// Border color of the circle
		circleOptions.strokeColor(Color.BLACK);

		// Fill color of the circle
		circleOptions.fillColor(0x30ff0000);

		// Border width of the circle
		circleOptions.strokeWidth(2);

		// Adding the circle to the GoogleMap
		map.addCircle(circleOptions);

	}

	private void drawMarker(LatLng point) {
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);

		// Adding marker on the Google Map
		map.addMarker(markerOptions);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_hybrid) {
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

			return true;
		} else if (id == android.R.id.home) {
			finish();
			return true;
		} else if (id == R.id.action_normal) {
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			return true;
		} else if (id == R.id.action_add_alert) {
			Intent proximityIntent = new Intent("proximity.proximity");
			proximityIntent.putExtra("json", json);

			// Creating a pending intent which will be invoked by
			// LocationManager when the specified region is
			// entered or exited
			pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
					proximityIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

			// Setting proximity alert
			// The pending intent will be invoked when the device
			// enters or exits the region 20 meters
			// away from the marked point
			// The -1 indicates that, the monitor will not be
			// expired
			locationManager.addProximityAlert(point.latitude, point.longitude,
					20, -1, pendingIntent);

			/**
			 * Opening the editor object to write data to sharedPreferences
			 */
			SharedPreferences.Editor editor = sharedPreferences.edit();

			/**
			 * Storing the latitude of the current location to the shared
			 * preferences
			 */
			editor.putString("lat", Double.toString(point.latitude));

			/**
			 * Storing the longitude of the current location to the shared
			 * preferences
			 */
			editor.putString("lng", Double.toString(point.longitude));

			/** Storing the zoom level to the shared preferences */
			editor.putString("zoom",
					Float.toString(map.getCameraPosition().zoom));

			/** Saving the values stored in the shared preferences */
			editor.commit();

			Toast.makeText(getBaseContext(), "Proximity Alert is added",
					Toast.LENGTH_SHORT).show();

			return true;
		} else if (id == R.id.action_remove_alert) {
			Intent proximityIntent = new Intent("proximity.proximity");
			pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
					proximityIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

			// Removing the proximity alert
			locationManager.removeProximityAlert(pendingIntent);

			// Removing the marker and circle from the Google Map

			// Opening the editor object to delete data from sharedPreferences
			SharedPreferences.Editor editor = sharedPreferences.edit();

			// Clearing the editor
			editor.clear();

			// Committing the changes
			editor.commit();

			Toast.makeText(getBaseContext(), "Proximity Alert is removed",
					Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void HandleProximity() {
		boolean proximity_entering = getIntent().getBooleanExtra(
				LocationManager.KEY_PROXIMITY_ENTERING, true);

		if (proximity_entering) {
			Toast.makeText(getBaseContext(), "Entering the region",
					Toast.LENGTH_LONG).show();
			notificationTitle = "Proximity - Entry";
			notificationContent = "Entered the region";
			tickerMessage = "Entered the region";
			Utility.shoAlert("you are 20 metres away", Map.this);
		} else {
			Toast.makeText(getBaseContext(), "Exiting the region",
					Toast.LENGTH_LONG).show();
			notificationTitle = "Proximity - Exit";
			notificationContent = "Exited the region";
			tickerMessage = "Exited the region";
			Utility.shoAlert("exited proximity region", Map.this);

		}
		Intent notificationIntent = new Intent(getApplicationContext(),
				Map.class);
		notificationIntent.putExtra("content", notificationContent);
		notificationIntent.putExtra("json", json);

		/**
		 * This is needed to make this intent different from its previous
		 * intents
		 */
		notificationIntent.setData(Uri.parse("tel:/"
				+ (int) System.currentTimeMillis()));

		/**
		 * Creating different tasks for each notification. See the flag
		 * Intent.FLAG_ACTIVITY_NEW_TASK
		 */
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, notificationIntent,
				Intent.FLAG_ACTIVITY_NEW_TASK);

		/** Getting the System service NotificationManager */
		NotificationManager nManager = (NotificationManager) getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);

		/** Configuring notification builder to create a notification */
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				getApplicationContext()).setWhen(System.currentTimeMillis())
				.setContentText(notificationContent)
				.setContentTitle(notificationTitle)
				.setSmallIcon(R.drawable.logo).setAutoCancel(true)
				.setTicker(tickerMessage).setContentIntent(pendingIntent);

		/** Creating a notification from the notification builder */
		Notification notification = notificationBuilder.build();

		/**
		 * Sending the notification to system. The first argument ensures that
		 * each notification is having a unique id If two notifications share
		 * same notification id, then the last notification replaces the first
		 * notification
		 * */
		nManager.notify((int) System.currentTimeMillis(), notification);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map=googleMap;
	}
}