package com.example.brandon.transblink;
import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Matt on 12/02/2017.
 */

public class Path implements Serializable
{
    private Station startStn;
    private Station previousStn;
    private Station endStn;
    private int numStops;
    private int numTransfers;
    public ArrayList<Station> pathStops; //should this be public?
    public ArrayList<String> traversed;
    public ArrayList<Station> transferStations;

    public Path(Station a, Station b)
    {
        this.startStn = a;
        this.endStn = b;
        this.previousStn = null;
        this.numStops = 0;
        this.numTransfers = 0;
        this.pathStops = new ArrayList<Station>();
        this.traversed = new ArrayList<String>();
    }

    //copy constructor
    public Path(Path old)
    {
        this.startStn = old.startStn;
        this.endStn = old.endStn;
        this.previousStn = null;
        this.numStops = 0;
        this.numTransfers = 0;
        this.pathStops = new ArrayList<>(old.pathStops);
        this.traversed = new ArrayList<>(old.traversed);
    }

    public void setTransferInfo()
    {
        this.transferStations = new ArrayList<Station>();
        ArrayList<Station> stationQueue = new ArrayList<Station>();
        this.numTransfers = 0;

        Station prevStation = null;
        Station.Lines currentLine = null;
        Station.Lines[] currentStationLines;

        if(pathStops == null || pathStops.size() ==0)
            return;

        for(int i=0; i<pathStops.size(); i++)
        {

            if(stationQueue.size() <3 ){
                stationQueue.add(pathStops.get(i));
            }else{
                stationQueue.remove(0);
                stationQueue.add(pathStops.get(i));
            }

            Station currentStation = pathStops.get(i);
            currentStationLines = currentStation.getLines();

            if(i==0)
            {
                prevStation = currentStation;
                if(currentStationLines.length == 1)
                    currentLine = currentStationLines[0];

            }
            else
            {
                if (currentLine == null && currentStationLines.length == 1)
                {
                    currentLine = currentStationLines[0];
                }else if (currentLine != null && currentStationLines.length == 1 && currentLine != currentStationLines[0]) {
                    transferStations.add(prevStation);
                    numTransfers++;
                    currentLine = currentStationLines[0];
                }else if(stationQueue.size() >=3 && (
                    // Sapperton : Columbia : Scott Road case
                            stationQueue.get(0).getCode().equalsIgnoreCase("SAP") && stationQueue.get(1).getCode().equalsIgnoreCase("COL") && stationQueue.get(2).getCode().equalsIgnoreCase("SCR")
                            || stationQueue.get(0).getCode().equalsIgnoreCase("SCR") && stationQueue.get(1).getCode().equalsIgnoreCase("COL") && stationQueue.get(2).getCode().equalsIgnoreCase("SAP")

                    // Aberdeen : Bridgeport : Templeton case
                            || stationQueue.get(0).getCode().equalsIgnoreCase("TPL") && stationQueue.get(1).getCode().equalsIgnoreCase("BRP") && stationQueue.get(2).getCode().equalsIgnoreCase("ABD")
                            || stationQueue.get(0).getCode().equalsIgnoreCase("ABD") && stationQueue.get(1).getCode().equalsIgnoreCase("BRP") && stationQueue.get(2).getCode().equalsIgnoreCase("TPL"))
                    ){
                        transferStations.add(prevStation);
                        numTransfers++;
                }

            }
            prevStation = currentStation;
        }
    }

    public String getStartCode() { return this.startStn.getCode(); }

    public String getEndCode() { return this.endStn.getCode(); }

    public int getNumStops()
    {
        return this.numStops;
    }

    public void setNumStops()
    {
        this.numStops = this.pathStops.size() - 1;
    }

    public int getNumTransfers()
    {
        return this.numTransfers;
    }

    public void setNumTransfers(int a)
    {
        this.numTransfers = a;
    }

    public Station getPreviousStn()
    {
        return this.previousStn;
    }

    public void setPreviousStn(Station stn)
    {
        this.previousStn = stn;
    }

    public boolean traverse(Station stn)
    {
        boolean end = false;
        Station current = stn;

        //System.out.println("NOW AT " + current.getFullName() + " ON THE WAY TO " + this.endStn.getFullName());
        this.traversed.add(current.getCode());

        if (current.equals(this.endStn)) //stop if end
        {
            end = true;
            this.pathStops.add(current); //adds ending station
            //System.out.println("DESTINATION FOUND AT " + current.getFullName());
            //return end;
        }
        else
        {
            for (int i = 0; i < current.connectingStations.size(); i++)
            {
                //System.out.println("IN FOR LOOP, ITERATION NO." + i);

                String code = current.connectingStations.get(i).split("-")[0];

                if (!end && valid(code)) //if not yet at the destination
                {
                    this.setPreviousStn(current);
                    //System.out.println("TRAVERSE TO " + code);
                    end = traverse(DataProcessor.findStation(MainActivity.masterList, code)); //keep going
                }

                if (end)
                {
                    //System.out.println("ADDING " + current.getFullName());
                    //if (!pathStops.contains(current))
                    this.pathStops.add(current);
                    break; //get out of the loop
                }
            }
        }

        //System.out.println("END OF TRAVERSE");
        return end;
    }

    public boolean valid(String nextCode)
    {
        boolean v = true;
        Station testStn = DataProcessor.findStation(MainActivity.masterList, nextCode);

        if (this.pathStops.contains(testStn) || testStn.equals(this.getPreviousStn()) || this.traversed.contains(nextCode))
            v = false;

        return v;
    }

    public void insertPaths(Path add)
    {
        /*
        System.out.print("ADDING PATHS ");
        DataProcessor.printPath(add);
        System.out.print("-TO- ");
        DataProcessor.printPath(this);
        System.out.println("");
        */

        for (int i = add.pathStops.size() - 1; i >= 0; i--)
        {
            if (!this.pathStops.contains(add.pathStops.get(i)))
            {
                this.pathStops.add(0, add.pathStops.get(i));
                this.numStops = this.pathStops.size();
            }
        }

        /*
        System.out.print("AFTER ADDING: ");
        DataProcessor.printPath(this);
        System.out.println("");
        */
    }
}