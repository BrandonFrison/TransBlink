package com.example.brandon.transblink;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class LocationStation extends AppCompatActivity {
    private ArrayList<Station> stations;
    private Station stationDistance[];
    private GPS gps;
    private Location currentLocation = null;
    private double latitude = 0.0;
    private double longitude = 0.0;
    ListStationAdapater adapater;
    ListView listStation;
    private int themeSel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent =  getIntent();
        ThemeChange themeChg = new ThemeChange();
        themeSel = themeChg.findTheme(this);
        setTheme(themeSel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_station);

        stations = (ArrayList<Station>) intent.getSerializableExtra("stations");
        listStation = (ListView)findViewById(R.id.listViewNearbyStations);
        stationDistance = new Station[stations.size()];

        gpsTracking();
        findNearStation();

        listStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LocationStation.this, TripPlanner.class);
                intent.putExtra("caller","LocationStation");
                intent.putExtra("startStation",stationDistance[i]);
                intent.putExtra("stations",stations);

                finish();
                startActivity(intent);
            }
        });
    }

    private void gpsTracking(){
        final Handler mHandler = new Handler();
        gps = GPS.getGPSInstance(this);
        currentLocation = gps.getCurrentLocation();
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        gps.setOnGPSEventListener(new GPS.GPSEventListener(){
            @Override
            public void onReceivedEvent(){
                Log.d("LocationStation", "onReceivedEvent");
                currentLocation = gps.getCurrentLocation();
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                findNearStation();
            }
        });
    }

    private void findNearStation() {
        if(currentLocation != null){
            Location newLocation = new Location("");

            for (Station station : stations) {
                newLocation.setLatitude(station.getLatitude());
                newLocation.setLongitude(station.getLongitude());
                double distance = currentLocation.distanceTo(newLocation);
                station.setDistance(distance);
                Log.d(station.getFullName(), distance + "m");
            }

            stations.toArray(stationDistance);
            Arrays.sort(stationDistance, new Comparator<Station>() {

                @Override
                public int compare(Station o1, Station o2) {
                    return (int)(o1.getDistance() - o2.getDistance());
                }
            });

            if(adapater == null){
                adapater = new ListStationAdapater(this, stationDistance, ListStationAdapater.DISP.NEARSTATION);
                listStation.setAdapter(adapater);
            }

            adapater.notifyDataSetChanged();
        }
    }

    public void showMap(View v){
        if( currentLocation != null ){
            Intent intent = new Intent(LocationStation.this, MapsActivity.class);

            intent.putExtra("caller","nearStation");                // set caller information to Map
            intent.putExtra("currentLatitude", latitude);
            intent.putExtra("currentLongitude", longitude);
            intent.putExtra("nearStations", stationDistance);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Please wait receiving location",Toast.LENGTH_SHORT).show();
        }
    }
}
