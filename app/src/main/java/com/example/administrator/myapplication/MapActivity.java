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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        mName = intent.getStringExtra("name");
        mId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        new NetworkTask().execute(intent.getStringExtra("ip"),intent.getStringExtra("port"));

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }

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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) throws SecurityException{
            if (permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mGoogleApiClient.connect();
            }
        }

    @Override
    public void onConnected(Bundle connectionHint) throws SecurityException{
        mGetLocation.run();
    }

    Runnable mGetLocation = new Runnable() {
        @Override
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
