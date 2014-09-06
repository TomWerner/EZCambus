package com.wernerapps.ezbongo.bll;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class DistanceCalculator
{
    private static DistanceCalculator instance;
    private static final double DEFAULT_WALKING_DISTANCE = 0.5; //Miles
    private static final double MILES_PER_METER = 0.000621371;
    private double walkingDistance;
    
    private DistanceCalculator()
    {
        walkingDistance = DEFAULT_WALKING_DISTANCE;
    }
    
    public static DistanceCalculator getInstance()
    {
        if (instance == null)
            instance = new DistanceCalculator();
        return instance;
    }

    public double getWalkingDistance()
    {
        return walkingDistance;
    }

    public void setWalkingDistance(double walkingDistance)
    {
        this.walkingDistance = walkingDistance;
    }

    public boolean isWithinWalkingDistance(LatLng userPosition, LatLng stopPosition)
    {
        float[] distance = new float[1];
        Location.distanceBetween(userPosition.latitude, userPosition.longitude,
                stopPosition.latitude, stopPosition.longitude, distance);
        double milesDistance = distance[0] * MILES_PER_METER;
        
        return milesDistance <= walkingDistance;
    }

    public static double rawDistance(LatLng userLocation1, LatLng location)
    {
        float[] distance = new float[1];
        Location.distanceBetween(userLocation1.latitude, userLocation1.longitude,
                location.latitude, location.longitude, distance);
        return distance[0];
    }
    
    
    
}
