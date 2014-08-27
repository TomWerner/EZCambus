package edu.uiowa.ezcambus;

import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class RouteFinderManager extends BaseAdapter
{

	private Context context;
	private List<RouteListItem> listItems; // header titles
	private FragmentActivity fragment;
	private RouteFinderFragment routeFinder;

	public RouteFinderManager(RouteFinderFragment routeFinderFragment, Context context,
			FragmentActivity fragmentActivity, List<RouteListItem> listItems)
	{
		this.routeFinder = routeFinderFragment;
		this.context = context;
		this.fragment = fragmentActivity;
		this.listItems = listItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (position == listItems.size())
			return null;
		
		final String text1 = listItems.get(position).getS1().toString();
		final String text2 = listItems.get(position).getS2().toString();
		final Stop s1 = listItems.get(position).getS1();
		final Stop s2 = listItems.get(position).getS2();
		final RouteListItem listItem = listItems.get(position);
		
		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.route_list_item, null);
		}
		
		TextView routeNameView = (TextView) convertView.findViewById(R.id.routeNameText);
		routeNameView.setText(listItem.getRoute().getName());
		Button highlight = (Button) convertView.findViewById(R.id.highlightButton);
		highlight.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				routeFinder.addMarker(s1, s2);
			}
		});

		TextView stopNameView1 = (TextView) convertView
				.findViewById(R.id.lblListItem1);
		stopNameView1.setText(text1);
		TextView stopNameView2 = (TextView) convertView
				.findViewById(R.id.lblListItem2);
		stopNameView2.setText(text2);
		
		Button button11 = (Button) convertView.findViewById(R.id.button11);
		button11.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Utilities.createInfoPopup(s1, fragment);
			}
		});
		Button button12 = (Button) convertView.findViewById(R.id.button12);
		button12.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Utilities.saveStop(s1, context.getApplicationContext());
			}
		});
		Button button21 = (Button) convertView.findViewById(R.id.button21);
		button21.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Utilities.createInfoPopup(s2, fragment);
			}
		});
		Button button22 = (Button) convertView.findViewById(R.id.button22);
		button22.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Utilities.saveStop(s2, context.getApplicationContext());
			}
		});
		
		
		
		return convertView;
		
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
}