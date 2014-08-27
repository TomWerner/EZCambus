package edu.uiowa.ezcambus;

public class ListItem
{

	private Stop stop;
	private boolean saved;

	public ListItem(Stop stop, boolean saved)
	{
		this.stop = stop;
		this.saved = saved;
	}
	
	public String toString()
	{
		return stop.toString();
	}

	public Stop getStop()
	{
		return stop;
	}

	public boolean isSaved()
	{
		return saved;
	}
}
