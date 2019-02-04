package com.example.brandon.transblink;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by alee on 3/11/2017.
 */

public class Station implements Serializable
{
    private String fullName;
    private String code;
    private int zone;
    private boolean isOpen;
    private boolean hasConstruction;
    private boolean isTransferPoint;
    private double latitude;
    private double longitude;
    private double distance;
    private Lines[] lines;

    public ArrayList<String> connectingStations; //should this be public?
    public static enum Lines {
        Millennium, Canada, Expo
    }
    //Constructor
    //note: once all this is initialized, there should be no reason to change the information ie no need for mutator functions
    Station(String n, String c, int z, boolean o, boolean con, boolean t, String connectionString, double latitude, double longitude, String lineInfo)
    {
        this.fullName = n;
        this.code = c;
        this.zone = z;
        this.isOpen = o;
        this.hasConstruction = con;
        this.isTransferPoint = t;
        this.latitude = latitude;
        this.longitude = longitude;

        //updateConnectionStations(connectionString);
        connectingStations = new ArrayList<String>();
        if (connectionString.contains(":"))
        {
            String[] connections = connectionString.split(":");

            for (int i = 0; i < connections.length; i++)
            {
                connectingStations.add(connections[i]);
            }
        }
        else
        {
            connectingStations.add(connectionString);
        }

        updateLineInfo(lineInfo);
    }

    //why does this exist
    private void updateConnectionStations(String connectionString){
        connectingStations = new ArrayList<String>();

        if (connectionString.contains(":"))
        {
            String[] connections = connectionString.split(":");

            for (int i = 0; i < connections.length; i++)
            {
                connectingStations.add(connections[i]);
            }
        }
        else
        {
            connectingStations.add(connectionString);
        }
    }

    //what is this? a station can be on multiple lines
    private void updateLineInfo(String lineInfo){
        String[] arrLine = lineInfo.split(":");
        lines = new Lines[arrLine.length];

        for(int i=0; i<arrLine.length; i++){
            Lines getLine = null;
            switch(arrLine[i]){
                case "EXPO":
                    getLine = Lines.Expo;
                    break;

                case "MILL":
                    getLine = Lines.Millennium;
                    break;

                case "CAN":
                    getLine = Lines.Canada;
                    break;
            }
            lines[i] = getLine;
        }
    }

    public String getFullName()
    {
        return this.fullName;
    }

    public String getCode()
    {
        return this.code;
    }

    public int getZone()
    {
        return this.zone;
    }

    public boolean getOpen()
    {
        return this.isOpen;
    }

    public boolean getConstruction()
    {
        return this.hasConstruction;
    }

    public boolean getTransferPoint()
    {
        return this.isTransferPoint;
    }

    public double getDistance() { return this.distance; }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitude() { return this.latitude; }

    public double getLongitude() { return this.longitude; }

    public Lines[] getLines() { return this.lines; }
}
