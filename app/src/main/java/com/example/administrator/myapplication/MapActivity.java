package com.example.administrator.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.util.Calendar;

/*---------------------------------------------------------------------------------
--	CLASS FILE:	    MapActivity.java -
--
--	PROGRAM:		Android GPS app
--
--	METHODS:      
--					protected void onCreate(Bundle savedInstanceState)
--					public void onMapReady(GoogleMap googleMap)
--					private void enableMyLocation()
--					public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
--					protected void onDestroy()
--					public void onConnected(Bundle connectionHint) 
--					public void run()
--
--
--	DATE:			March 14, 2016
--
--	DESIGNER:		Tom Tang
--
--	PROGRAMMER:		Tom Tang
--
--	NOTES:
--	This file contains the code for the MapActivity of the application. That is the main
--  activity that is displayed when the app is running. The activity will display where the
--  user is on a map and also send user infomation to the server to be displayed.
--
---------------------------------------------------------------------------------*/
public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback,
                    GoogleMap.OnMyLocationButtonClickListener,
                    ActivityCompat.OnRequestPermissionsResultCallback,
                    GoogleApiClient.ConnectionCallbacks{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Handler mHandler = new Handler();
    private long mInterval = 10000;

    private String mName;
    private String mId;
    private boolean centered = false;
	
	
	/*---------------------------------------------------------------------------------
	--	METHOD:     onCreate
	--
	--	DATE:		March 14, 2014
	--
	--	DESIGNER:	Tom Tang
	--
	--	PROGRAMMER:	Tom Tang
	--
	--	INTERFACE:	protected void onCreate(Bundle savedInstanceState)
	--
	--  PARAMETERS: Bundle savedInstanceState a Bundle containing the activity's previously frozen state
	--
	--	RETURNS:	void
	--
	--	NOTES:
	--	Entry point of the activity. It initializes the user info, establishes a connection 
	--  with the server, starts google maps and starts a connection with the google api.
	--
	---------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        mName = intent.getStringExtra("name");
        mId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

		//establishes a connection with the server
        new NetworkTask().execute(intent.getStringExtra("ip"),intent.getStringExtra("port"));
		
		//initializes google api client
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }
		//starts google maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

	/*---------------------------------------------------------------------------------
	--	METHOD:     onMapReady
	--
	--	DATE:		March 14, 2014
	--
	--	DESIGNER:	Tom Tang
	--
	--	PROGRAMMER:	Tom Tang
	--
	--	INTERFACE:	public void onMapReady(GoogleMap googleMap)
	--
	--  PARAMETERS: GoogleMap googleMap googleMap object
	--
	--	RETURNS:	void
	--
	--	NOTES:
	--	Calledback method for when google maps is ready. This method enables my location
	--  finding and my location button when called
	--
	---------------------------------------------------------------------------------*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }
	
	/*---------------------------------------------------------------------------------
	--	METHOD:     enableMyLocation
	--
	--	DATE:		March 14, 2014
	--
	--	DESIGNER:	Tom Tang
	--
	--	PROGRAMMER:	Tom Tang
	--
	--	INTERFACE:	private void enableMyLocation() 
	--
	--  PARAMETERS: void
	--
	--	RETURNS:	void
	--
	--	NOTES:
	--	Method that enabled location finding and requests location permissions
	--
	---------------------------------------------------------------------------------*/
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            mMap.setMyLocationEnabled(true);
            mGoogleApiClient.connect();
        }
    }
	
	/*---------------------------------------------------------------------------------
	--	METHOD:     onRequestPermissionsResult
	--
	--	DATE:		March 14, 2014
	--
	--	DESIGNER:	Tom Tang
	--
	--	PROGRAMMER:	Tom Tang
	--
	--	INTERFACE:	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) 
	--
	--  PARAMETERS: int requestCode       request code passed in from permission request
	--				String[] permissions  array of permissions asked for
	--				int[] grantResults    array of ints representing whether  or not permissions have been granted
	--
	--	RETURNS:	void
	--
	--	NOTES:
	--	Callback method for permission requests.
	--
	---------------------------------------------------------------------------------*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) throws SecurityException{
        if (permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        }
    }
	
	/*---------------------------------------------------------------------------------
	--	METHOD:     onDestroy
	--
	--	DATE:		March 14, 2014
	--
	--	DESIGNER:	Tom Tang
	--
	--	PROGRAMMER:	Tom Tang
	--
	--	INTERFACE:	protected void onDestroy()
	--
	--  PARAMETERS: void
	--
	--	RETURNS:	void
	--
	--	NOTES:
	--	Destructor for the program that closes the google api connection and server socket
	--
	---------------------------------------------------------------------------------*/
	@Override
    protected void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }
	
	/*---------------------------------------------------------------------------------
	--	METHOD:     onConnected
	--
	--	DATE:		March 14, 2014
	--
	--	DESIGNER:	Tom Tang
	--
	--	PROGRAMMER:	Tom Tang
	--
	--	INTERFACE:	onConnected(Bundle connectionHint)
	--
	--  PARAMETERS: Bundle connectionHint   Bundle of data provided to clients by Google Play services. 
	--										May be null if no content is provided by the service. 
	--
	--	RETURNS:	void
	--
	--	NOTES:
	--	Callback method for when google api connection is established
	--
	---------------------------------------------------------------------------------*/
    @Override
    public void onConnected(Bundle connectionHint) throws SecurityException{
        mGetLocation.run();
    }

    Runnable mGetLocation = new Runnable() {
        @Override
		
		/*---------------------------------------------------------------------------------
		--	METHOD:     run
		--
		--	DATE:		March 14, 2014
		--
		--	DESIGNER:	Tom Tang
		--
		--	PROGRAMMER:	Tom Tang
		--
		--	INTERFACE:	onConnected()
		--
		--  PARAMETERS: void 
		--
		--	RETURNS:	void
		--
		--	NOTES:
		--	Periodic method that sends user data to the server.
		--
		---------------------------------------------------------------------------------*/
        public void run() throws SecurityException{{

                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if(location != null) {
                    if(!centered) {
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,15));
                        centered = true;
                    }
                    String time = Calendar.getInstance().getTime().toString(); ;
                    JSONObject json = new JSONObject();

                    try {
                        json.put("Lat", location.getLatitude());
                        json.put("Long", location.getLongitude());
                        json.put("Name", mName);
                        json.put("Time", time);
                        json.put("ID", mId);

                        new NetworkTask().execute(json.toString());
                    }catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            mHandler.postDelayed(mGetLocation, mInterval);
        }
    };

    @Override
    public boolean onMyLocationButtonClick() {return false;}

    @Override
    public void onConnectionSuspended(int k){}
}
