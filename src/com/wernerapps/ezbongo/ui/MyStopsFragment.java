package com.wernerapps.ezbongo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.wernerapps.ezbongo.R;
import com.wernerapps.ezbongo.bll.Stop;

public class MyStopsFragment extends Fragment
{

    public MyStopsFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_my_stops, container, false);
        final MainActivity activity = (MainActivity)getActivity();
        
        
        Button findStops = (Button)rootView.findViewById(R.id.btnFindStops);
        findStops.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                activity.switchToFragment(new StopFinderFragment());
            }
        });
        
        Stop[] myStops = {
                new Stop(1, "Stop 1", 0, 0),
                new Stop(2, "Stop 2", 1, 1),
                new Stop(3, "Stop 3", 2, 2),
                new Stop(4, "Stop 4", 3, 3),
        };
        ListView stopListing = (ListView)rootView.findViewById(R.id.listStopListing);
        stopListing.setAdapter(new ArrayAdapter<Stop>(activity, R.layout.list_item_stop, R.id.stop_list_textview, myStops));
        
        return rootView;
    }
}