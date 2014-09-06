package com.wernerapps.ezbongo.bll;

public class Route {

	private String name;
	private String tag;
	private String agency;

	public Route(String name, String tag, String agency) {
		this.name = name;
		this.tag = tag;
		this.agency = agency;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

}
