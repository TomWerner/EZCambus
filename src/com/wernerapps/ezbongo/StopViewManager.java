package com.wernerapps.ezbongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StopViewManager extends BaseExpandableListAdapter
{

	private Context context;
	private List<ListGroup> listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<ListGroup, List<ListItem>> listChildData;
	private FragmentActivity fragment;
	private StopViewFragment svf;

	public StopViewManager(StopViewFragment stopViewFragment, Context context,
			FragmentActivity fragmentActivity, List<ListGroup> listDataHeader2,
			HashMap<ListGroup, List<ListItem>> listDataChild)
	{
		this.svf = stopViewFragment;
		this.context = context;
		this.fragment = fragmentActivity;
		this.listDataHeader = listDataHeader2;
		this.listChildData = listDataChild;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{
		if (childPosition == this.listChildData.get(this.listDataHeader.get(groupPosition)).size())
			return null;
		final String childText = ((ListItem) getChild(groupPosition,
				childPosition)).toString();
		final Stop stop = ((ListItem) getChild(groupPosition,
				childPosition)).getStop();
		final ListItem listItem = ((ListItem) getChild(groupPosition,
				childPosition));

		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);
		Button button1 = (Button) convertView.findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Utilities.createInfoPopup(stop, fragment,
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								saveStop(stop);
								Toast.makeText(fragment, "Saved2", Toast.LENGTH_SHORT).show();
								for (int i = 0; i < svf.expListView.getCount(); i++)
								{
									if (svf.expListView.isGroupExpanded(i))
									{
										svf.expListView.collapseGroup(i);
										svf.expListView.expandGroup(i);
									}
								}
								setupView();
								
							}
						});
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
					for (int i = 0; i < svf.expListView.getCount(); i++)
					{
						if (svf.expListView.isGroupExpanded(i))
						{
							svf.expListView.collapseGroup(i);
							svf.expListView.expandGroup(i);
						}
					}
				}
				else
				{
					removeStop(stop);
					Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
					for (int i = 0; i < svf.expListView.getCount(); i++)
					{
						if (svf.expListView.isGroupExpanded(i))
						{
							svf.expListView.collapseGroup(i);
							svf.expListView.expandGroup(i);
						}
					}
				}
			}
		});
		
		if (listItem.isSaved())
		{
			button2.setText("Delete\nStop");
		}
		else
		{
			button2.setText("Save\nStop");
			
		}

		txtListChild.setText(childText);
		return convertView;
	}


	public void removeStop(Stop stop)
	{
		Utilities.removeStop(stop, fragment);

		setupView();
	}

	public void saveStop(Stop stop)
	{
		Utilities.saveStop(stop, fragment);
		
		setupView();
	}

	public void setupView()
	{
		
		listDataHeader = new ArrayList<ListGroup>();
		listChildData = new HashMap<ListGroup, List<ListItem>>();

		// Adding child data
		listDataHeader.add(new ListGroup("Saved"));
		listDataHeader.add(new ListGroup("Cambus"));
		listDataHeader.add(new ListGroup("Iowa City"));
		listDataHeader.add(new ListGroup("Coralville"));

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		List<ListItem> saved = new ArrayList<ListItem>();
		Set<String> savedSet = new HashSet<String> (sharedPrefs.getStringSet("stops", new HashSet<String>()));
		System.out.println(savedSet);
		String[] savedNums = savedSet.toArray(new String[savedSet.size()]);
		Arrays.sort(savedNums);
		for (String stopNum : savedNums)
		{
			System.out.println("Saved: " + MainActivity.busInfo.getStop(stopNum));
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

		listChildData.put(listDataHeader.get(0), saved); // Header, Child data
		listChildData.put(listDataHeader.get(1), cambus);
		listChildData.put(listDataHeader.get(2), iowaCity);
		listChildData.put(listDataHeader.get(3), coralville);
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{
		String headerTitle = ((ListGroup) getGroup(groupPosition)).toString();
		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon)
	{
		return this.listChildData.get(this.listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		return this.listChildData.get(this.listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return this.listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount()
	{
		return this.listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}
}