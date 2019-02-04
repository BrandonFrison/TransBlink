package com.example.brandon.transblink;

import android.content.Intent;
import android.content.res.AssetManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import android.content.Context;

public class MainActivity extends AppCompatActivity
{
    public static ArrayList<Station> masterList;
    public int themeSel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChange themeChg = new ThemeChange();
        themeSel = themeChg.findTheme(this);
        setTheme(themeSel);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initialize();

        //ALGORITHM TESTING: UNCOMMENT AT OWN RISK
        //RUN THIS SHIT IN DEBUG IF YOU WANT TO SEE ANYTHING
        /*
        Station start = DataProcessor.findStation(masterList, "WTF");
        Station end = DataProcessor.findStation(masterList, "BRD");
        ArrayList<Path> paths = DataProcessor.findRoutes(start, end);
        //System.out.println(DataProcessor.findStation(masterList, "CMB").getTransferPoint());

        for (int i = 0; i < paths.size(); i++)
        {
            System.out.println("PATH NO." + (i+1) + " --------------------");
            for (int j = 0; j < paths.get(i).pathStops.size(); j++)
            {
                System.out.println(paths.get(i).pathStops.get(j).getFullName());
            }
        }
        */

    }

    //why is do we need to call this single method that only calls a single method?
    private void initialize()
    {
        try
        {
            initStations();
        }
        catch (IOException ex)
        {
            //some shit goes here
        }
    }


    // Station initialize with station information string
    private void initStations() throws IOException
    {
        masterList = new ArrayList<Station>();

        // FILE READING HERE //////////////////////////////////////////
        AssetManager am = this.getAssets();
        InputStream is = am.open("data.txt");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String line = "";

        while ((line = br.readLine()) != null)
        {
            String[] data = line.split(",");
            Station temp = new Station(
                    data[0],                            //Data0 : Station Full name
                    data[1],                            //Data1 : Station Short name
                    Integer.parseInt(data[2]),          //Data2 : Zone Number
                    Boolean.parseBoolean(data[3]),      //Data3 : Open Status
                    Boolean.parseBoolean(data[4]),      //Data4 : Construction Status
                    Boolean.parseBoolean(data[5]),      //Data5 : Transfer Flag
                    data[6],                            //Data6 : Station Connection Info
                    Double.parseDouble(data[7]),        //Data7 : Station Latitude
                    Double.parseDouble(data[8]),        //Data8 : Station Longitude
                    data[9]                             //Data9 : Line information
            );
            masterList.add(temp);
        }
    }

    // Setter and Getters ---------------------------------------------------
    public ArrayList<Station> getMasterList(){
        return this.masterList;
    }

    //activity teleporters ---------------------------------------------------
    public void goToTripPlanner(View v)
    {
        Intent intent = new Intent(MainActivity.this, TripPlanner.class);
        intent.putExtra("caller","MainActivity");
        intent.putExtra("stations",masterList);

        startActivity(intent);
    }

    public void goToNearestStation(View v)
    {
        GPS gps = GPS.getGPSInstance(this);
        if(gps.isGetLocation()) {
            Intent intent = new Intent(MainActivity.this, LocationStation.class);
            intent.putExtra("stations", masterList);
            startActivity(intent);
        }else{
            gps.showSettingsAlert();
        }
    }


    public void goToThemeChange(View v)
    {
        Intent intent = new Intent(MainActivity.this, ThemeChange.class);

        startActivity(intent);
    }

}
