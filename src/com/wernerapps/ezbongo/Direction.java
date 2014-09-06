package com.wernerapps.ezbongo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Direction
{
	private String direction;
	private String tag;
	private ArrayList<String> stops = new ArrayList<String>();

	public Direction(String direction, String tag, ArrayList<String> stops)
	{
		this.direction = direction;
		this.tag = tag;
		this.stops = stops;
	}

	public Direction(JSONObject jsonObject) throws JSONException
	{
		tag = jsonObject.getString("tag");
		direction = jsonObject.getString("d");
		JSONArray stopJson = jsonObject.getJSONArray("s");
		
		for (int i = 0; i < stopJson.length(); i++)
		{
			String number = "" + stopJson.getInt(i);
			String zeros = "0000";
			String num = zeros.substring(0, 4 - number.length()) + number;
			stops.add(num);
		}
	}
	
	public boolean containsStops(Stop s1, Stop s2)
	{
		boolean hasS1 = false;
		boolean hasS2 = false;
		for (String stop : stops)
		{
			System.out.println("HERE: " + stop + ", " + s1.getStopNumber() + ", " + s2.getStopNumber());
			if (stop.equals(s1.getStopNumber()))
				hasS1 = true;
			if (stop.equals(s2.getStopNumber()))
				hasS2 = true;
			if (hasS1 && hasS2)
				return true;
		}
		return (hasS1 && hasS2);
	}

	public String getDirection()
	{
		return direction;
	}

	public String getTag()
	{
		return tag;
	}

	public ArrayList<String> getStops()
	{
		return stops;
	}
}
