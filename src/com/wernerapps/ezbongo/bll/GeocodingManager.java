package com.wernerapps.ezbongo.bll;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

public class GeocodingManager
{
    private static GeocodingManager instance;
    private Geocoder geocoder;
    private static final int DEFAULT_MAX_RESULTS = 10;
    private int maxResults = DEFAULT_MAX_RESULTS;
    private String stateName;
    private String stateAbbrev;

    private GeocodingManager()
    {

    }

    public static GeocodingManager getInstance()
    {
        if (instance == null)
            instance = new GeocodingManager();
        return instance;
    }

    public List<Address> searchForAddress(Context context, String address)
    {
        if (geocoder == null)
            geocoder = new Geocoder(context);

        // Check to see if we have this address in our campus places list
        if (AddressSearchHelper.getCampusPlaces().containsKey(address))
        {
            address = AddressSearchHelper.getCampusPlaces().get(address);
        }

        List<Address> results = null;
        try
        {
            results = geocoder.getFromLocationName(address, maxResults);
            for (int i = 0; i < results.size();)
            {
                Address a = results.get(i);
                if (a.getAdminArea() == null || (!a.getAdminArea().equalsIgnoreCase(stateName) && !a.getAdminArea().equalsIgnoreCase(stateAbbrev)))
                {
                    results.remove(i);
                }
                else
                {
                    i++;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return results;
    }

    public int getMaxResult()
    {
        return maxResults;
    }

    public void setMaxResult(int maxResult)
    {
        this.maxResults = maxResult;
    }

    public void setState(String stateName, String stateAbbrev)
    {
        this.stateName = stateName;
        this.stateAbbrev = stateAbbrev;
    }
}
