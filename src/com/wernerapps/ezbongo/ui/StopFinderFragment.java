package com.wernerapps.ezbongo.ui;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.wernerapps.ezbongo.R;
import com.wernerapps.ezbongo.api.APIManager;
import com.wernerapps.ezbongo.api.APIManager.OnStopInfoDownloaded;
import com.wernerapps.ezbongo.bll.AddressSearchHelper;
import com.wernerapps.ezbongo.bll.DistanceCalculator;
import com.wernerapps.ezbongo.bll.GeocodingManager;
import com.wernerapps.ezbongo.bll.GoogleMapManager;
import com.wernerapps.ezbongo.bll.Stop;

public class StopFinderFragment extends Fragment
{
    public StopFinderFragment()
    {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_stop_finder, container, false);
        final SupportMapFragment mapFragment = new SupportMapFragment();

        getChildFragmentManager().beginTransaction().replace(R.id.mapContainer, mapFragment).commit();

        ImageButton search = (ImageButton) rootView.findViewById(R.id.btnSearch);
        final AutoCompleteTextView searchBar = (AutoCompleteTextView) rootView.findViewById(R.id.editTextEnterAddress);
        initializeSearchAutocomplete(searchBar);
        search.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                searchForAddress(searchBar.getText().toString());
            }
        });

        rootView.post(new Runnable()
        {
            @Override
            public void run()
            {
                GoogleMapManager.getInstance().setMap(getActivity(), mapFragment.getMap(), mapFragment);
                GoogleMapManager.getInstance().getMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener()
                {
                    @Override
                    public void onInfoWindowClick(Marker arg0)
                    {
                        int stopNum = Integer.parseInt(arg0.getSnippet().substring(arg0.getSnippet().indexOf(": ") + 2));
                        
                        APIManager.getInstance().addStopInfoListener(new OnStopInfoDownloaded()
                        {
                            @Override
                            public void stopInfoDownloaded(Stop stop)
                            {
                                ((MainActivity)getActivity()).showDialog(new StopDetailDialog(stop));
                            }
                        });
                        APIManager.getInstance().requestStopInfo(stopNum);
                    }
                });
            }
        });

        return rootView;
    }

    private void initializeSearchAutocomplete(AutoCompleteTextView searchBar)
    {
        searchBar.setAdapter(AddressSearchHelper.getCampusNamesArrayAdapter(getActivity()));

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                searchForAddress(v.getText().toString());
                return false;
            }
        });
    }

    protected void searchForAddress(String query)
    {
        if (query == null || query.length() == 0)
        {
            displayErrorDialog("Address cannot be blank");
        }
        else if (!AddressSearchHelper.isInternetAvailable(getActivity()))
        {
            displayErrorDialog("No Internet Connection");
        }
        else
        {
            GeocodingManager.getInstance().setState("IOWA", "IA");
            GeocodingManager.getInstance().setMaxResult(20);
            List<Address> results = GeocodingManager.getInstance().searchForAddress(getActivity(), query);
            if (results == null)
            {
                displayErrorDialog("No internet connection - check your connection");
            }
            else if (results.size() == 0)
            {
                displayErrorDialog("No results in Iowa - try to be more specific");
            }
            else
            {
                // We have results!
                displayAddressResults(results);
            }
        }
    }

    private void displayAddressResults(final List<Address> results)
    {
        if (results.size() == 1)
        {
            // Don't bother making the user pick if they only have one choice
            handlePickedAddress(results.get(0));
        }
        else
        {
            ((MainActivity) getActivity()).showDialog(new SearchResultsDialog(results, new SearchResultsDialog.OnAddressPickedListener()
            {
                @Override
                public void addressPicked(Address address)
                {
                    handlePickedAddress(address);
                }
            }));
        }
    }

    protected void handlePickedAddress(final Address address)
    {
        GoogleMapManager.getInstance().setUserLocation(address);
        GoogleMapManager.getInstance().clearStopMarkers();

        APIManager.getInstance().addStopListingListener(new APIManager.OnStopListingDownloaded()
        {
            @Override
            public void stopListingDownloaded(SparseArray<Stop> stopListing)
            {
                addStopsWithinWalkingDistance(address, stopListing);
            }
        });
        APIManager.getInstance().requestStopListing();
    }

    private void addStopsWithinWalkingDistance(Address address, SparseArray<Stop> stopListing)
    {
        LatLng position = new LatLng(address.getLatitude(), address.getLongitude());
        DistanceCalculator calc = DistanceCalculator.getInstance();
        for (int i = 0; i < stopListing.size(); i++)
        {
            Stop stop = stopListing.valueAt(i);
            if (calc.isWithinWalkingDistance(position, stop.getPosition()))
            {
                GoogleMapManager.getInstance().addStop(stop);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_set_walking_distance)
        {
            ((MainActivity) getActivity()).showDialog(new DistanceDialog());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.stop_finder_menu, menu);
    }

    private void displayErrorDialog(String message)
    {
        new AlertDialog.Builder(getActivity()).setMessage(message).setNegativeButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        }).show();
    }
}