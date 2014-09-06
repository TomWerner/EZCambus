package com.wernerapps.ezbongo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StopFinderManager extends BaseAdapter
{

	private Context context;
	private List<ListItem> listItems; // header titles
	private FragmentActivity fragment;

	public StopFinderManager(Context context,
			FragmentActivity fragmentActivity, List<ListItem> listItems)
	{
		this.context = context;
		this.fragment = fragmentActivity;
		this.listItems = listItems;
	}
	


	@Override
	public int getCount()
	{
		return listItems.size();
	}

	@Override
	public Object getItem(int position)
	{
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (position == listItems.size())
			return null;
		final String text = listItems.get(position).toString();
		final Stop stop = listItems.get(position).getStop();
		final ListItem listItem = listItems.get(position);
		
		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);
		txtListChild.setText(text);
		
		Button button1 = (Button) convertView.findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				createInfoPopup(stop);
			}
		});
		Button button2 = (Button) convertView.findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (!listItem.isSaved())
				{
					saveStop(stop);
					Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
				}
				else
				{
					removeStop(stop);
					Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return convertView;
		
	}


	private void removeStop(Stop stop)
	{
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		Editor edit = sharedPrefs.edit();
		Set<String> s = new HashSet<String> (sharedPrefs.getStringSet("stops", new HashSet<String>()));
		s.remove(stop.getStopNumber());
		edit.putStringSet("stops", s);
		edit.commit();
	}

	private void saveStop(Stop stop)
	{
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		Editor edit = sharedPrefs.edit();
		Set<String> s = new HashSet<String> (sharedPrefs.getStringSet("stops", new HashSet<String>()));
		s.add(stop.getStopNumber());
		edit.putStringSet("stops", s);
		edit.commit();
	}


	private void createInfoPopup(final Stop stop)
	{
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(fragment);
		builder.setTitle("Incoming Routes");
		
		AsyncTask<Stop, Void, ArrayList<String>> query = new AsyncTask<Stop, Void, ArrayList<String>>()
		{

			@Override
			protected ArrayList<String> doInBackground(Stop... arg0)
			{
				ArrayList<String> incomingTimes = null;
				try
				{
					String url = "http://api.ebongo.org/prediction?format=json&stopid=" + arg0[0].getStopNumber() + "&api_key=hfjiLcSjgJUQnhqn1WpGu4AfKBZX7Eo1";
					System.out.println(url);
					JSONObject result = readJsonFromUrl(url);
					
					incomingTimes  = new ArrayList<String>();
					JSONArray preds = result.getJSONArray("predictions");
					for (int i = 0; i < preds.length(); i++)
					{
						JSONObject pred = preds.getJSONObject(i);
						String route = pred.getString("title");
						String time = pred.getString("minutes");
						String direction = pred.getString("direction");
						incomingTimes.add(route + "( " + direction + " ): " + time + " minutes");
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
		
		try
		{
			final ArrayList<String> data = query.execute(stop).get();
			builder.setPositiveButton("Save", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					saveStop(stop);
					Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
				}
			});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
						}
					});

			builder.setItems(data.toArray(new String[data.size()]), null);
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
	 * @param url The url to request data from
	 * @return JSONObject of the data returned by the url
	 * @throws IOException
	 * @throws JSONException
	 */
	private JSONObject readJsonFromUrl(String url) throws IOException,
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