package com.wernerapps.ezbongo.ui;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.wernerapps.ezbongo.R;
import com.wernerapps.ezbongo.bll.Stop;

public class StopInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
{
    private HashMap<Marker, Stop> stopMarkerMap;
    private Context context;
    private ViewGroup parent;

    public StopInfoWindowAdapter(Context context, HashMap<Marker, Stop> stopMarkerMap, SupportMapFragment parent)
    {
        this.stopMarkerMap = stopMarkerMap;
        this.context = context;
        this.parent = (ViewGroup) parent.getView();
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        Stop stop = stopMarkerMap.get(marker);
        View window = null;
           
        if (stop == null)
        {
            window = LayoutInflater.from(context).inflate(R.layout.popup_location_marker_info, parent, false);
            String title = marker.getTitle();
            TextView txtTitle = ((TextView) window.findViewById(R.id.title));
            txtTitle.setText(title);
        }
        else
        {
            window = LayoutInflater.from(context).inflate(R.layout.popup_stop_marker_info, parent, false);
            String title = marker.getTitle();
    
            TextView txtTitle = ((TextView) window.findViewById(R.id.stopTitle));
            txtTitle.setText("" + stop.getStopNumber() + " - " + stop.getStopTitle());
            
//            ListView routeList = (ListView)window.findViewById(R.id.listViewRoutes);
        }
        return window;

    }

    @Override
    public View getInfoContents(Marker marker)
    {

        // this method is not called if getInfoWindow(Marker) does not return
        // null

        return null;

    }

}
