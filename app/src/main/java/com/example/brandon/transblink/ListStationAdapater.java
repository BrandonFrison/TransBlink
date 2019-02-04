package com.example.brandon.transblink;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by alee on 3/12/2017.
 */

public class ListStationAdapater extends ArrayAdapter{
    public static enum DISP { NEARSTATION, ROUTE_OPTIONS, ROUTE_DISPLAY };        // Display Mode for other List

    private Activity context;
    private Station[] stations;
    private Path[] paths;
    private Path path;
    private DISP displayMode;

    TextView txtTitle;
    TextView txtDesc;

    public ListStationAdapater(Activity context, Station[] stations, DISP mode ) {
        super(context, R.layout.list_station, stations);
        this.stations = stations;
        this.context = context;
        this.displayMode = mode;
    }

    public ListStationAdapater(Activity context, Path[] paths, DISP mode ) {
        super(context, R.layout.list_station, paths);
        this.paths = paths;
        this.context = context;
        this.displayMode = mode;
    }

    public ListStationAdapater(Activity context, Path path, DISP mode ) {
        super(context, R.layout.list_station, path.pathStops);
        this.path = path;
        this.context = context;
        this.displayMode = mode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_station, parent, false);
        }

        txtTitle = (TextView)convertView.findViewById(R.id.tvStationTitle);
        txtDesc = (TextView)convertView.findViewById(R.id.tvStationDesc);

        switch (displayMode){
            case NEARSTATION:
                showNearStations(position, convertView);
                break;

            case ROUTE_OPTIONS:
                showRouteOptions(position, convertView);
                break;

            case ROUTE_DISPLAY:
                showRouteChosen(position, convertView);
                break;

        }
        return convertView;
    }

    public void showNearStations(int position, View convertView){
        Button[] lineColors = new Button[2];
        lineColors[0] = (Button)convertView.findViewById(R.id.line1);
        lineColors[0].setBackgroundColor(0);
        lineColors[1] = (Button)convertView.findViewById(R.id.line2);
        lineColors[1].setBackgroundColor(0);

        txtTitle.setText(stations[position].getFullName());
        txtDesc.setText(String.valueOf((int)(stations[position].getDistance()) + "m"));

        Station.Lines[] lines = stations[position].getLines();
        for(int i=0; i<lines.length; i++){
            int color = 0;
            switch (lines[i]){
                case Expo:
                    color = Color.parseColor("#458fef");
                    break;

                case Millennium:
                    color = Color.parseColor("#f4f442");
                    break;

                case Canada:
                    color = Color.parseColor("#41e2f4");
                    break;
            }
            lineColors[i].setBackgroundColor(color);
        }
    }

    public void showRouteOptions(int position, View convertView){
        Path path = paths[position];
        path.setTransferInfo();

        int numOfStations = path.pathStops.size();
        int numOfTransfers = path.getNumTransfers();
        String transferInfo = "";
        txtTitle.setText("Path Option "+ (position+1) +": "+numOfStations+" Stops");

        if(numOfTransfers !=0){
            transferInfo += numOfTransfers+ " Time Transfer";
            for (int i=0; i<path.transferStations.size(); i++){
                transferInfo += "\n";
                transferInfo += (i+1)+ ": "+path.transferStations.get(i).getFullName();
            }
        }else{
            transferInfo += "No Transfer";
        }
        txtDesc.setText(transferInfo);
    }


    public void showRouteChosen(int position, View convertView){
        Station currentStation = path.pathStops.get(position);
        Station nextStation = null;

        if(position < path.pathStops.size()-1){
            nextStation = path.pathStops.get(position+1);
        }
        txtTitle.setText(currentStation.getFullName());

        if(path.transferStations.contains(currentStation) && nextStation !=null){
            txtDesc.setText("["+ nextStation.getLines()[0] + "] to "+nextStation.getFullName());
//            switch(nextStation.getLines()[0]){
//                case Expo:
//                    txtDesc.setTextColor(Color.parseColor("#458fef"));
//                    break;
//
//                case Millennium:
//                    txtDesc.setTextColor(Color.parseColor("#f4dc42"));
//                    break;
//
//                case Canada:
//                    txtDesc.setTextColor(Color.parseColor("#41e2f4"));
//                    break;
//            }

        }else{
            txtDesc.setText("");
        }

        Button[] lineColors = new Button[2];
        lineColors[0] = (Button)convertView.findViewById(R.id.line1);
        lineColors[0].setBackgroundColor(0);
        lineColors[1] = (Button)convertView.findViewById(R.id.line2);
        lineColors[1].setBackgroundColor(0);

        Station.Lines[] lines = path.pathStops.get(position).getLines();
        for(int i=0; i<lines.length; i++){
            int color = 0;
            switch (lines[i]){
                case Expo:
                    color = Color.parseColor("#458fef");
                    break;

                case Millennium:
                    color = Color.parseColor("#f4f442");
                    break;

                case Canada:
                    color = Color.parseColor("#41e2f4");
                    break;
            }
            lineColors[i].setBackgroundColor(color);
        }
    }
}
