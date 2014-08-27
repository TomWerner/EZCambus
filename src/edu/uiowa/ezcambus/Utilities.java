package edu.uiowa.ezcambus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class Utilities
{
	public static final String[] busServices = { "uiowa", "iowa-city",
			"coralville" };
	public static final String[] busServiceNames = { "Cambus", "Iowa City",
			"Coralville" };

	public static void createBusServiceDialog(
			final ArrayList<String> selectedServices, Context context,
			DialogInterface.OnClickListener okMethod)
	{
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Bus service stops to look for");
		selectedServices.removeAll(selectedServices);
		builder.setMultiChoiceItems(busServiceNames, null,
				new DialogInterface.OnMultiChoiceClickListener()
				{
					// indexSelected contains the index of item (of which
					// checkbox checked)
					@Override
					public void onClick(DialogInterface dialog,
							int indexSelected, boolean isChecked)
					{
						if (isChecked)
						{
							// If the user checked the item, add it to the
							// selected items
							// write your code when user checked the checkbox
							selectedServices.add(busServices[indexSelected]);
						}
						else if (selectedServices.contains(indexSelected))
						{
							// Else, if the item is already in the array, remove
							// it
							// write your code when user Uchecked the checkbox
							selectedServices.remove(busServices[indexSelected]);
						}
					}
				})
				// Set the action buttons
				.setPositiveButton("OK", okMethod)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int id)
							{
								selectedServices.removeAll(selectedServices);
							}
						});

		dialog = builder.create();// AlertDialog dialog; create like this
									// outside onClick
		dialog.show();
	}
	
	public static Address getAddress(String query, Geocoder geoCoder)
	{
		try
		{
			Address result = null;
			if (BusInformationManager.CAMPUS_PLACES.containsKey(query))
			{
				result = geoCoder.getFromLocationName(
						BusInformationManager.CAMPUS_PLACES.get(query), 1).get(
						0);
			}
			else
			{
				for (Address a : geoCoder.getFromLocationName(query, 30))
				{
					if (a.getAdminArea().equalsIgnoreCase("IA")
							|| a.getAdminArea().equalsIgnoreCase("Iowa"))
					{
						result = a;
					}
				}
			}
			return result;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void saveStop(Stop stop, Context context)
	{
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = sharedPrefs.edit();
		Set<String> s = new HashSet<String>(sharedPrefs.getStringSet("stops",
				new HashSet<String>()));
		if (stop != null)
		{
			s.add(stop.getStopNumber());
			edit.putStringSet("stops", s);
		}
		edit.commit();
	}
	
	public static void removeStop(Stop stop, Context context)
	{
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		Editor edit = sharedPrefs.edit();
		Set<String> s = new HashSet<String> (sharedPrefs.getStringSet("stops", new HashSet<String>()));
		if (stop != null)
		{
			s.remove(stop.getStopNumber());
			edit.putStringSet("stops", s);
		}
		edit.commit();
	}

	public static void createInfoPopup(final Stop stop, final Context fragment)
	{
		createInfoPopup(stop, 
				fragment,
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						Utilities.saveStop(stop,
								fragment.getApplicationContext());
						Toast.makeText(fragment, "Saved", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}
	
	public static void createInfoPopup(final Stop stop, final Context fragment,
			DialogInterface.OnClickListener saveMethod)
	{
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(fragment);
		builder.setTitle("Incoming Routes");

		try
		{
			final ArrayList<String> data = getIncomingTimes(stop);
			builder.setPositiveButton("Save Stop", saveMethod);
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
						}
					});

			if (data != null)
				builder.setItems(data.toArray(new String[data.size()]), null);
			else
				builder.setItems(new String[]{"Error loading incoming times"}, null);
			dialog = builder.create();// AlertDialog dialog; create like this
										// outside onClick
			dialog.show();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
		}

	}

	public static ArrayList<String> getIncomingTimes(Stop stop)
			throws InterruptedException, ExecutionException
	{
		AsyncTask<Stop, Void, ArrayList<String>> query = new AsyncTask<Stop, Void, ArrayList<String>>()
		{

			@Override
			protected ArrayList<String> doInBackground(Stop... arg0)
			{
				ArrayList<String> incomingTimes = null;
				try
				{
					String url = "http://api.ebongo.org/prediction?format=json&stopid="
							+ arg0[0].getStopNumber()
							+ "&api_key=hfjiLcSjgJUQnhqn1WpGu4AfKBZX7Eo1";
					JSONObject result = readJsonFromUrl(url);

					incomingTimes = new ArrayList<String>();
					JSONArray preds = result.getJSONArray("predictions");
					for (int i = 0; i < preds.length(); i++)
					{
						JSONObject pred = preds.getJSONObject(i);
						String route = pred.getString("title");
						String time = pred.getString("minutes");
						String direction = pred.getString("direction");
						incomingTimes.add(route + " (" + direction + ") : "
								+ time + " minutes");
					}
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return incomingTimes;
			}

		};
		return query.execute(stop).get();
	}

	/**
	 * 
	 * @param point
	 * @param selectedServices
	 * @return ArrayList of stops, sorted by distance, closest first
	 */
	public static HashMap<Float, Stop> stopDistances(LatLng point,
			ArrayList<String> selectedServices)
	{
		float[] distance = new float[1];
		HashMap<Float, Stop> stopDistances = new HashMap<Float, Stop>();

		for (Stop stop : MainActivity.busInfo.getStops())
		{
			LatLng pos1 = point;
			LatLng pos2 = stop.getPosition();
			Location.distanceBetween(pos1.latitude, pos1.longitude,
					pos2.latitude, pos2.longitude, distance);
			if (stop.inServiceList(selectedServices))
				stopDistances.put(Float.valueOf(distance[0]), stop);
		}

		return stopDistances;
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

	/**
	 * This method creates a JSON object from data returned by an HTTP GET
	 * request to the given url.
	 * 
	 * @param url
	 *            The url to request data from
	 * @return JSONObject of the data returned by the url
	 * @throws IOException
	 * @throws JSONException
	 */
	private static JSONObject readJsonFromUrl(String url) throws IOException,
			JSONException
	{
		InputStream is = new URL(url).openStream();
		try
		{
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		}
		finally
		{
			is.close();
		}
	}

}
