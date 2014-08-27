package edu.uiowa.ezcambus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class RouteFinderFragment extends Fragment
{
	private GoogleMap googleMap;
	private AutoCompleteTextView addressStart;
	private Geocoder geoCoder;
	private Marker startMarker;
	private LatLng homeLocation = new LatLng(41.661378f, -91.536097f);

	final ArrayList<String> selectedServices = new ArrayList<String>();
	private ListView stopListView;
	private int height;
	private AutoCompleteTextView addressEnd;
	private Marker endMarker;
	private HashMap<Float, Stop> closestStartStops;
	private HashMap<Float, Stop> closestEndStops;
	private ArrayList<Stop> placedStops = new ArrayList<Stop>();
	private Button findButton;

	public void onResume()
	{
		super.onResume();

		System.out.println("onResume route finder");
	}
	
	@Override 
	public void onSaveInstanceState(Bundle outState) 
	{
	//first saving my state, so the bundle wont be empty.
	outState.putString("WORKAROUND_FOR_BUG_19917_KEY",  "WORKAROUND_FOR_BUG_19917_VALUE");
	super.onSaveInstanceState(outState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState)
	{
//		if (container == null)
//			return null;
		View rootView = inflater.inflate(R.layout.route_finder_fragment,
				container, false);


//		setRetainInstance(true);
		
		geoCoder = new Geocoder(this.getActivity().getApplicationContext());
		initializeSearchAutocomplete(rootView);
		initializeMap(rootView);

		stopListView = (ListView) rootView.findViewById(R.id.routeListView);

		findButton = (Button) rootView.findViewById(R.id.findEndButton);
		findButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				addressSearchTextSubmit(addressEnd);
			}
		});
		System.out.println("Finished making route finder");
		return rootView;
	}

	protected void findRoute()
	{
		Float[] startDists = closestStartStops.keySet().toArray(
				new Float[closestStartStops.size()]);
		Float[] endDists = closestEndStops.keySet().toArray(
				new Float[closestEndStops.size()]);

		HashMap<String, BestRouteInfo> bestRoutes = new HashMap<String, BestRouteInfo>();
		HashMap<String, Route> routes = MainActivity.busInfo.getRoutes();
		Set<String> keys = routes.keySet();

		for (int i = 0; i < startDists.length; i++)
		{
			for (int k = 0; k < endDists.length; k++)
			{
				for (String key : keys)
				{
					Route route = routes.get(key);
					if (selectedServices.contains(route.getAgency())
							&& route.containsStops(
									closestStartStops.get(startDists[i]),
									closestEndStops.get(endDists[k])))
					{
						if (!bestRoutes.containsKey(route.getName())
								|| (bestRoutes.containsKey(route.getName()) && startDists[i]
										+ endDists[k] < bestRoutes.get(route
										.getName()).totalDistance))
						{
							BestRouteInfo temp = new BestRouteInfo(
									startDists[i] + endDists[k],
									closestStartStops.get(startDists[i]),
									closestEndStops.get(endDists[k]), route);

							System.out.println("Total distance: "
									+ route.getName() + ", "
									+ temp.totalDistance + "m");
							bestRoutes.put(route.getName(), temp);
						}
					}
				}
			}
		}

		String[] routeNames = bestRoutes.keySet().toArray(
				new String[bestRoutes.size()]);
		ArrayList<RouteListItem> listItems = new ArrayList<RouteListItem>();
		for (int i = 0; i < routeNames.length; i++)
		{
			BestRouteInfo route = bestRoutes.get(routeNames[i]);
			if (route.totalDistance < 2000)
			{
				listItems.add(new RouteListItem(route.s1, route.s2,
						route.route, route.totalDistance));
			}
		}
		Collections.sort(listItems);

		RouteFinderManager brm = new RouteFinderManager(this, getActivity()
				.getApplicationContext(), getActivity(), listItems);
		stopListView.setAdapter(brm);
		if (height == 0)
		{
			stopListView
					.setLayoutParams(new TableLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, .01f));
			height = stopListView.getHeight();
		}
		stopListView.setLayoutParams(new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, (int) (height * 3)));
	}

	private class BestRouteInfo
	{
		public float totalDistance;
		public Stop s1;
		public Stop s2;
		public Route route;

		public BestRouteInfo(float totalDistance, Stop s1, Stop s2, Route route)
		{
			this.totalDistance = totalDistance;
			this.s1 = s1;
			this.s2 = s2;
			this.route = route;
		}

		public int hashCode()
		{
			return route.getName().hashCode();
		}
	}

	private void initializeSearchAutocomplete(View rootView)
	{
		addressStart = (AutoCompleteTextView) rootView
				.findViewById(R.id.startAddressSearch);
		addressEnd = (AutoCompleteTextView) rootView
				.findViewById(R.id.endAddressSearch);

		String[] places = getResources().getStringArray(
				R.array.university_places);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_list_item_1, places);
		addressStart.setAdapter(adapter);

		addressStart
				.setOnEditorActionListener(new TextView.OnEditorActionListener()
				{
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event)
					{
						addressEnd.requestFocus();
						return true;
					}
				});

		addressEnd.setAdapter(adapter);

		addressEnd
				.setOnEditorActionListener(new TextView.OnEditorActionListener()
				{
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event)
					{
						return addressSearchTextSubmit(v);
					}
				});
	}

	protected boolean addressSearchTextSubmit(TextView v)
	{
		String query = v.getText().toString();
		if (query != null && addressStart.getText().length() > 0
				&& addressEnd.getText().length() > 0)
		{
			Address startAddress = Utilities.getAddress(addressStart.getText()
					.toString(), geoCoder);
			Address endAddress = Utilities.getAddress(addressEnd.getText()
					.toString(), geoCoder);
			final LatLng start = new LatLng(startAddress.getLatitude(),
					startAddress.getLongitude());
			final LatLng stop = new LatLng(endAddress.getLatitude(),
					endAddress.getLongitude());

			if (startAddress != null && endAddress != null)
			{
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(addressStart.getWindowToken(), 0);
				googleMap.clear();
				setStartMarker(start, query);
				setEndMarker(stop, query);

				Utilities.createBusServiceDialog(selectedServices,
						getActivity(), new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int id)
							{
								if (selectedServices.size() == 0)
									return;
								closestStartStops = Utilities.stopDistances(
										start, selectedServices);
								closestEndStops = Utilities.stopDistances(stop,
										selectedServices);
								findRoute();
							}
						});
			}
			else
			{
				createDialog(
						"Location not found",
						"Sorry, but the address entered was not found or not specific enough. Try adding \"Iowa City\" to it.");

			}
		}

		return true;
	}

	private void setEndMarker(LatLng point, String name)
	{
		if (endMarker != null)
			endMarker.remove();

		MarkerOptions mark = new MarkerOptions()
				.position(new LatLng(point.latitude, point.longitude))
				.title(name + "\n(start)").draggable(true)
				.icon(BitmapDescriptorFactory.fromAsset("endIcon.png"))
				.anchor(.5f, .5f);

		endMarker = googleMap.addMarker(mark);
		try
		{
			googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
					createBounds(), 100));
		}
		catch (IllegalStateException e)
		{
			Toast.makeText(getActivity(), "Your screen is too small for optimum usage", Toast.LENGTH_SHORT).show();
		}
	}

	private void createDialog(String title, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				// Do something
			}
		});
		builder.show();
	}

	private void setStartMarker(LatLng point, String name)
	{
		if (startMarker != null)
			startMarker.remove();

		MarkerOptions mark = new MarkerOptions()
				.position(new LatLng(point.latitude, point.longitude))
				.title(name + "\n(start)").draggable(true)
				.icon(BitmapDescriptorFactory.fromAsset("homeIcon.png"))
				.anchor(.5f, .5f);

		startMarker = googleMap.addMarker(mark);
		try
		{
			googleMap.animateCamera(CameraUpdateFactory.newLatLng(startMarker
					.getPosition()));
			googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
					createBounds(), 100));
		}
		catch (IllegalStateException e)
		{
			Toast.makeText(getActivity(), "Your screen is too small for optimum usage", Toast.LENGTH_SHORT).show();
		}
	}

	private void initializeMap(View rootView)
	{
		if (googleMap == null)
		{
			googleMap = ((SupportMapFragment) MainActivity.fragmentManager
					.findFragmentById(R.id.routeFinder)).getMap();

			// check if map is created successfully or not
			if (googleMap == null)
			{
				Toast.makeText(getActivity().getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}

		if (googleMap != null)
		{
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					homeLocation, 16));
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					homeLocation, 16));
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		}
	}

	private LatLngBounds createBounds()
	{
		float minLat = (float) homeLocation.latitude;
		float maxLat = (float) homeLocation.latitude;
		float minLng = (float) homeLocation.longitude;
		float maxLng = (float) homeLocation.longitude;

		if (startMarker != null)
		{
			minLat = Math.min((float) startMarker.getPosition().latitude,
					minLat);
			maxLat = Math.max((float) startMarker.getPosition().latitude,
					maxLat);
			minLng = Math.min((float) startMarker.getPosition().longitude,
					minLng);
			maxLng = Math.max((float) startMarker.getPosition().longitude,
					maxLng);
		}
		if (endMarker != null)
		{
			minLat = Math.min((float) endMarker.getPosition().latitude, minLat);
			maxLat = Math.max((float) endMarker.getPosition().latitude, maxLat);
			minLng = Math
					.min((float) endMarker.getPosition().longitude, minLng);
			maxLng = Math
					.max((float) endMarker.getPosition().longitude, maxLng);
		}

		for (Stop stop : placedStops)
		{
			minLat = Math.min((float) stop.getPosition().latitude, minLat);
			maxLat = Math.max((float) stop.getPosition().latitude, maxLat);
			minLng = Math.min((float) stop.getPosition().longitude, minLng);
			maxLng = Math.max((float) stop.getPosition().longitude, maxLng);
		}

		return new LatLngBounds(new LatLng(minLat, minLng), new LatLng(maxLat,
				maxLng));
	}

	public void addMarker(Stop... stops)
	{
		googleMap.clear();
		setStartMarker(startMarker.getPosition(), "Start");
		setEndMarker(endMarker.getPosition(), "End");
		placedStops = new ArrayList<Stop>();

		for (Stop stop : stops)
		{
			MarkerOptions marker = new MarkerOptions()
					.position(stop.getPosition())
					.title(stop.getStopNumber() + " : " + stop.getName())
					.draggable(false);
			googleMap.addMarker(marker);
			placedStops.add(stop);
		}
		try
		{
			googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
					createBounds(), 100));
		}
		catch (IllegalStateException e)
		{
			Toast.makeText(getActivity(), "Your screen is too small for optimum usage", Toast.LENGTH_SHORT).show();
		}
	}

	public void onDestroyView()
	{
		System.out.println("Destroy route finder");
		Fragment fragment = (getFragmentManager()
				.findFragmentById(R.id.routeFinder));
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		googleMap = null;
		if (fragment == null)
		{
			super.onDestroyView();
			return;
		}
		ft.remove(fragment);
		ft.commitAllowingStateLoss();
		super.onDestroyView();
	}
}
