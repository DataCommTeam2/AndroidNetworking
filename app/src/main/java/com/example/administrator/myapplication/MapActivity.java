package com.example.administrator.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback,
                    GoogleMap.OnMyLocationButtonClickListener,
                    ActivityCompat.OnRequestPermissionsResultCallback,
                    GoogleApiClient.ConnectionCallbacks{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Handler mHandler;
    private long mInterval = 5000;

    private String mName;
    private String mIp;
    private String mPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        mName = intent.getStringExtra("name");
        mIp = intent.getStringExtra("ip");
        mPort = intent.getStringExtra("port");

        mHandler = new Handler();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();

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
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mGetLocation.run();
    }

    Runnable mGetLocation = new Runnable() {
        @Override
        public void run() {
            if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if(location != null) {
                    final String text = location.getLatitude() + "|" + location.getLongitude();

                    MapActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MapActivity.this, text, Toast.LENGTH_LONG).show();
                        }
                    });
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
