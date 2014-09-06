package com.wernerapps.ezbongo.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.wernerapps.ezbongo.R;
import com.wernerapps.ezbongo.bll.DistanceCalculator;

public class DistanceDialog extends DialogFragment
{
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View distanceDialogView = factory.inflate(R.layout.dialog_distance_picker, null);

        final EditText distanceValue = (EditText) distanceDialogView.findViewById(R.id.editTextDistanceValue);
        distanceValue.setText(String.format("%.2f", DistanceCalculator.getInstance().getWalkingDistance()));

        final AlertDialog distanceDialog = new AlertDialog.Builder(getActivity()).setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                submitWalkingDistance(Double.parseDouble(distanceValue.getText().toString()));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        }).setTitle("Walking Distance to Stops").create();
        distanceDialog.setView(distanceDialogView);
        

        distanceValue.setOnEditorActionListener(new OnEditorActionListener()
        {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                {
                    submitWalkingDistance(Double.parseDouble(distanceValue.getText().toString()));
                    distanceDialog.dismiss();
                }
                return false;
            }
        });

        return distanceDialog;
    }
    
    private void submitWalkingDistance(double distance)
    {
        DistanceCalculator.getInstance().setWalkingDistance(Math.abs(distance));
    }

}
