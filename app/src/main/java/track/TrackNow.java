package track;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.my.easybus.DetailsDriver;
import com.my.easybus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import BusinessLogic.GoogleMapsTasks;
import BusinessLogic.LogicClass;
import Entities.Account;
import adapters.AccountAdapter;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import localdata.Data;
import map.DirectionsJSONParser;
import places.PlaceService;
import utility.Utility;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class TrackNow extends AppCompatActivity implements OnMapReadyCallback,
		OnLocationUpdatedListener {
	private AccountAdapter adapter;
	private ArrayList<Account> list = new ArrayList<>();
	String mapdata = "";
	Boolean cancamera = false;
	GoogleMap map;
	ArrayList<LatLng> markerPoints;
	private LatLng myposition;
	private Toolbar toolbar;
	private LatLng origin;
	private String reporter = null;
	private String emergency_id = null, telephone = "";
	private String distkm, time, json = "",schedule,TripID="";
	private String loc = "Location", status = "", myaddress = "";
	private double homelat = 0, homelng = 0, latitude = 0.3476, longitude = 32.5825, mylatitude, mylongitude;
	// flag for GPS status
	boolean isGPSEnabled = false;
	// flag for network status
	boolean isNetworkEnabled = false;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
int miles=5,stroke=2;
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // 1 minut
	PendingIntent pendingIntent;
	SharedPreferences sharedPreferences;
	private LatLng point;
	Boolean cliked = false;
	private SeekBar seekbar;
	int MY_PERMISSION_LOCATION = 1;
	ImageView  livefeed;
	//private CollapsingToolbarLayout collapsingToolbarLayout;
	private ProgressDialog prgDialog;
	private TextView txupdates, tvpaneltitle,tvworker,tvstatus;
	private Button btnEndTrip;
	private int parsemode=1;
	private AutoCompleteTextView autocompleteView;
	private String input="";
	private ArrayAdapter<String> mAdapter;
	HandlerThread mHandlerThread;
	Handler mThreadHandler;
	private String patrolarray="";
	private TextView tvroute,tvbs,tvtime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);
		tvbs=(TextView)findViewById(R.id.tvbs);
		tvroute=(TextView)findViewById(R.id.tvroute);
		tvtime=(TextView)findViewById(R.id.tvtime);

		if (!isGooglePlayServicesAvailable()) {

		} else {
			MarkerOptions options = new MarkerOptions();
			new RequestTast().execute();
			// Initializing
			markerPoints = new ArrayList<LatLng>();
			// Getting reference to SupportMapFragment of the activity_main
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			// Getting Map for the SupportMapFragment
			fm.getMapAsync(this);
		}
		schedule=Data.getCurrentSchedule(this);
		autocompleteView = (AutoCompleteTextView)findViewById(R.id.autocomplete);
		autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Get data associated with the specified position
				// in the list (AdapterView)
				String description = (String) parent.getItemAtPosition(position);
			}
		});
		/*
		autocompleteView.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				final String value = s.toString();
				input=value;
				new PlacesTsk().execute();
			}
			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		*/

		btnEndTrip=(Button)findViewById(R.id.btnEndTrip);
		seekbar = (SeekBar) findViewById(R.id.seekBar);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.menu);
		getSupportActionBar().setTitle("Safari Now");
		origin = new LatLng(latitude, longitude);
		prgDialog = new ProgressDialog(this);
		btnEndTrip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, String>() {
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						prgDialog.setTitle("Ending Trip");
						prgDialog.setMessage("Please wait");
						prgDialog.show();
					}

					@Override
					protected String doInBackground(Void... voids) {
						String res = LogicClass.startJourney(TrackNow.this,schedule,0);
						return res;
					}

					@Override
					protected void onPostExecute(String result) {
						prgDialog.dismiss();
						super.onPostExecute(result);
						if(result.contains("{")) {
							try {
								JSONObject o = new JSONObject(result);
								String trip = o.getString("TripID");
								String msg = o.getString("msg");
								Data.stopTrip(TrackNow.this);
								Utility.shoAlert(msg,TrackNow.this);
							}
							catch (Exception er)
							{

							}
						}
						else
						{
							Utility.shoAlert(result,TrackNow.this);
						}
					}
				}.execute();
			}
		});

		try {
			StartGPS();
		} catch (Exception er) {
			int currentapiVersion = Build.VERSION.SDK_INT;
			if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
				requestLocation();
			}
		}
		memory();
		if(schedule.equals("0"))
		{
			btnEndTrip.setVisibility(View.GONE);
		}
		//BgTracker();
		TrackJob();

	}

	void memory() {
		 schedule=Data.getCurrentSchedule(this);
		 TripID=Data.getCurrentTrip(this);
		if(!schedule.equals("0"))
		{

		}
		//schedule="12";
		//Utility.shoAlert(TripID+" vs "+schedule,this);
	}

	private class DownloadTask extends AsyncTask<String, Void, String> {
		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {
			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = GoogleMapsTasks.downloadUrl(url[0]);
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
			Log.e("google data",result);
			ParserTask parserTask = new ParserTask();
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

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
			if(parsemode==1) {
				//map.clear();
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
				cliked = true;
			}
			else
			{
				tvtime.setText(durastance);
				//Utility.showAlert(durastance,Request.this);
			}

		}

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

	Boolean isInBounds(Circle circle, LatLng latlong) {
		Boolean res = false;
		float[] distance = new float[2];

		Location.distanceBetween(latlong.latitude, latlong.longitude, circle.getCenter().latitude, circle.getCenter().longitude, distance);

		if (distance[0] <= circle.getRadius()) {
			res = true;
		} else {
			res = false;
		}
		return res;
	}

	public class RequestTast extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			//String res= LogicClass.getEmergencies(Request.this);
			String res =LogicClass.getSchedule(TrackNow.this,"","","All","3");
			json = res;
			return res;
		}
		// Show Dialog Box with Progress bar
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			mapdata = result;
			super.onPostExecute(result);
			json = "" + result;
			//Utility.showAlert(result,TrackNow.this);
			startMaps(result);
			//Utility.shoAlert(result,NearMeOld.this);
		}
	}
	void DrawCircle()
	{
		Circle circle = map.addCircle(new CircleOptions()
				.center(new LatLng(origin.latitude, origin.longitude))
				.radius(miles)
				.strokeColor(Color.RED)
				.strokeWidth(stroke));
		circle.setVisible(false);
	}
	void startMaps(String result)
	{
		SetMap(result);

		//AddCenter();
	}
	void SetMap(String result) {
		if (!result.equals("")) {
			map.clear();
			MarkerOptions options = new MarkerOptions();
			Circle circle = map.addCircle(new CircleOptions()
					.center(new LatLng(origin.latitude, origin.longitude))
					.radius(miles) // Converting Miles into Meters...
					.strokeColor(Color.RED)
					.strokeWidth(stroke));
			//DrawCircle();
			try {
				JSONArray jarraymain = new JSONArray(result);
				int y=0;
				for (y=0; y<jarraymain.length();y++)
				{
					JSONObject o=jarraymain.getJSONObject(y);
					String latitude=o.getString("latitude");
					String longitude=o.getString("longitude");
					String speed=o.getString("speed");
					String bearing=o.getString("bearing");
					String company=o.getString("CompanyName");
					String time=o.getString("time");
					String route=o.getString("source")+"-"+o.getString("destination");
					loc=company+time+"\n";
					status="Speed "+speed+" Km/h "+route;
					if(!latitude.equals("0"))
					{
						options.title(loc);
						options.snippet(route);
						options.position(new LatLng(Double.valueOf(latitude),Double.valueOf(longitude)));
						options.snippet(status);
						// Add new marker to the Google Map Android API V2
						LatLng latlong =new LatLng(Double.valueOf(latitude),Double.valueOf(longitude));
						if (isInBounds(circle, latlong)) {
							options.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
						} else {
							options.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_RED));
						}
						options.icon(BitmapDescriptorFactory.fromResource(R.drawable.busmarker));
						options.rotation(Float.valueOf(bearing));
						options.flat(true);
						map.addMarker(options);
					}



				}
			}
			catch (Exception er)
			{
				Utility.showAlert(er.toString(),this);
			}


		} else {
			Utility.shoAlert("Check network Connectivity", this);
		}
	}
	@Override
	public void onMapReady(final GoogleMap map) {
		this.map = map;
		requestLocation();

		map.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				if ((homelat != marker.getPosition().latitude)
						&& (homelng != marker.getPosition().longitude)) {
					LatLng dest = marker.getPosition();
					LatLng point = marker.getPosition();
					status=marker.getSnippet();
                    /*
					String url = GoogleMapsTasks.getDirectionsUrl(origin, dest);
					if (!cliked) {
						DownloadTask downloadTask = new DownloadTask();
						downloadTask.execute(url);
					}
					*/
				}
				return false;
			}
		});
		map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
			@Override
			public void onCameraMove() {
				//Utility.Toast(Request.this,"moving");
				cancamera = true;
				//SetMap(mapdata, 5000);
			}

		});
		map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
			@Override
			public void onCameraIdle() {
				//do something
				cancamera=false;
			}
		});
		map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
			@Override
			public void onMarkerDragStart(Marker marker) {

		}

			@Override
			public void onMarkerDrag(Marker marker) {

			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
             //get the name of this place
				Decode(marker.getPosition());
			}
		});
		map.setMyLocationEnabled(true);
	}
	void requestLocation() {

		//only for higher version
		String[] perms = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};

		int permsRequestCode = 200;

		ActivityCompat.requestPermissions(this, perms, permsRequestCode);
	}

	public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
		switch (permsRequestCode) {

			case 200:
				if (grantResults.length > 0) {
					boolean islocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
					if (islocation) {
						map.setMyLocationEnabled(true);
						StartGPS();
					}
				} else {
					//requestLocation();
				}
				//Utility.showAlert(" read "+read +" write "+write,Login.this);
				break;
		}
	}
	@Override
	public void onLocationUpdated(Location location) {
		drawCircle(new LatLng(location.getLatitude(),location.getLongitude()));
		myposition = new LatLng(location.getLatitude(), location.getLongitude());
		origin = new LatLng(location.getLatitude(), location.getLongitude());
		mylatitude = location.getLatitude();
		mylongitude = location.getLongitude();
		String speed=location.getSpeed()+"";
		//Utility.shoAlert(location.getLongitude()+"",this);

		if(!schedule.equals("0")) {
	    LogicClass.startTracking(this, schedule,
			String.valueOf(location.getLatitude()), location.getLongitude()+"",
			location.getBearing() + "",speed,TripID);
}
			if (!cancamera) {
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(origin.latitude, origin.longitude), 15));
			//map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
			Decode(new LatLng(location.getLatitude(), location.getLongitude()));
			//get patrols and find the nearest
			new RequestTast().execute();
		} else {
			//decode at the center
		}
	}
	protected void Decode(LatLng latlng) {
		Geocoder geocorder = new Geocoder(this, Locale.US);
		try {
			List<Address> address = geocorder.getFromLocation(latlng.latitude, latlng.longitude, 1);
			//toolbar.setTitle(address.get(0).getFeatureName());
			myaddress = address.get(0).getAdminArea() + " " + address.get(0).getSubAdminArea() + ' ' + address.get(0).getFeatureName();
			String add=address.get(0).getFeatureName();
			txupdates.setText(add);
		} catch (Exception er) {

		}
	}
	protected void StartGPS() {
		long mLocTrackingInterval = 1000 *60*5; // 5 secs
		float trackingDistance = 0;
		LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

		LocationParams.Builder builder = new LocationParams.Builder()
				.setAccuracy(trackingAccuracy)
				.setDistance(trackingDistance)
				.setInterval(mLocTrackingInterval);

		SmartLocation.with(this)
				.location()
				.continuous()
				.config(builder.build())
				.start(this);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SmartLocation.with(this).location().stop();
		if (mThreadHandler != null) {
			mThreadHandler.removeCallbacksAndMessages(null);
			mHandlerThread.quit();
		}
	}

	public class GCMTast extends AsyncTask<Void, Integer, String> {
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			refreshedToken = FirebaseInstanceId.getInstance().getToken();
			return LogicClass.FCM(TrackNow.this, refreshedToken);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		}
	}

	@Override
	public void onBackPressed() {
		finish();
			}
	void BgTracker()
	{
		Handler handler = new Handler();
		final Runnable r = new Runnable() {
			public void run() {
				//TrackJob();
			}
		};
		//handler.postDelayed(r, 5000);
		if (!schedule.equals("0")) {
			TrackJob();
		}

	}
	void TrackJob() {

		new AsyncTask<Void, Void, String>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected String doInBackground(Void... voids) {
				String res = LogicClass.TrackNow(TrackNow.this, schedule,TripID);
				Log.e("tracker",res);
				return res;
			}
			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				//Utility.shoAlert(s,TrackNow.this);
					try
				{
					String res=new JSONArray(s).getJSONObject(0).toString();
                   JSONObject o=new JSONObject(res);
					String latitude=o.getString("latitude");
					String longitude=o.getString("longitude");
					String speed=o.getString("speed");
					String bearing=o.getString("bearing");
					String company=o.getString("CompanyName");
					String time=o.getString("time");
					String route=o.getString("source")+"-"+o.getString("destination");
					String title=company+" departure time "+time;
					String stampTrip="Trip Started "+o.getString("stampTrip");
					tvroute.setText(route);
					tvbs.setText(company);
					tvtime.setText(stampTrip);

				}
				catch (Exception er)
				{
					//Utility.shoAlert(er.toString(),TrackNow.this);
				}
			}
		}.execute();
	}

	public class PlacesTsk extends AsyncTask<Void, Integer, ArrayList<String>> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//progressBar2.setVisibility(View.VISIBLE);
		}
		@Override
		protected ArrayList<String> doInBackground(Void... arg0) {
			ArrayList<String> res = new ArrayList<>();
			try {
				res = PlaceService.getPlaces(input);
			} catch (Exception er) {
				prgDialog.dismiss();
			}
			return res;
		}
		// Show Dialog Box with Progress bar
		@Override
		protected void onPostExecute(ArrayList<String> results) {
			// TODO Auto-generated method stub
			super.onPostExecute(results);
			//progressBar2.setVisibility(View.GONE);
			mAdapter=new ArrayAdapter<String>(TrackNow.this,android.R.layout.simple_list_item_1,results);
			prgDialog.dismiss();
			if (results != null && results.size() > 0) {
				mAdapter.notifyDataSetChanged();
			}
			else {
				mAdapter.notifyDataSetInvalidated();
			}
			autocompleteView.setAdapter(mAdapter);
		}
	}

}