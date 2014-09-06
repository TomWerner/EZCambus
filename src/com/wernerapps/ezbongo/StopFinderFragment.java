package com.wernerapps.ezbongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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

public class StopFinderFragment extends Fragment
{
	private GoogleMap googleMap;
	private AutoCompleteTextView addressSearch;
	private Geocoder geoCoder;
	private Marker startMarker;
	private LatLng homeLocation = new LatLng(41.661378f, -91.536097f);

	final ArrayList<String> selectedServices = new ArrayList<String>();
	private SeekBar distanceSlider;
	private int maxDistance;
	private TextView stopText;
	private ListView stopListView;
	private int height;
	private Button findButton;
	protected HashMap<Float, Stop> closestStops = new HashMap<Float, Stop>();
	
	public void onResume()
	{
		super.onResume();

		System.out.println("onResume Stop finder");
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
		View rootView = inflater.inflate(R.layout.stop_finder_fragment,
				container, false);


//		setRetainInstance(true);
		
		
		geoCoder = new Geocoder(this.getActivity().getApplicationContext());
		initializeSearchAutocomplete(rootView);
		initializeMap(rootView);

		stopListView = (ListView) rootView.findViewById(R.id.stopListView);

		stopText = (TextView) rootView.findViewById(R.id.stopText);
		distanceSlider = (SeekBar) rootView.findViewById(R.id.stopDistanceSlider);
		distanceSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser)
			{
				if (fromUser && ((progress / 10) * 100 != maxDistance))
				{
					maxDistance = (distanceSlider.getProgress() / 10) * 100;
					stopText.setText("" + maxDistance + " Meters");
					System.out.println("Max: " + maxDistance);
					adjustVisibleStops();
				}
			}
		});

		findButton = (Button) rootView.findViewById(R.id.findButton);
		findButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				addressSearchTextSubmit(addressSearch, 0, null);
			}
		});
		System.out.println("Finished making stop finder");
		return rootView;
	}

	private void adjustVisibleStops()
	{
		maxDistance = (distanceSlider.getProgress() / 10) * 100;
		if (closestStops == null || googleMap == null || startMarker == null)
			return;
		googleMap.clear();

		Float[] distances = closestStops.keySet().toArray(
				new Float[closestStops.keySet().size()]);
		Arrays.sort(distances);

		setStartMarker(startMarker.getPosition(), startMarker.getTitle());
		MarkerOptions mark;

		Float[] keys = closestStops.keySet()
				.toArray(new Float[closestStops.size()]);
		Arrays.sort(keys);
		List<ListItem> listItems = new ArrayList<ListItem>();
		System.out.println(maxDistance);
		for (int i = 0; i < keys.length; i++)
		{
			float distance = keys[i];
			if (distance <= maxDistance)
			{
				System.out.println(distance + ", " + closestStops.get(distance));
				Stop stop = closestStops.get(distance);
				mark = new MarkerOptions().position(stop.getPosition()).title(
						stop.toString());
				googleMap.addMarker(mark);
	
				listItems.add(new ListItem(stop, false));
			}
		}
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

		StopFinderManager nsm = new StopFinderManager(getActivity()
				.getApplicationContext(), getActivity(), listItems);
		stopListView.setAdapter(nsm);
		if (height == 0)
		{
			stopListView
					.setLayoutParams(new TableLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, .01f));
			height = stopListView.getHeight();
		}
		stopListView.setLayoutParams(new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, height * 3));
	}

	private void initializeSearchAutocomplete(View rootView)
	{
		addressSearch = (AutoCompleteTextView) rootView
				.findViewById(R.id.startAddressSearch);
		String[] places = getResources().getStringArray(
				R.array.university_places);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_list_item_1, places);
		addressSearch.setAdapter(adapter);

		addressSearch
				.setOnEditorActionListener(new TextView.OnEditorActionListener()
				{
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event)
					{
						return addressSearchTextSubmit(v, actionId, event);
					}
				});
	}

	protected boolean addressSearchTextSubmit(TextView v, int actionId,
			KeyEvent event)
	{
		String query = v.getText().toString();
		if (query != null)
		{
			Address result = Utilities.getAddress(query, geoCoder);
			if (result != null)
			{
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(addressSearch.getWindowToken(), 0);
				setStartMarker(
						new LatLng(result.getLatitude(), result.getLongitude()),
						query);
				final LatLng point = new LatLng(result.getLatitude(),
						result.getLongitude());

				Utilities.createBusServiceDialog(selectedServices,
						getActivity(), new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int id)
							{
								if (selectedServices.size() == 0)
									return;
								closestStops = Utilities.stopDistances(point,
										selectedServices);
								adjustVisibleStops();
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
				.title(name).draggable(true)
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
			System.out.println("initMapStop");
			googleMap = ((SupportMapFragment) MainActivity.fragmentManager
					.findFragmentById(R.id.map)).getMap();

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
			googleMap
					.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
					{
						public void onMapLongClick(LatLng point)
						{
							googleMapOnMapLongClick(point);
						}
					});

			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					homeLocation, 16));
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			System.out.println("initMapStop2");

		}
	}

	protected void googleMapOnMapLongClick(final LatLng point)
	{
		setStartMarker(point, "Home");

		Utilities.createBusServiceDialog(selectedServices, getActivity(),
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						if (selectedServices.size() == 0)
							return;
						closestStops = Utilities.stopDistances(point, selectedServices);
						adjustVisibleStops();
					}
				});
	}

	private LatLngBounds createBounds()
	{
		float minLat = Integer.MAX_VALUE;
		float minLng = Integer.MAX_VALUE;
		float maxLat = Integer.MIN_VALUE;
		float maxLng = Integer.MIN_VALUE;
		
		if ((closestStops == null || closestStops.size() == 0) && startMarker == null)
		{
			minLat = (float) homeLocation.latitude;
			maxLat = (float) homeLocation.latitude;
			minLng = (float) homeLocation.longitude; 
			maxLng = (float) homeLocation.longitude;
		}
		
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
			System.out.println(startMarker.getPosition());
		}

		if (closestStops != null)
		{
			for (Float distance : closestStops.keySet())
			{
				if (distance <= maxDistance)
				{
					Stop stop = closestStops.get(distance);
	
					minLat = Math.min((float) stop.getPosition().latitude, minLat);
					maxLat = Math.max((float) stop.getPosition().latitude, maxLat);
					minLng = Math.min((float) stop.getPosition().longitude, minLng);
					maxLng = Math.max((float) stop.getPosition().longitude, maxLng);
				}
			}
		}
		System.out.println(new LatLng(minLat, minLng).toString() + new LatLng(maxLat,
				maxLng).toString());
		return new LatLngBounds(new LatLng(minLat, minLng), new LatLng(maxLat,
				maxLng));
	}

	public void onDestroyView()
	{
		System.out.println("Destroy stop finder");
		Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
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
