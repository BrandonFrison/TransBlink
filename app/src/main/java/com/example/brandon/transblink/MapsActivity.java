package com.example.brandon.transblink;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Station[] stations;
    String caller;
    double currentLatitude;
    double currentLongitude;
    ArrayList<Station> routesht;
    private PolylineOptions poly = new PolylineOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        caller = (String)intent.getStringExtra("caller");

        switch (caller){
            case "nearStation":
                currentLatitude = (Double)intent.getDoubleExtra("currentLatitude",49.262623 );
                currentLongitude = (Double)intent.getDoubleExtra("currentLongitude",-123.069258 );
                stations = (Station[])intent.getSerializableExtra("nearStations");
                break;
            case "tripRoute":
                routesht = (ArrayList<Station>) intent.getSerializableExtra("route");
                try {
                    setRoute();
                } catch (Exception e) {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                }
                break;
        }


        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }





    public void setRoute() throws IOException {
        String line = "";
        poly.color(getResources().getColor(R.color.lightAccent));
        poly.width(50);
        for(int i = 0; i < routesht.size()-1; i++) {

            AssetManager am = this.getAssets();
            InputStream is = am.open("coord.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\*");
                String[] statName = data[0].split(",");
                if (statName[0].equals(routesht.get(i).getCode()) && statName[1].equals(routesht.get(i+1).getCode()))
                {
                    for(int y = 1; y < data.length; y++ )
                    {

                        String[] holder = data[y].split(",");
                        Double[] lonlat = new Double[2];
                        lonlat[0] = Double.parseDouble(holder[0]);
                        lonlat[1] = Double.parseDouble(holder[1]);
                        poly.add(new LatLng(lonlat[0], lonlat[1]));
                    }
                }
                else if(statName[0].equals(routesht.get(i+1).getCode()) && statName[1].equals(routesht.get(i).getCode()))
                {
                    for(int y = data.length; y > 1; y-- )
                    {
                        String[] holder = data[y-1].split(",");
                        Double[] lonlat = new Double[2];
                        lonlat[0] = Double.parseDouble(holder[0]);
                        lonlat[1] = Double.parseDouble(holder[1]);
                        poly.add(new LatLng(lonlat[0], lonlat[1]));
                    }
                }
            }
            br.close();
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        switch(caller){
            case "nearStation":
                LatLng current = new LatLng(currentLatitude, currentLongitude);
                mMap.addMarker(new MarkerOptions().position(current).title("Your Location")).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 13));
                for(Station station : stations){
                    addMarker(station);
                }
                break;
            case "tripRoute":
                current = new LatLng(routesht.get(0).getLatitude(),(routesht.get(0).getLongitude()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 13));
                mMap.addPolyline(poly);
                for(Station station : routesht)
                {
                    addMarker(station);
                }


        }
    }

    private void addMarker(Station station){
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.stationlogo))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(station.getLatitude(), station.getLongitude()))
                .title(station.getFullName())
        );
    }

}
