package com.wernerapps.ezbongo.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wernerapps.ezbongo.R;
import com.wernerapps.ezbongo.bll.Route;
import com.wernerapps.ezbongo.bll.Stop;

public class StopDetailDialog extends DialogFragment
{

    private Stop stop;

    public StopDetailDialog(Stop stop)
    {
        this.stop = stop;
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View stopDetailDialogView = factory.inflate(R.layout.dialog_stop_detail, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(stopDetailDialogView);
        builder.setCancelable(true);
        final Dialog dialog = builder.create();
        
        TextView stopTitle = (TextView) stopDetailDialogView.findViewById(R.id.stopTitle);
        stopTitle.setText(stop.getStopNumber() + " - " + stop.getStopTitle());

        Button planTripToStop = (Button) stopDetailDialogView.findViewById(R.id.btnPlanTo);
        
        Button planTripFromStop = (Button) stopDetailDialogView.findViewById(R.id.btnPlanFrom);
        
        ArrayList<Route> routes = stop.getRoutes();
        System.out.println(routes);
        ArrayAdapter<Route> routeAdapter = new ArrayAdapter<Route>(getActivity(), R.layout.item_route_list, R.id.textViewRouteText, routes);
        ListView servicingRoutes = (ListView) stopDetailDialogView.findViewById(R.id.listViewServicesRoutes);
        servicingRoutes.setAdapter(routeAdapter);
        
        Button cancelButton = (Button) stopDetailDialogView.findViewById(R.id.btnCancelStopDetail);
        cancelButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        
        return dialog;
    }
    
}
