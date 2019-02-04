package com.example.brandon.transblink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RouteDisplay extends AppCompatActivity {
    public int themeSel;
    ArrayList<Station> pathStations;
    Station[] routeStations;
    ListView listViewSelectedRoute;
/*
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(RouteDisplay.this, TripPlanner.class);

        //not sure what to put here but it definetly needs either the list with the possible routes or the text fields entered in trip planner.
        //intent.putExtra("stations", );

        startActivity(intent);
        finish();

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent =  getIntent();
        ThemeChange themeChg = new ThemeChange();
        themeSel = themeChg.findTheme(this);
        setTheme(themeSel);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_display);

        listViewSelectedRoute = (ListView)findViewById(R.id.listViewStationByStationRoute);
        String caller = (String)intent.getSerializableExtra("caller");
        Path path = (Path)intent.getSerializableExtra("route");

        pathStations = path.pathStops;

        for(int i=0; i< pathStations.size(); i++) {
            Log.i("RouteDisplay route :", pathStations.get(i).getFullName());
        }
//        routeStations = new Station[pathStations.size()];
//        pathStations.toArray(routeStations);

        ListStationAdapater adapter = new ListStationAdapater(this, path, ListStationAdapater.DISP.ROUTE_DISPLAY);
        listViewSelectedRoute.setAdapter(adapter);
    }

    public void drawRouteOnMap(View v){
        Intent intent = new Intent(RouteDisplay.this, MapsActivity.class);

        intent.putExtra("caller","tripRoute");
        intent.putExtra("route",pathStations);

        startActivity(intent);
    }
}
