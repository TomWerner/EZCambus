package com.wernerapps.ezbongo.bll;

import java.util.HashMap;

import android.content.Context;
import android.location.Address;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapManager
{
    private static GoogleMapManager instance;
    private static final float DEFAULT_CAMERA_ZOOM = 16;
    
    private static final LatLng OLD_CAPITAL = new LatLng(41.661378f, -91.536097f);
    private HashMap<Marker, Stop> markerStopMap = new HashMap<Marker, Stop>();
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;
    private GoogleMap googleMap;
    
    private Marker userLocation1;
    private boolean updateOnLocationChange;
    
    public static GoogleMapManager getInstance()
    {
        if (instance == null)
            instance = new GoogleMapManager();
        return instance;
    }
    
    public void resetBounds()
    {
        minLatitude = Double.MAX_VALUE;
        maxLatitude = Double.MIN_VALUE;
        minLongitude = Double.MAX_VALUE;
        maxLongitude = Double.MIN_VALUE;
    }
    
    public void clearStopMarkers()
    {
        for (Marker marker : markerStopMap.keySet())
            marker.remove();
    }
    
    public void setMap(Context context, GoogleMap map, SupportMapFragment parent)
    {
        resetBounds();
        this.googleMap = map;
        
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener()
        {
            @Override
            public void onMyLocationChange(Location arg0)
            {
                if (updateOnLocationChange)
                {
                    placeMyLocationMarker(arg0);
                }
            }
        });
        googleMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener()
        {
            @Override
            public boolean onMyLocationButtonClick()
            {
                updateOnLocationChange = true;
                // Force the redraw
                userLocation1 = null;
                return true;
            }
        });
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(OLD_CAPITAL, DEFAULT_CAMERA_ZOOM));
    }
    
    private void placeMyLocationMarker(Location location)
    {
        LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
        if (userLocation1 == null || (userLocation1 != null && DistanceCalculator.rawDistance(userLocation1.getPosition(), myPosition) > 10))
        {
            if (userLocation1 != null)
            {
                userLocation1.remove();
            }
            setUserLocation(myPosition);
            userLocation1.setTitle("My Position");
        }
        
    }

    public void setUserLocation(Address address)
    {
        userLocation1 = null;
        LatLng addressPosition = new LatLng(address.getLatitude(), address.getLongitude());
        setUserLocation(addressPosition);
        updateOnLocationChange = false;
    }
    
    private void setUserLocation(LatLng position)
    {
        MarkerOptions mark = new MarkerOptions();
        mark.position(position);
        userLocation1 = googleMap.addMarker(mark);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, DEFAULT_CAMERA_ZOOM));
    }

    public void addStop(Stop stop)
    {
        MarkerOptions mark = new MarkerOptions();
        mark.title(stop.getStopTitle());
        mark.snippet("Stop number: " + stop.getStopNumber());
        
        mark.position(stop.getPosition());
        Marker marker = googleMap.addMarker(mark);
        markerStopMap.put(marker, stop);
        
        minLatitude = Math.min(minLatitude, stop.getPosition().latitude);
        maxLatitude = Math.max(maxLatitude, stop.getPosition().latitude);
        minLongitude = Math.min(minLongitude, stop.getPosition().longitude);
        maxLongitude = Math.max(maxLongitude, stop.getPosition().longitude);
    }

    public GoogleMap getMap()
    {
        return googleMap;
    }
    
    
}
