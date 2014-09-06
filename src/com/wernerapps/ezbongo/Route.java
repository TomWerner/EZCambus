package com.wernerapps.ezbongo;

import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Route
{
	private String agency;
	private String tag;
	private String name;
	private ArrayList<Direction> directions = new ArrayList<Direction>();
	private HashSet<String> stops;
	

	public Route(JSONObject jsonObject) throws JSONException
	{
		jsonObject = jsonObject.getJSONObject("route");
		
		this.agency = jsonObject.getString("a");
		this.tag = jsonObject.getString("t");
		this.name = jsonObject.getString("n");
		
		JSONArray dirJSON= jsonObject.getJSONArray("d");
		for (int i = 0; i < dirJSON.length(); i++)
			directions.add(new Direction(dirJSON.getJSONObject(i)));
		stops = new HashSet<String>();
		for (Direction dir : directions)
			stops.addAll(dir.getStops());
		
	}

	public boolean containsStops(Stop s1, Stop s2)
	{
		return stops.contains(s1.getStopNumber()) && stops.contains(s2.getStopNumber());
//		for (Direction direction : directions)
//			if (direction.containsStops(s1, s2))
//				return true;
//		return false;
	}
	
	public ArrayList<Direction> getDirections()
	{
		return directions;
	}

	public String getAgency()
	{
		return agency;
	}

	public String getTag()
	{
		return tag;
	}

	public String getName()
	{
		return name;
	}
}
