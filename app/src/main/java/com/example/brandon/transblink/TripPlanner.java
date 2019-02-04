package com.example.brandon.transblink;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TripPlanner extends AppCompatActivity {
    AutoCompleteTextView autoCompleteStart;
    AutoCompleteTextView autoCompleteStop;

    List<String> stationDisp;
    ArrayList<Station> stations;
    private int themeSel;
    ListStationAdapater adapater;
    Path[] pathOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent =  getIntent();
        ThemeChange themeChg = new ThemeChange();
        themeSel = themeChg.findTheme(this);
        setTheme(themeSel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planner);

        String caller = (String)intent.getSerializableExtra("caller");
        stations = (ArrayList<Station>) intent.getSerializableExtra("stations");
        stationDisp = new ArrayList<>();

        for(int i = 0; i < stations.size(); i++)
        {
            Station station = stations.get(i);
            stationDisp.add(station.getFullName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stationDisp);

        //old code delete after:
        autoCompleteStart = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewStarting);
        autoCompleteStop = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewEnding);

        autoCompleteStart.setAdapter(adapter);
        autoCompleteStop.setAdapter(adapter);

        switch (caller){
            case "LocationStation":
                Station startStation = (Station)intent.getSerializableExtra("startStation");
                Log.d("startStation",startStation.getFullName());
                autoCompleteStart.setText(startStation.getFullName());
                break;

            case "MainActivity":

                break;

        }

        int i =0;

    }

    public void findRoute(View v)
    {
        ListView listViewPossibleRoutes = (ListView)findViewById(R.id.listViewPossibleRoutes);
        Station start = DataProcessor.findStation(MainActivity.masterList, autoCompleteStart.getText().toString());
        Station end = DataProcessor.findStation(MainActivity.masterList, autoCompleteStop.getText().toString());

        if(start == null || end == null){
            Toast.makeText(this, "Please select Start & Stop stations",Toast.LENGTH_LONG).show();
            return;
        }else if(start == end){
            Toast.makeText(this, "Start & Stop stations should be different",Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<Path> paths = DataProcessor.findRoutes(start, end);

        pathOptions = new Path[paths.size()];
        paths.toArray(pathOptions);

        adapater = new ListStationAdapater(this, pathOptions, ListStationAdapater.DISP.ROUTE_OPTIONS);
        listViewPossibleRoutes.setAdapter(adapater);

        adapater.notifyDataSetChanged();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(listViewPossibleRoutes.getWindowToken(),0);

        listViewPossibleRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TripPlanner.this, RouteDisplay.class);
                intent.putExtra("caller","TripPlanner");
                intent.putExtra("route",pathOptions[i]);
                //finish();
                startActivity(intent);
            }
        });
    }
}
