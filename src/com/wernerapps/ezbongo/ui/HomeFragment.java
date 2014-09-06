package com.wernerapps.ezbongo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.wernerapps.ezbongo.R;

public class HomeFragment extends Fragment
{

    public HomeFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final MainActivity activity = (MainActivity)getActivity();
        
        Button myStops = (Button)rootView.findViewById(R.id.btnMyStops);
        myStops.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                activity.switchToFragment(new MyStopsFragment());
            }
        });
        
        Button myRoutes = (Button)rootView.findViewById(R.id.btnMyRoutes);
        myRoutes.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //activity.switchToFragment(new MyRoutesFragment());
            }
        });
        
        Button findStops = (Button)rootView.findViewById(R.id.btnFindStops);
        findStops.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                activity.switchToFragment(new StopFinderFragment());
            }
        });
        
        Button busLocations = (Button)rootView.findViewById(R.id.btnLiveData);
        busLocations.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //activity.switchToFragment(new BusLocationsFragment());
            }
        });
        
        Button tripPlanner = (Button)rootView.findViewById(R.id.btnTripPlanner);
        tripPlanner.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //activity.switchToFragment(new TripPlannerFragment());
            }
        });
        
        return rootView;
    }
}