package com.wernerapps.ezbongo;

public class RouteListItem implements Comparable<RouteListItem>
{

	private Stop s1;
	private Stop s2;
	private Route route;
	private float distance;
	
	public RouteListItem(Stop s1, Stop s2, Route route, float distance)
	{
		this.route = route;
		this.s1 = s1;
		this.s2 = s2;
		this.distance = distance;
	}
	
	public Route getRoute()
	{
		return route;
	}
	
	public String toString()
	{
		return s1.toString();
	}

	public Stop getS1()
	{
		return s1;
	}
	
	public Stop getS2()
	{
		return s2;
	}

	public float getDistance()
	{
		return distance;
	}

	@Override
	public int compareTo(RouteListItem arg0)
	{
		return Float.valueOf(distance).compareTo(arg0.distance);
	}
}
