package com.wernerapps.ezbongo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class Stop
{
	private LatLng position;
	private String name;
	private boolean cambus, iowaCity, coralville;
	private String stopNumber;
	private ArrayList<String> routes;
	
	public boolean inServiceList(ArrayList<String> selectedServices)
	{
		return (selectedServices.contains("uiowa") && cambus) ||
			   (selectedServices.contains("iowa-city") && iowaCity) ||
			   (selectedServices.contains("coralville") && coralville);
	}
	
	public boolean isCambusStop()
	{
		return cambus;
	}
	
	public boolean isIowaCityStop()
	{
		return iowaCity;
	}
	
	public boolean isCoralvilleStop()
	{
		return coralville;
	}
	
	public Stop(BusInformationManager bim, JSONObject jsonObject)
	{
		try
		{
			JSONObject stopData = jsonObject.getJSONObject("stop");
			
			String number = "" + stopData.getInt("n");
			String zeros = "0000";
			stopNumber = zeros.substring(0, 4 - number.length()) + number;
			
			name = stopData.getString("t");
			
			position = new LatLng(stopData.getDouble("lat"), stopData.getDouble("lng"));
			
			routes = new ArrayList<String>();
			JSONArray routeData = stopData.getJSONArray("r");
			for (int i = 0; i < routeData.length(); i++)
			{
				routes.add(routeData.getString(i));
				
				String tag = routeData.getString(i);
				if (bim.getRoutes().containsKey(tag))
				{
					String agency = bim.getRoutes().get(tag).getAgency();
					if (agency.equals(BusInformationManager.CAMBUS))
						cambus = true;
					else if (agency.equals(BusInformationManager.CORALVILLE))
						coralville = true;
					else if (agency.equals(BusInformationManager.IOWACITY))
						iowaCity = true;
				}
			}
			
			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
	}

	public String getName()
	{
		return name;
	}
	
	public LatLng getPosition()
	{
		return position;
	}
	
	public String toString()
	{
		return stopNumber + ": " + name;
	}

	public String getStopNumber()
	{
		return stopNumber;
	}
}
