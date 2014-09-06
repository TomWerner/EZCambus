package com.wernerapps.ezbongo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;


public class BusInformationManager
{
	public static final String CAMBUS = "uiowa";
	public static final String CORALVILLE = "coralville";
	public static final String IOWACITY = "iowa-city";

	private Stop[] stopList;
	
	public static HashMap<String, String> CAMPUS_PLACES = new HashMap<String, String>();
	{{
		CAMPUS_PLACES.put("Adler Journalism and Mass Communication Building", "104 West Washington St, Iowa City");
		CAMPUS_PLACES.put("Afro-American Cultural Center", "303 Melrose Ave, Iowa City");
		CAMPUS_PLACES.put("Art Building", "120 North Riverside Dr, Iowa City");
		CAMPUS_PLACES.put("Art Building West", "141 North Riverside Dr, Iowa City");
		CAMPUS_PLACES.put("Asian Pacific-American Cultural Center", "223 Lucon Dr, Iowa City");
		CAMPUS_PLACES.put("Banks Field", "960 Evashevski Dr, Iowa City");
		CAMPUS_PLACES.put("Becker Communication Studies Building", "25 South Madison St, Iowa City");
		CAMPUS_PLACES.put("Beckwith Boathouse", "1201 North Dubuque St, Iowa City");
		CAMPUS_PLACES.put("Bedell Entrepreneurship Learning Lab", "322 North Clinton, Iowa City");
		CAMPUS_PLACES.put("BioVentures Center", "2500 Crosspark Rd, Coralville");
		CAMPUS_PLACES.put("Biology Building", "129 East Jefferson St, Iowa City");
		CAMPUS_PLACES.put("Biology Building East", "210 East Iowa Ave, Iowa City");
		CAMPUS_PLACES.put("Blank Honors Center", "221 North Clinton, Iowa City");
		CAMPUS_PLACES.put("Bowen Science Building", "51 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("Bowman House", "230 North Clinton St, Iowa City");
		CAMPUS_PLACES.put("Boyd Law Building", "130 Byington Rd, Iowa City");
		CAMPUS_PLACES.put("Boyd Tower", "601 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("Burge Hall", "301 North Clinton St, Iowa City");
		CAMPUS_PLACES.put("Calvin Hall", "2 West Jefferson St, Iowa City");
		CAMPUS_PLACES.put("Cambus Maintenance Facility", "517 South Madison St, Iowa City");
		CAMPUS_PLACES.put("Cambus Offices", "925 Stadium Dr, Iowa City");
		CAMPUS_PLACES.put("Campus Recreation and Wellness Center", "309 South Madison St, Iowa City");
		CAMPUS_PLACES.put("Carver Biomedical Research Building", "285 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("Carver River Research and Education Facility", "3388 Hwy 22, Muscatine");
		CAMPUS_PLACES.put("Carver-Hawkeye Arena", "1 Elliott Dr, Iowa City");
		CAMPUS_PLACES.put("Center for Disabilities and Development", "100 Hawkins Dr, Iowa City");
		CAMPUS_PLACES.put("Chemistry Building", "251 North Capitol St, Iowa City");
		CAMPUS_PLACES.put("Chilled Water Plant 1", "255 Hawkins Dr, Iowa City");
		CAMPUS_PLACES.put("Chilled Water Plant 2 (West)", "305 Hawkins Dr, Iowa City");
		CAMPUS_PLACES.put("Clinton Street Building", "700 South Clinton, Iowa City");
		CAMPUS_PLACES.put("Clinton Street Music 375", "375 South Clinton, Iowa City");
		CAMPUS_PLACES.put("Clinton Street Music 376", "376 South Clinton, Iowa City");
		CAMPUS_PLACES.put("College of Medicine Administration Building", "451 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("College of Nursing Building", "50 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("College of Public Health Building", "105 River St., Iowa City");
		CAMPUS_PLACES.put("Communications Center", "116 South Madison St, Iowa City");
		CAMPUS_PLACES.put("Continuing Education Facility", "30 South Dubuque St, Iowa City");
		CAMPUS_PLACES.put("Coral Center", "625 1st Ave, Coralville");
		CAMPUS_PLACES.put("Corridor Technology Center", "map");
		CAMPUS_PLACES.put("Currier Hall", "413 North Clinton St, Iowa City");
		CAMPUS_PLACES.put("Danforth Chapel", "15 West Jefferson St, Iowa City");
		CAMPUS_PLACES.put("Daum Hall", "100 Daum, Iowa City");
		CAMPUS_PLACES.put("Dental Science Building", "801 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("Dey House", "507 North Clinton St, Iowa City");
		CAMPUS_PLACES.put("Eckstein Medical Research Building", "431 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("Emergency Power Facility 1", "24 South Grand Ave, Iowa City");
		CAMPUS_PLACES.put("Engineering Research Facility", "324 South Madison St, Iowa City");
		CAMPUS_PLACES.put("English-Philosophy Building", "251 West Iowa Ave, Iowa City");
		CAMPUS_PLACES.put("Environmental Health and Safety Office", "122 Grand Ave Court, Iowa City");
		CAMPUS_PLACES.put("Environmental Health and Safety Office 2", "311 Grand Ave, Iowa City");
		CAMPUS_PLACES.put("Environmental Health and Safety Office 3", "120 Grand Ave Court, Iowa City");
		CAMPUS_PLACES.put("Environmental Health and Safety Office 4", "126 Grand Ave Court, Iowa City");
		CAMPUS_PLACES.put("Environmental Management Facility", "2260 Old Farmstead Rd, Coralville");
		CAMPUS_PLACES.put("Family Medical Center – Hawkeye Campus", "4051 Hawkeye Park Rd, Iowa City");
		CAMPUS_PLACES.put("Field Hockey Event Management Box", "2555 Praire Meadow Dr, Iowa City");
		CAMPUS_PLACES.put("Field Hockey Grandstand", "2601 Praire Meadow Dr, Iowa City");
		CAMPUS_PLACES.put("Field House", "225 South Grand Ave, Iowa City");
		CAMPUS_PLACES.put("Finkbine Golf Club House", "1380 Melrose Ave, Iowa City");
		CAMPUS_PLACES.put("Finkbine Shop Facility", "874 North Mormon Trek, Iowa City");
		CAMPUS_PLACES.put("Fleet Services", "155 West Harrison, Iowa City");
		CAMPUS_PLACES.put("General Hospital", "220 Hawkins Dr, Iowa City");
		CAMPUS_PLACES.put("Gerdin Athletic Learning Center", "402 Melrose Ave, Iowa City");
		CAMPUS_PLACES.put("Gilbert Street Building", "1225 South Gilbert St, Iowa City");
		CAMPUS_PLACES.put("Gilmore Hall", "112 North Capitol St, Iowa City");
		CAMPUS_PLACES.put("Glenn Schaeffer Library", "507 North Clinton, Iowa City");
		CAMPUS_PLACES.put("Graduate Painting Studios and Faculty Office", "109 River St, Iowa City");
		CAMPUS_PLACES.put("Halsey Hall", "28 West Jefferson St, Iowa City");
		CAMPUS_PLACES.put("Hancher Auditorium", "101 East Park Rd, Iowa City");
		CAMPUS_PLACES.put("Hancher Box Office", "200 South Capitol St #193, Iowa City");
		CAMPUS_PLACES.put("Hardin Library for Health Sciences", "600 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("Hawkeye Court Apartments", "101 Hawkeye Court, Iowa City");
		CAMPUS_PLACES.put("Hawkeye Drive Apartments", "110 Hawkeye Dr, Iowa City");
		CAMPUS_PLACES.put("Hawkeye Recreation Service Building", "2960 Hawkeye Park Rd, Iowa City");
		CAMPUS_PLACES.put("Hawkeye Tennis and Recreation Complex", "2820 Prairie Meadows Dr, Iowa City");
		CAMPUS_PLACES.put("Hawks Ridge", "100 Hawk Ridge Dr, Iowa City");
		CAMPUS_PLACES.put("Helicopter Hanger", "2262 Old Farmstead Rd, Iowa City");
		CAMPUS_PLACES.put("Hillcrest", "25 Byington Rd, Iowa City");
		CAMPUS_PLACES.put("Hope Lodge", "750 Hawkins Dr, Iowa City");
		CAMPUS_PLACES.put("Hospital Ramp 1", "230 Hawkins Dr, Iowa City");
		CAMPUS_PLACES.put("Hospital Ramp 2", "120 Hawkins Dr, Iowa City");
		CAMPUS_PLACES.put("Hospital Ramp 3", "800 Stadium Dr, Iowa City");
		CAMPUS_PLACES.put("Housing Services Building", "2804 Prairie Meadow Dr, Iowa City");
		CAMPUS_PLACES.put("Hydraulics Annex 1", "2310 Old Farmstead Rd, Coralville");
		CAMPUS_PLACES.put("Hydraulics Annex 2", "2275 Old Farmstead Rd, Coralville");
		CAMPUS_PLACES.put("Hydraulics East Annex", "140 West Harrison St, Iowa City");
		CAMPUS_PLACES.put("Hydraulics Model Annex", "129 West Court St, Iowa City");
		CAMPUS_PLACES.put("Hydraulics Research Lab", "2310 Old Farmstead Rd, Coralville");
		CAMPUS_PLACES.put("Hydraulics Wind Tunnel Annex", "130 West Harrison St, Iowa City");
		CAMPUS_PLACES.put("IIHR—Hydroscience & Engineering", "100 C. Maxwell Stanley Hydraulics Laboratory, Iowa City");
		CAMPUS_PLACES.put("IMU Parking Ramp", "120 North Madison St, Iowa City");
		CAMPUS_PLACES.put("Indoor Practice Facility (Bubble)", "910 Evashevski Dr, Iowa City");
		CAMPUS_PLACES.put("Institute for Rural and Environmental Health", "2420 Old Farmstead Rd, Coralville");
		CAMPUS_PLACES.put("Institute of Public Affairs", "124 Grand Ave Ct, Iowa City");
		CAMPUS_PLACES.put("Iowa Advanced Technology Labs", "205 North Madison St, Iowa City");
		CAMPUS_PLACES.put("Iowa Eye Bank", "2346 Mormon Trek Blvd., Iowa City");
		CAMPUS_PLACES.put("Iowa Geological Survey-Oakdale", "2390 Old Farmstead Rd, Coralville");
		CAMPUS_PLACES.put("Iowa House", "15 West Jefferson St, Iowa City");
		CAMPUS_PLACES.put("Iowa Kidsight", "2346 Mormon Trek Blvd., Iowa City");
		CAMPUS_PLACES.put("Iowa Memorial Union", "125 North Madison St, Iowa City");
		CAMPUS_PLACES.put("Iowa Nonprofit Resource Center", "130 Grand Ave Ct, Iowa City");
		CAMPUS_PLACES.put("J Pappajohn Pavilion", "see SRF");
		CAMPUS_PLACES.put("J W Colloton Pavilion", "see SRF");
		CAMPUS_PLACES.put("Jacobson Athletic Building", "map");
		CAMPUS_PLACES.put("Jefferson Building", "129 East Washington St, Iowa City");
		CAMPUS_PLACES.put("Jessup Hall", "5 West Jefferson St, Iowa City");
		CAMPUS_PLACES.put("Karro Athletic Hall of Fame", "2425 Prairie Meadow Dr, Iowa City");
		CAMPUS_PLACES.put("Kinnick Stadium", "1 Evashevski Dr, Iowa City");
		CAMPUS_PLACES.put("Kuhl House", "119 West Park Rd, Iowa City");
		CAMPUS_PLACES.put("Lagoon Shelter House", "230 North Riverside Dr, Iowa City");
		CAMPUS_PLACES.put("Lakeside Laboratory", "1838 Highway 86 Milford, IA 51351-7267");
		CAMPUS_PLACES.put("Landscape Services Complex", "415 South Madison, Iowa City");
		CAMPUS_PLACES.put("Latino/Native American Cultural Center", "308 Melrose, Iowa City");
		CAMPUS_PLACES.put("Laundry", "2000 Crosspark Rd, Coralville");
		CAMPUS_PLACES.put("Law Admissions Building", "320 Melrose Ave, Iowa City");
		CAMPUS_PLACES.put("Law, Health Policy & Disability Center", "315 Melrose Ave, Iowa City");
		CAMPUS_PLACES.put("Lesbian, Gay, Bisexual, Transgender Resource Center", "125 Grand Ave Court, Iowa City");
		CAMPUS_PLACES.put("Levitt Center for University Advancement", "180 North Riverside Dr, Iowa City");
		CAMPUS_PLACES.put("Liberty Square", "119 2nd St, Coralville");
		CAMPUS_PLACES.put("Library, Main", "125 West Washington St, Iowa City");
		CAMPUS_PLACES.put("Lindquist Center", "240 South Madison St, Iowa City");
		CAMPUS_PLACES.put("Linn Street Music", "209 North Linn St, Iowa City");
		CAMPUS_PLACES.put("Linn Street Place", "332 South Linn St, Iowa City");
		CAMPUS_PLACES.put("MacLean Hall", "2 West Washington St, Iowa City");
		CAMPUS_PLACES.put("Macbride Hall", "17 North Clinton St, Iowa City");
		CAMPUS_PLACES.put("Madison Street Services Building", "640 South Madison, Iowa City");
		CAMPUS_PLACES.put("Market-Dubuque Building", "122 East Market St, Iowa City");
		CAMPUS_PLACES.put("Mayflower Hall", "1110 North Dubuque St, Iowa City");
		CAMPUS_PLACES.put("Medical Education Building", "500 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("Medical Education and Research Facility", "375 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("Medical Laboratories", "25 South Grand Ave, Iowa City");
		CAMPUS_PLACES.put("Medical Research Center", "501 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("Medical Research Facility", "55 South Grand Ave, Iowa City");
		CAMPUS_PLACES.put("Melrose Avenue Parking Facility", "610 Melrose Ave, Iowa City");
		CAMPUS_PLACES.put("Mossman Business Services Building", "2222 Old Hwy 218 South, Iowa City");
		CAMPUS_PLACES.put("Multi-Tenant Facility", "2501 Crosspark Rd, Coralville");
		CAMPUS_PLACES.put("Music West - Interim Building", "150 North Riverside Dr, Iowa City");
		CAMPUS_PLACES.put("National Advanced Driving Simulator", "2401 Oakdale Blvd., Coralville");
		CAMPUS_PLACES.put("Newton Road Ramp", "360 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("North Campus Parking and Chilled Water Facility", "339 North Madison St, Iowa City");
		CAMPUS_PLACES.put("North Hall", "20 West Davenport St, Iowa City");
		CAMPUS_PLACES.put("Oakdale Research Building", "2290 Old Farmstead Rd, Coralville");
		CAMPUS_PLACES.put("Oakdale Systems", "map");
		CAMPUS_PLACES.put("Old Capitol", "1 North Clinton St, Iowa City");
		CAMPUS_PLACES.put("Orthopaedics and Sports Medicine Clinic", "2701 Prairie Meadow Dr, Iowa City");
		CAMPUS_PLACES.put("Pappajohn Biomedical Discovery Building", "169 Newton Road, Iowa City");
		CAMPUS_PLACES.put("Pappajohn Business Building", "21 East Market St, Iowa City");
		CAMPUS_PLACES.put("Pappajohn Education Center", "1200 Grand Ave, Des Moines");
		CAMPUS_PLACES.put("Parklawn", "447-449 N Riverside Dr, Iowa City");
		CAMPUS_PLACES.put("Pearl Softball Field", "2001 Highway 6 West, Iowa City");
		CAMPUS_PLACES.put("Pharmacy Building", "115 South Grand Ave, Iowa City");
		CAMPUS_PLACES.put("Phillips Hall", "16 North Clinton St, Iowa City");
		CAMPUS_PLACES.put("Physiology Research Laboratory", "2340 Old Farmstead Rd, Coralville");
		CAMPUS_PLACES.put("Plaza Centre One", "125 South Dubuque St, Iowa City");
		CAMPUS_PLACES.put("Pomerantz Center", "213 North Clinton St, Iowa City");
		CAMPUS_PLACES.put("Pomerantz Family Pavilion", "see SRF");
		CAMPUS_PLACES.put("Power Plant", "207 West Burlington St, Iowa City");
		CAMPUS_PLACES.put("President's Residence", "102 Church St, Iowa City");
		CAMPUS_PLACES.put("Public Library Training Facility", "130 South Dubuque St, Iowa City");
		CAMPUS_PLACES.put("Public Safety", "131 South Capitol St, Iowa City");
		CAMPUS_PLACES.put("Quadrangle", "310 Grand Ave, Iowa City");
		CAMPUS_PLACES.put("R J Carver Pavilion", "see SRF");
		CAMPUS_PLACES.put("Recreation Building", "930 Evashevski Dr, Iowa City");
		CAMPUS_PLACES.put("Rienow Hall", "320 Grand Ave, Iowa City");
		CAMPUS_PLACES.put("Ronald McDonald House", "730 Hawkins Dr, Iowa City");
		CAMPUS_PLACES.put("Saint Thomas More", "405 North Riverside Dr, Iowa City");
		CAMPUS_PLACES.put("Sand Road Services Building", "4868 Sand Rd, Iowa City");
		CAMPUS_PLACES.put("Sanitary Engineering Lab", "1020 South Clinton St, Iowa City");
		CAMPUS_PLACES.put("Schaeffer Hall", "20 East Washington St, Iowa City");
		CAMPUS_PLACES.put("School of Music Replacement Facility", "93 East Burlington St, Iowa City");
		CAMPUS_PLACES.put("Sciences Library", "120 East Iowa Ave, Iowa City");
		CAMPUS_PLACES.put("Seamans Center", "103 South Capitol St, Iowa City");
		CAMPUS_PLACES.put("Seashore Hall", "301 East Jefferson St, Iowa City");
		CAMPUS_PLACES.put("Shambaugh House", "430 North Clinton, Iowa City");
		CAMPUS_PLACES.put("Shipping and Recieving Facility", "65 South Grand Ave, Iowa City");
		CAMPUS_PLACES.put("Slater Hall", "325 Grand Ave, Iowa City");
		CAMPUS_PLACES.put("South Capitol Services Building", "866 South Capitol St, Iowa City");
		CAMPUS_PLACES.put("South Quadrangle", "310 South Grand Ave, Iowa City");
		CAMPUS_PLACES.put("South Wing", "see SRF");
		CAMPUS_PLACES.put("Speech and Hearing Center", "250 Hawkins Dr, Iowa City");
		CAMPUS_PLACES.put("Spence Laboratories of Psychology", "308 East Iowa Ave, Iowa City");
		CAMPUS_PLACES.put("Stanley Hall", "10 East Davenport St, Iowa City");
		CAMPUS_PLACES.put("Stanley Hydraulics Laboratory", "320 South Riverside Dr, Iowa City");
		CAMPUS_PLACES.put("State Historical Society Building", "402 Iowa Ave East, Iowa City");
		CAMPUS_PLACES.put("State Hygienic Laboratory - Ankeny", "2220 South Ankeny Blvd, Ankeny");
		CAMPUS_PLACES.put("State Hygienic Laboratory - Iowa City", "2490 Crosspark Rd, Coralville");
		CAMPUS_PLACES.put("Studio Arts", "1375 Hwy 1 West, Iowa City");
		CAMPUS_PLACES.put("Stuit Hall", "335 East Jefferson St, Iowa City");
		CAMPUS_PLACES.put("Technology Innovation Center", "2261 Crosspark Rd, Coralville");
		CAMPUS_PLACES.put("Temporary Arts Facilities", "202 North Riverside Dr");
		CAMPUS_PLACES.put("Theatre Building", "200 North Riverside Dr, Iowa City");
		CAMPUS_PLACES.put("Trowbridge Hall", "123 North Capitol St, Iowa City");
		CAMPUS_PLACES.put("UI QuickCare – Southwest Iowa City", "767 Mormon Trek Blvd, Iowa City");
		CAMPUS_PLACES.put("UI Research Park Animal Quarters A", "Coralville");
		CAMPUS_PLACES.put("UI Research Park Power Plant Substation", "Coralville");
		CAMPUS_PLACES.put("UI Research Park Research Building", "2290 Old Farmstead Rd, Coralville");
		CAMPUS_PLACES.put("UI Research Park Research Facilities", "2330 Old Farmstead Rd, Coralville");
		CAMPUS_PLACES.put("UI Research Park Studio A", "2450 Old Hospital Rd, Coralville");
		CAMPUS_PLACES.put("UI Research Park Waste Storage Facility", "2260 Old Farmstead Rd, Coralville");
		CAMPUS_PLACES.put("University Athletic Club", "1360 Melrose Ave, University Hts");
		CAMPUS_PLACES.put("University Book Store", "201 South Clinton St, Iowa City");
		CAMPUS_PLACES.put("University Capitol Centre", "200 South Capitol St, Iowa City");
		CAMPUS_PLACES.put("University Services Building", "1 West Prentiss, Iowa City");
		CAMPUS_PLACES.put("Van Allen Hall", "30 North Dubuque St, Iowa City");
		CAMPUS_PLACES.put("Veteran's Affairs Medical Center", "601 Hwy 6 West");
		CAMPUS_PLACES.put("Voxman Music Building", "300 North Riverside Dr, Iowa City");
		CAMPUS_PLACES.put("Waste Incineration Plant", "2310 Crosspark Road, Coralville");
		CAMPUS_PLACES.put("Water Plant", "208 West Burlington St, Iowa City");
		CAMPUS_PLACES.put("Westlawn", "200 Newton Rd, Iowa City");
		CAMPUS_PLACES.put("Women's Resource and Action Center", "130 North Madison, Iowa City");
		
	}};
	
	private HashMap<String, Route> routes = new HashMap<String, Route>();
	
	public BusInformationManager(Context context)
	{
		BufferedReader routeReader;
		BufferedReader stopReader;
		try
		{
			routeReader = new BufferedReader(
			        new InputStreamReader(context.getAssets().open("route_data.json"), "UTF-8"));

			String jsonText = readAll(routeReader);
			JSONObject json = new JSONObject(jsonText);
			
			JSONArray routeData = json.getJSONArray("routes");
			for (int i = 0; i < routeData.length(); i++)
			{
				Route entry = new Route(routeData.getJSONObject(i));
				routes.put(entry.getTag(), entry);
			}
			
			stopReader = new BufferedReader(
			        new InputStreamReader(context.getAssets().open("stop_data.json"), "UTF-8"));

			jsonText = readAll(stopReader);
			json = new JSONObject(jsonText);
			
			JSONArray stopData = json.getJSONArray("stops");
			stopList = new Stop[stopData.length()];
			
			for (int i = 0; i < stopData.length(); i++)
			{
				stopList[i] = new Stop(this, stopData.getJSONObject(i));
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		
	}

	public Stop[] getStops()
	{
		return stopList;
	}
	
	public Stop getStop(String stopNumber)
	{
		for (Stop stop : stopList)
		{
			if (stop == null || stopNumber == null)
				System.out.println(stop + ", " + stopNumber);
			if (stop.getStopNumber().equals(stopNumber))
				return stop;
		}
		return null;
	}

	public HashMap<String, Route> getRoutes()
	{
		return routes;
	}
	
	private static String readAll(Reader rd) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1)
		{
			sb.append((char) cp);
		}
		return sb.toString();
	}
}
