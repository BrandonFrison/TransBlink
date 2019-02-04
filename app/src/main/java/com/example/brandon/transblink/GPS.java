package com.example.brandon.transblink;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by alee on 3/11/2017.
 */

public class GPS implements LocationListener {
    private static GPS instance = null;
    private final Context mContext;
    private LocationManager locationManager;

    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean isGetLocation = false;

    private Location location;
    private Location currentLocation;

    private double lat;
    private double lon;

    private int testCnt = 0;
    private GPSEventListener mGPSEventListener;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    // Minimum GPS information update period 1s
    private static final long MIN_TIME_BW_UPDATES = 1000;

    //Singleton implementation of getting instance
    public static GPS getGPSInstance(Context context){
        if (instance == null) {
            instance = new GPS(context);
        }
        instance.initGPSService();
        return instance;
    }

    // Private constructor for singleton
    private GPS(Context context)     {
        Log.d("GPS","GpsInfo create");
        this.mContext = context;
    }

    public void initGPSService() {
        Log.d("GPS","initGPSService");

        try {
            if(isGetLocation()){
                // Getting Data From Network
                if (isNetworkEnabled) {
                    Log.d("GPS","isNetworkEnabled");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        updateCoordinates(location);
                    }
                }
                // Getting Data From GPS
                if (isGPSEnabled) {
                    Log.d("GPS","isGPSEnabled");
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            updateCoordinates(location);
                        }
                    }
                }
            }else{
//                showSettingsAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface GPSEventListener{
        void onReceivedEvent();
    }

    public void setOnGPSEventListener(GPSEventListener listener){
        mGPSEventListener = listener;
    }

    public double getLatitude(){ return lat; }

    public double getLongitude(){ return lon; }

    public Location getCurrentLocation(){
        return this.currentLocation;
    }

    public boolean isGetLocation() {
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        // GPS Setting Status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // Network Setting Status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(isGPSEnabled || isNetworkEnabled)
            isGetLocation = true;

        return isGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Location Setup");
        alertDialog.setMessage("Location Service is not available. Would you setup this option?");

        // OK
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
        // Cancle
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    //Save Location
    private void updateCoordinates(Location location){
        if (location != null) {
            currentLocation = location;
            lat = location.getLatitude();
            lon = location.getLongitude();
            Log.d("GPS","updateCoordinates :" + lat + ", "+ lon);

            // Event Listener call
            if(mGPSEventListener != null)
                mGPSEventListener.onReceivedEvent();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        Log.d("GPS","onLocationChanged");
        testCnt++;
        updateCoordinates(location);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("GPS","onStatusChanged");
        // TODO Auto-generated method stub
    }

    public void onProviderEnabled(String provider) {
        Log.i("GPS","onStatusChanged");
        // TODO Auto-generated method stub
    }

    public void onProviderDisabled(String provider) {
        Log.i("GPS","onStatusChanged");
        // TODO Auto-generated method stub
    }

//    public void stopUsingGPS(){
//        if(locationManager != null){
//            locationManager.removeUpdates(GpsInfo.this);
//        }
//    }
}
