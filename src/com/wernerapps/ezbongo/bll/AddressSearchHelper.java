package com.wernerapps.ezbongo.bll;

import java.util.HashMap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ArrayAdapter;

import com.wernerapps.ezbongo.R;

public class AddressSearchHelper
{
    private static HashMap<String, String> campusPlaces = new HashMap<String, String>();
    
    public static HashMap<String, String> getCampusPlaces()
    {
        if (campusPlaces.size() == 0)
            createCampusPlaces();
        return campusPlaces;
    }
    
    private static void createCampusPlaces()
    {
        campusPlaces.put("Adler Journalism and Mass Communication Building", "104 West Washington St, Iowa City");
        campusPlaces.put("Afro-American Cultural Center", "303 Melrose Ave, Iowa City");
        campusPlaces.put("Art Building", "120 North Riverside Dr, Iowa City");
        campusPlaces.put("Art Building West", "141 North Riverside Dr, Iowa City");
        campusPlaces.put("Asian Pacific-American Cultural Center", "223 Lucon Dr, Iowa City");
        campusPlaces.put("Banks Field", "960 Evashevski Dr, Iowa City");
        campusPlaces.put("Becker Communication Studies Building", "25 South Madison St, Iowa City");
        campusPlaces.put("Beckwith Boathouse", "1201 North Dubuque St, Iowa City");
        campusPlaces.put("Bedell Entrepreneurship Learning Lab", "322 North Clinton, Iowa City");
        campusPlaces.put("BioVentures Center", "2500 Crosspark Rd, Coralville");
        campusPlaces.put("Biology Building", "129 East Jefferson St, Iowa City");
        campusPlaces.put("Biology Building East", "210 East Iowa Ave, Iowa City");
        campusPlaces.put("Blank Honors Center", "221 North Clinton, Iowa City");
        campusPlaces.put("Bowen Science Building", "51 Newton Rd, Iowa City");
        campusPlaces.put("Bowman House", "230 North Clinton St, Iowa City");
        campusPlaces.put("Boyd Law Building", "130 Byington Rd, Iowa City");
        campusPlaces.put("Boyd Tower", "601 Newton Rd, Iowa City");
        campusPlaces.put("Burge Hall", "301 North Clinton St, Iowa City");
        campusPlaces.put("Calvin Hall", "2 West Jefferson St, Iowa City");
        campusPlaces.put("Cambus Maintenance Facility", "517 South Madison St, Iowa City");
        campusPlaces.put("Cambus Offices", "925 Stadium Dr, Iowa City");
        campusPlaces.put("Campus Recreation and Wellness Center", "309 South Madison St, Iowa City");
        campusPlaces.put("Carver Biomedical Research Building", "285 Newton Rd, Iowa City");
        campusPlaces.put("Carver River Research and Education Facility", "3388 Hwy 22, Muscatine");
        campusPlaces.put("Carver-Hawkeye Arena", "1 Elliott Dr, Iowa City");
        campusPlaces.put("Center for Disabilities and Development", "100 Hawkins Dr, Iowa City");
        campusPlaces.put("Chemistry Building", "251 North Capitol St, Iowa City");
        campusPlaces.put("Chilled Water Plant 1", "255 Hawkins Dr, Iowa City");
        campusPlaces.put("Chilled Water Plant 2 (West)", "305 Hawkins Dr, Iowa City");
        campusPlaces.put("Clinton Street Building", "700 South Clinton, Iowa City");
        campusPlaces.put("Clinton Street Music 375", "375 South Clinton, Iowa City");
        campusPlaces.put("Clinton Street Music 376", "376 South Clinton, Iowa City");
        campusPlaces.put("College of Medicine Administration Building", "451 Newton Rd, Iowa City");
        campusPlaces.put("College of Nursing Building", "50 Newton Rd, Iowa City");
        campusPlaces.put("College of Public Health Building", "105 River St., Iowa City");
        campusPlaces.put("Communications Center", "116 South Madison St, Iowa City");
        campusPlaces.put("Continuing Education Facility", "30 South Dubuque St, Iowa City");
        campusPlaces.put("Coral Center", "625 1st Ave, Coralville");
        campusPlaces.put("Corridor Technology Center", "map");
        campusPlaces.put("Currier Hall", "413 North Clinton St, Iowa City");
        campusPlaces.put("Danforth Chapel", "15 West Jefferson St, Iowa City");
        campusPlaces.put("Daum Hall", "100 Daum, Iowa City");
        campusPlaces.put("Dental Science Building", "801 Newton Rd, Iowa City");
        campusPlaces.put("Dey House", "507 North Clinton St, Iowa City");
        campusPlaces.put("Eckstein Medical Research Building", "431 Newton Rd, Iowa City");
        campusPlaces.put("Emergency Power Facility 1", "24 South Grand Ave, Iowa City");
        campusPlaces.put("Engineering Research Facility", "324 South Madison St, Iowa City");
        campusPlaces.put("English-Philosophy Building", "251 West Iowa Ave, Iowa City");
        campusPlaces.put("Environmental Health and Safety Office", "122 Grand Ave Court, Iowa City");
        campusPlaces.put("Environmental Health and Safety Office 2", "311 Grand Ave, Iowa City");
        campusPlaces.put("Environmental Health and Safety Office 3", "120 Grand Ave Court, Iowa City");
        campusPlaces.put("Environmental Health and Safety Office 4", "126 Grand Ave Court, Iowa City");
        campusPlaces.put("Environmental Management Facility", "2260 Old Farmstead Rd, Coralville");
        campusPlaces.put("Family Medical Center – Hawkeye Campus", "4051 Hawkeye Park Rd, Iowa City");
        campusPlaces.put("Field Hockey Event Management Box", "2555 Praire Meadow Dr, Iowa City");
        campusPlaces.put("Field Hockey Grandstand", "2601 Praire Meadow Dr, Iowa City");
        campusPlaces.put("Field House", "225 South Grand Ave, Iowa City");
        campusPlaces.put("Finkbine Golf Club House", "1380 Melrose Ave, Iowa City");
        campusPlaces.put("Finkbine Shop Facility", "874 North Mormon Trek, Iowa City");
        campusPlaces.put("Fleet Services", "155 West Harrison, Iowa City");
        campusPlaces.put("General Hospital", "220 Hawkins Dr, Iowa City");
        campusPlaces.put("Gerdin Athletic Learning Center", "402 Melrose Ave, Iowa City");
        campusPlaces.put("Gilbert Street Building", "1225 South Gilbert St, Iowa City");
        campusPlaces.put("Gilmore Hall", "112 North Capitol St, Iowa City");
        campusPlaces.put("Glenn Schaeffer Library", "507 North Clinton, Iowa City");
        campusPlaces.put("Graduate Painting Studios and Faculty Office", "109 River St, Iowa City");
        campusPlaces.put("Halsey Hall", "28 West Jefferson St, Iowa City");
        campusPlaces.put("Hancher Auditorium", "101 East Park Rd, Iowa City");
        campusPlaces.put("Hancher Box Office", "200 South Capitol St #193, Iowa City");
        campusPlaces.put("Hardin Library for Health Sciences", "600 Newton Rd, Iowa City");
        campusPlaces.put("Hawkeye Court Apartments", "101 Hawkeye Court, Iowa City");
        campusPlaces.put("Hawkeye Drive Apartments", "110 Hawkeye Dr, Iowa City");
        campusPlaces.put("Hawkeye Recreation Service Building", "2960 Hawkeye Park Rd, Iowa City");
        campusPlaces.put("Hawkeye Tennis and Recreation Complex", "2820 Prairie Meadows Dr, Iowa City");
        campusPlaces.put("Hawks Ridge", "100 Hawk Ridge Dr, Iowa City");
        campusPlaces.put("Helicopter Hanger", "2262 Old Farmstead Rd, Iowa City");
        campusPlaces.put("Hillcrest", "25 Byington Rd, Iowa City");
        campusPlaces.put("Hope Lodge", "750 Hawkins Dr, Iowa City");
        campusPlaces.put("Hospital Ramp 1", "230 Hawkins Dr, Iowa City");
        campusPlaces.put("Hospital Ramp 2", "120 Hawkins Dr, Iowa City");
        campusPlaces.put("Hospital Ramp 3", "800 Stadium Dr, Iowa City");
        campusPlaces.put("Housing Services Building", "2804 Prairie Meadow Dr, Iowa City");
        campusPlaces.put("Hydraulics Annex 1", "2310 Old Farmstead Rd, Coralville");
        campusPlaces.put("Hydraulics Annex 2", "2275 Old Farmstead Rd, Coralville");
        campusPlaces.put("Hydraulics East Annex", "140 West Harrison St, Iowa City");
        campusPlaces.put("Hydraulics Model Annex", "129 West Court St, Iowa City");
        campusPlaces.put("Hydraulics Research Lab", "2310 Old Farmstead Rd, Coralville");
        campusPlaces.put("Hydraulics Wind Tunnel Annex", "130 West Harrison St, Iowa City");
        campusPlaces.put("IIHR—Hydroscience & Engineering", "100 C. Maxwell Stanley Hydraulics Laboratory, Iowa City");
        campusPlaces.put("IMU Parking Ramp", "120 North Madison St, Iowa City");
        campusPlaces.put("Indoor Practice Facility (Bubble)", "910 Evashevski Dr, Iowa City");
        campusPlaces.put("Institute for Rural and Environmental Health", "2420 Old Farmstead Rd, Coralville");
        campusPlaces.put("Institute of Public Affairs", "124 Grand Ave Ct, Iowa City");
        campusPlaces.put("Iowa Advanced Technology Labs", "205 North Madison St, Iowa City");
        campusPlaces.put("Iowa Eye Bank", "2346 Mormon Trek Blvd., Iowa City");
        campusPlaces.put("Iowa Geological Survey-Oakdale", "2390 Old Farmstead Rd, Coralville");
        campusPlaces.put("Iowa House", "15 West Jefferson St, Iowa City");
        campusPlaces.put("Iowa Kidsight", "2346 Mormon Trek Blvd., Iowa City");
        campusPlaces.put("Iowa Memorial Union", "125 North Madison St, Iowa City");
        campusPlaces.put("Iowa Nonprofit Resource Center", "130 Grand Ave Ct, Iowa City");
        campusPlaces.put("J Pappajohn Pavilion", "see SRF");
        campusPlaces.put("J W Colloton Pavilion", "see SRF");
        campusPlaces.put("Jacobson Athletic Building", "map");
        campusPlaces.put("Jefferson Building", "129 East Washington St, Iowa City");
        campusPlaces.put("Jessup Hall", "5 West Jefferson St, Iowa City");
        campusPlaces.put("Karro Athletic Hall of Fame", "2425 Prairie Meadow Dr, Iowa City");
        campusPlaces.put("Kinnick Stadium", "1 Evashevski Dr, Iowa City");
        campusPlaces.put("Kuhl House", "119 West Park Rd, Iowa City");
        campusPlaces.put("Lagoon Shelter House", "230 North Riverside Dr, Iowa City");
        campusPlaces.put("Lakeside Laboratory", "1838 Highway 86 Milford, IA 51351-7267");
        campusPlaces.put("Landscape Services Complex", "415 South Madison, Iowa City");
        campusPlaces.put("Latino/Native American Cultural Center", "308 Melrose, Iowa City");
        campusPlaces.put("Laundry", "2000 Crosspark Rd, Coralville");
        campusPlaces.put("Law Admissions Building", "320 Melrose Ave, Iowa City");
        campusPlaces.put("Law, Health Policy & Disability Center", "315 Melrose Ave, Iowa City");
        campusPlaces.put("Lesbian, Gay, Bisexual, Transgender Resource Center", "125 Grand Ave Court, Iowa City");
        campusPlaces.put("Levitt Center for University Advancement", "180 North Riverside Dr, Iowa City");
        campusPlaces.put("Liberty Square", "119 2nd St, Coralville");
        campusPlaces.put("Library, Main", "125 West Washington St, Iowa City");
        campusPlaces.put("Lindquist Center", "240 South Madison St, Iowa City");
        campusPlaces.put("Linn Street Music", "209 North Linn St, Iowa City");
        campusPlaces.put("Linn Street Place", "332 South Linn St, Iowa City");
        campusPlaces.put("MacLean Hall", "2 West Washington St, Iowa City");
        campusPlaces.put("Macbride Hall", "17 North Clinton St, Iowa City");
        campusPlaces.put("Madison Street Services Building", "640 South Madison, Iowa City");
        campusPlaces.put("Market-Dubuque Building", "122 East Market St, Iowa City");
        campusPlaces.put("Mayflower Hall", "1110 North Dubuque St, Iowa City");
        campusPlaces.put("Medical Education Building", "500 Newton Rd, Iowa City");
        campusPlaces.put("Medical Education and Research Facility", "375 Newton Rd, Iowa City");
        campusPlaces.put("Medical Laboratories", "25 South Grand Ave, Iowa City");
        campusPlaces.put("Medical Research Center", "501 Newton Rd, Iowa City");
        campusPlaces.put("Medical Research Facility", "55 South Grand Ave, Iowa City");
        campusPlaces.put("Melrose Avenue Parking Facility", "610 Melrose Ave, Iowa City");
        campusPlaces.put("Mossman Business Services Building", "2222 Old Hwy 218 South, Iowa City");
        campusPlaces.put("Multi-Tenant Facility", "2501 Crosspark Rd, Coralville");
        campusPlaces.put("Music West - Interim Building", "150 North Riverside Dr, Iowa City");
        campusPlaces.put("National Advanced Driving Simulator", "2401 Oakdale Blvd., Coralville");
        campusPlaces.put("Newton Road Ramp", "360 Newton Rd, Iowa City");
        campusPlaces.put("North Campus Parking and Chilled Water Facility", "339 North Madison St, Iowa City");
        campusPlaces.put("North Hall", "20 West Davenport St, Iowa City");
        campusPlaces.put("Oakdale Research Building", "2290 Old Farmstead Rd, Coralville");
        campusPlaces.put("Oakdale Systems", "map");
        campusPlaces.put("Old Capitol", "1 North Clinton St, Iowa City");
        campusPlaces.put("Orthopaedics and Sports Medicine Clinic", "2701 Prairie Meadow Dr, Iowa City");
        campusPlaces.put("Pappajohn Biomedical Discovery Building", "169 Newton Road, Iowa City");
        campusPlaces.put("Pappajohn Business Building", "21 East Market St, Iowa City");
        campusPlaces.put("Pappajohn Education Center", "1200 Grand Ave, Des Moines");
        campusPlaces.put("Parklawn", "447-449 N Riverside Dr, Iowa City");
        campusPlaces.put("Pearl Softball Field", "2001 Highway 6 West, Iowa City");
        campusPlaces.put("Pharmacy Building", "115 South Grand Ave, Iowa City");
        campusPlaces.put("Phillips Hall", "16 North Clinton St, Iowa City");
        campusPlaces.put("Physiology Research Laboratory", "2340 Old Farmstead Rd, Coralville");
        campusPlaces.put("Plaza Centre One", "125 South Dubuque St, Iowa City");
        campusPlaces.put("Pomerantz Center", "213 North Clinton St, Iowa City");
        campusPlaces.put("Pomerantz Family Pavilion", "see SRF");
        campusPlaces.put("Power Plant", "207 West Burlington St, Iowa City");
        campusPlaces.put("President's Residence", "102 Church St, Iowa City");
        campusPlaces.put("Public Library Training Facility", "130 South Dubuque St, Iowa City");
        campusPlaces.put("Public Safety", "131 South Capitol St, Iowa City");
        campusPlaces.put("Quadrangle", "310 Grand Ave, Iowa City");
        campusPlaces.put("R J Carver Pavilion", "see SRF");
        campusPlaces.put("Recreation Building", "930 Evashevski Dr, Iowa City");
        campusPlaces.put("Rienow Hall", "320 Grand Ave, Iowa City");
        campusPlaces.put("Ronald McDonald House", "730 Hawkins Dr, Iowa City");
        campusPlaces.put("Saint Thomas More", "405 North Riverside Dr, Iowa City");
        campusPlaces.put("Sand Road Services Building", "4868 Sand Rd, Iowa City");
        campusPlaces.put("Sanitary Engineering Lab", "1020 South Clinton St, Iowa City");
        campusPlaces.put("Schaeffer Hall", "20 East Washington St, Iowa City");
        campusPlaces.put("School of Music Replacement Facility", "93 East Burlington St, Iowa City");
        campusPlaces.put("Sciences Library", "120 East Iowa Ave, Iowa City");
        campusPlaces.put("Seamans Center", "103 South Capitol St, Iowa City");
        campusPlaces.put("Seashore Hall", "301 East Jefferson St, Iowa City");
        campusPlaces.put("Shambaugh House", "430 North Clinton, Iowa City");
        campusPlaces.put("Shipping and Recieving Facility", "65 South Grand Ave, Iowa City");
        campusPlaces.put("Slater Hall", "325 Grand Ave, Iowa City");
        campusPlaces.put("South Capitol Services Building", "866 South Capitol St, Iowa City");
        campusPlaces.put("South Quadrangle", "310 South Grand Ave, Iowa City");
        campusPlaces.put("South Wing", "see SRF");
        campusPlaces.put("Speech and Hearing Center", "250 Hawkins Dr, Iowa City");
        campusPlaces.put("Spence Laboratories of Psychology", "308 East Iowa Ave, Iowa City");
        campusPlaces.put("Stanley Hall", "10 East Davenport St, Iowa City");
        campusPlaces.put("Stanley Hydraulics Laboratory", "320 South Riverside Dr, Iowa City");
        campusPlaces.put("State Historical Society Building", "402 Iowa Ave East, Iowa City");
        campusPlaces.put("State Hygienic Laboratory - Ankeny", "2220 South Ankeny Blvd, Ankeny");
        campusPlaces.put("State Hygienic Laboratory - Iowa City", "2490 Crosspark Rd, Coralville");
        campusPlaces.put("Studio Arts", "1375 Hwy 1 West, Iowa City");
        campusPlaces.put("Stuit Hall", "335 East Jefferson St, Iowa City");
        campusPlaces.put("Technology Innovation Center", "2261 Crosspark Rd, Coralville");
        campusPlaces.put("Temporary Arts Facilities", "202 North Riverside Dr");
        campusPlaces.put("Theatre Building", "200 North Riverside Dr, Iowa City");
        campusPlaces.put("Trowbridge Hall", "123 North Capitol St, Iowa City");
        campusPlaces.put("UI QuickCare – Southwest Iowa City", "767 Mormon Trek Blvd, Iowa City");
        campusPlaces.put("UI Research Park Animal Quarters A", "Coralville");
        campusPlaces.put("UI Research Park Power Plant Substation", "Coralville");
        campusPlaces.put("UI Research Park Research Building", "2290 Old Farmstead Rd, Coralville");
        campusPlaces.put("UI Research Park Research Facilities", "2330 Old Farmstead Rd, Coralville");
        campusPlaces.put("UI Research Park Studio A", "2450 Old Hospital Rd, Coralville");
        campusPlaces.put("UI Research Park Waste Storage Facility", "2260 Old Farmstead Rd, Coralville");
        campusPlaces.put("University Athletic Club", "1360 Melrose Ave, University Hts");
        campusPlaces.put("University Book Store", "201 South Clinton St, Iowa City");
        campusPlaces.put("University Capitol Centre", "200 South Capitol St, Iowa City");
        campusPlaces.put("University Services Building", "1 West Prentiss, Iowa City");
        campusPlaces.put("Van Allen Hall", "30 North Dubuque St, Iowa City");
        campusPlaces.put("Veteran's Affairs Medical Center", "601 Hwy 6 West");
        campusPlaces.put("Voxman Music Building", "300 North Riverside Dr, Iowa City");
        campusPlaces.put("Waste Incineration Plant", "2310 Crosspark Road, Coralville");
        campusPlaces.put("Water Plant", "208 West Burlington St, Iowa City");
        campusPlaces.put("Westlawn", "200 Newton Rd, Iowa City");
        campusPlaces.put("Women's Resource and Action Center", "130 North Madison, Iowa City");
    }

    public static ArrayAdapter<String> getCampusNamesArrayAdapter(Context context)
    {
        String[] places = context.getResources().getStringArray(R.array.university_places);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, places);
        return adapter;
    }
    
    public static boolean isInternetAvailable(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
         
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                              activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
