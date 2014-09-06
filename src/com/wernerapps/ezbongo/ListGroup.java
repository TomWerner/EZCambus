package com.wernerapps.ezbongo;

public class ListGroup
{

	private String text;

	public ListGroup(String string)
	{
		this.text = string;
	}
	
	public String toString()
	{
		return text;
	}
	
	public int hashCode()
	{
		return text.hashCode();
	}
}
