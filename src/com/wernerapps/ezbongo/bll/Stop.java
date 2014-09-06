package com.wernerapps.ezbongo.bll;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class Stop {

    private int stopNumber;
    private String stopTitle;
    private LatLng position;
    private ArrayList<Route> routes = new ArrayList<Route>();

    public Stop(int stopNumber, String stopTitle, double lat, double lng)
    {
        this.stopNumber = stopNumber;
        this.stopTitle = stopTitle;
        this.position = new LatLng(lat, lng);
    }

    public int getStopNumber()
    {
        return stopNumber;
    }

    public void setStopNumber(int stopNumber)
    {
        this.stopNumber = stopNumber;
    }

    public String getStopTitle()
    {
        return stopTitle;
    }

    public void setStopTitle(String stopTitle)
    {
        this.stopTitle = stopTitle;
    }

    public LatLng getPosition()
    {
        return position;
    }

    public void setPosition(LatLng position)
    {
        this.position = position;
    }

    public ArrayList<Route> getRoutes()
    {
        return routes;
    }
    
    public void updateInformation(int stopID, String stopTitle, double lat, double lng, ArrayList<Route> routes)
    {
        setStopNumber(stopID);
        setStopTitle(stopTitle);
        setPosition(new LatLng(lat, lng));
        this.routes  = routes;
    }

}
