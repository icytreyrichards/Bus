package proximity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import localdata.Data;

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
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.easybus.R;


@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class ProxMap extends AppCompatActivity implements
		OnMyLocationChangeListener, LocationSource, OnLocationChangedListener,
		LocationListener,OnMapReadyCallback {

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
	private double latitude, longitude;

	private UrlEncodedFormEntity uefa = null;
	LocationManager locationManager;
	PendingIntent pendingIntent;
	SharedPreferences sharedPreferences;
	private LatLng point;
	String notificationTitle;
	String notificationContent;
	String tickerMessage;

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

			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,
					longitude)));

			// Setting the zoom level in the map
			map.animateCamera(CameraUpdateFactory.zoomTo(20));
			drawCircle(point);

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
		params.add(new BasicNameValuePair("user_id", Data.User_Id(ProxMap.this)));
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
		markerOptions.snippet(status);
		markerOptions.title(loc);

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
			Utility.shoAlert("you are 20 metres away from "+loc, ProxMap.this);
		} else {
			Toast.makeText(getBaseContext(), "Exiting the region",
					Toast.LENGTH_LONG).show();
			notificationTitle = "Proximity - Exit";
			notificationContent = "Exited the region";
			tickerMessage = "Exited the region";
			Utility.shoAlert("exited proximity region", ProxMap.this);

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
				.setSmallIcon(R.drawable.ic_drawer).setAutoCancel(true)
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