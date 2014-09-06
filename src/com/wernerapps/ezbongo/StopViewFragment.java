package com.wernerapps.ezbongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class StopViewFragment extends Fragment
{
	/**
	 * The fragment argument representing the section number for this fragment.
	 */

	StopViewManager listAdapter;
	ExpandableListView expListView;
	List<ListGroup> listDataHeader;
	HashMap<ListGroup, List<ListItem>> listDataChild;

	public StopViewFragment()
	{
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    
	    if (isVisibleToUser) 
	    {
	    	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
	    	new HashSet<String> (sharedPrefs.getStringSet("stops", new HashSet<String>()));
	    	setupView();
	    }
	    else {  }
	}
	
	
	public void onResume()
	{
		super.onResume();

		System.out.println("onResume");
		
		setupView();
	}
	
	public void onStart()
	{
		super.onStart();
		System.out.println("onstart");
		
	}

	@Override 
	public void onSaveInstanceState(Bundle outState) 
	{
	//first saving my state, so the bundle wont be empty.
	outState.putString("WORKAROUND_FOR_BUG_19917_KEY",  "WORKAROUND_FOR_BUG_19917_VALUE");
	super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState)
	{
//		if (container == null)
//			return null;
		System.out.println("Here1");
		View rootView = inflater.inflate(R.layout.stop_view_fragment,
				container, false);
		System.out.println("Here2");
		
//		setRetainInstance(true);
		
		expListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView1);
		expListView.setOnChildClickListener(new OnChildClickListener()
		{
			public boolean onChildClick(ExpandableListView parent, View v,
			        int groupPosition, int childPosition, long id) {
			    return false;
			} 
		});
		
		
		// preparing list data
        setupView();
 
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(0);
        

		System.out.println("Here3");
		return rootView;
	}

	public void setupView()
	{
		listDataHeader = new ArrayList<ListGroup>();
		listDataChild = new HashMap<ListGroup, List<ListItem>>();

		// Adding child data
		listDataHeader.add(new ListGroup("Saved"));
		listDataHeader.add(new ListGroup("Cambus"));
		listDataHeader.add(new ListGroup("Iowa City"));
		listDataHeader.add(new ListGroup("Coralville"));

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		List<ListItem> saved = new ArrayList<ListItem>();
		Set<String> savedSet = new HashSet<String> (sharedPrefs.getStringSet("stops", new HashSet<String>()));
		String[] savedNums = savedSet.toArray(new String[savedSet.size()]);
		Arrays.sort(savedNums);
		for (String stopNum : savedNums)
		{
			saved.add(new ListItem(MainActivity.busInfo.getStop(stopNum), true));
		}

		List<ListItem> cambus = new ArrayList<ListItem>();
		List<ListItem> iowaCity = new ArrayList<ListItem>();
		List<ListItem> coralville = new ArrayList<ListItem>();
		
		for (Stop stop : MainActivity.busInfo.getStops())
		{
			if (stop.isCambusStop())
				cambus.add(new ListItem(stop, savedSet.contains(stop)));
			if (stop.isIowaCityStop())
				iowaCity.add(new ListItem(stop, savedSet.contains(stop)));
			if (stop.isCoralvilleStop())
				coralville.add(new ListItem(stop, savedSet.contains(stop)));
		}

		listDataChild.put(listDataHeader.get(0), saved); // Header, Child data
		listDataChild.put(listDataHeader.get(1), cambus);
		listDataChild.put(listDataHeader.get(2), iowaCity);
		listDataChild.put(listDataHeader.get(3), coralville);
        listAdapter = new StopViewManager(this, this.getActivity().getApplicationContext(), this.getActivity(), listDataHeader, listDataChild);
        
        if (expListView != null)
        {
        	expListView.setAdapter(listAdapter);
            expListView.expandGroup(0);
        }
	}
}
