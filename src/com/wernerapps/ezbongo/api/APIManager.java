package com.wernerapps.ezbongo.api;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.SparseArray;

import com.wernerapps.ezbongo.bll.Route;
import com.wernerapps.ezbongo.bll.Stop;

public class APIManager
{
    private static final String ROUTE_LISTING_URL = "http://api.ebongo.org/routelist?format=json&api_key=%s";
    private static final String ROUTE_INFO_URL = "http://api.ebongo.org/route?agency=%s&route=%s&format=json&api_key=%s";
    private static final String STOP_LISTING_URL = "http://api.ebongo.org/stoplist?format=json&api_key=%s";
    private static final String STOP_INFO_URL = "http://api.ebongo.org/stop?format=json&stopid=1&api_key=%s";

    private static APIManager instance;
    private String apiKey;

    private SparseArray<Stop> stopListing = new SparseArray<Stop>();
    
    private ArrayList<OnStopListingDownloaded> stopListingListeners = new ArrayList<APIManager.OnStopListingDownloaded>();
    private ArrayList<OnStopInfoDownloaded> stopInfoListeners = new ArrayList<APIManager.OnStopInfoDownloaded>();

    public static APIManager getInstance()
    {
        if (instance == null)
            instance = new APIManager();
        return instance;
    }

    public void setKey(String key)
    {
        apiKey = key;
    }

    public ArrayList<Route> getRouteListing()
    {
        ArrayList<Route> result = new ArrayList<Route>();

        // TODO: Use listeners
        try
        {
            JSONObject data = null;//readJsonFromUrl(String.format(ROUTE_LISTING_URL, apiKey));

            if (isDataValid(data))
            {
                JSONArray routes = data.getJSONArray("routes");
                for (int i = 0; i < routes.length(); i++)
                {
                    JSONObject route = routes.getJSONObject(i).getJSONObject("route");
                    result.add(new Route(route.getString("name"), route.getString("tag"), route.getString("agency")));
                }
            }
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    public void requestStopListing()
    {
        if (stopListing.size() == 0)
        {
            String url = String.format(STOP_LISTING_URL, apiKey);
            new NetworkCall(url)
            {
                @Override
                public void parseJSONResults(JSONObject networkCallResult)
                {
                    parseStopListing(networkCallResult);
                    notifyStopListingSubscribers();
                }
            };
        }
        else
        {
            notifyStopListingSubscribers();
        }
    }

    protected void parseStopListing(JSONObject networkResult)
    {
        try
        {
            if (isDataValid(networkResult))
            {
                JSONArray stopsJSON = networkResult.getJSONArray("stops");
                for (int i = 0; i < stopsJSON.length(); i++)
                {
                    JSONObject stop = stopsJSON.getJSONObject(i).getJSONObject("stop");
                    Stop newStop = new Stop(stop.getInt("stopnumber"), stop.getString("stoptitle"), stop.getDouble("stoplat"),
                            stop.getDouble("stoplng"));
                    
                    // Add the stop to our list
                    stopListing.append(stop.getInt("stopnumber"), newStop);
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void requestStopInfo(final int stopID)
    {        
        String url = String.format(STOP_INFO_URL, stopID, apiKey);
        new NetworkCall(url)
        {
            @Override
            public void parseJSONResults(JSONObject networkCallResult)
            {
                parseStopInfo(stopID, networkCallResult);
            }
        };
    }
    
    protected void parseStopInfo(int stopID, JSONObject networkResult)
    {
        try
        {
            if (isDataValid(networkResult))
            {
                Stop stop = stopListing.get(stopID);
                JSONObject stopinfo = networkResult.getJSONObject("stopinfo");
                System.out.println(stopinfo.toString(1));
                JSONArray routes = stopinfo.getJSONArray("routes");
                ArrayList<Route> servicingRoutes = new ArrayList<Route>();
    
                for (int i = 0; i < routes.length(); i++)
                {
                    JSONObject route = routes.getJSONObject(i);
                    servicingRoutes.add(new Route(route.getString("title"), route.getString("tag"), route.getString("agency")));
                }
    
                stop.updateInformation(stopinfo.getInt("stopid"), stopinfo.getString("stoptitle"), stopinfo.getDouble("latitude"),
                        stopinfo.getDouble("longitude"), servicingRoutes);
                notifyStopInfoSubscribers(stop);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
    }

    private void notifyStopInfoSubscribers(Stop stop)
    {
        for (OnStopInfoDownloaded listener : stopInfoListeners)
        {
            listener.stopInfoDownloaded(stop);
        }
    }

    public JSONObject getRouteInfo(String agency, String routeTag)
    {
        String url = String.format(ROUTE_INFO_URL, agency, routeTag, apiKey);

        // TODO: Use listeners
        JSONObject data = null;// readJsonFromUrl();
        return data;
    }

    private boolean isDataValid(JSONObject data)
    {
        if (data == null || data.has("ERROR"))
        {
            // TODO: Handle error
            return false;
        }
        return true;
    }

    
    private void notifyStopListingSubscribers()
    {
        for (OnStopListingDownloaded listener : stopListingListeners)
        {
            listener.stopListingDownloaded(stopListing);
        }
    }

    public interface OnStopListingDownloaded
    {
        public void stopListingDownloaded(SparseArray<Stop> stopListing);
    }

    public void addStopListingListener(OnStopListingDownloaded onStopListingDownloaded)
    {
        stopListingListeners.add(onStopListingDownloaded);
    }
    
    public interface OnStopInfoDownloaded
    {
        public void stopInfoDownloaded(Stop stop);
    }

    public void addStopInfoListener(OnStopInfoDownloaded onStopInfoDownloaded)
    {
        stopInfoListeners.add(onStopInfoDownloaded);
    }
}
