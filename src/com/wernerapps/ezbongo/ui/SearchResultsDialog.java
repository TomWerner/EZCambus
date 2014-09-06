package com.wernerapps.ezbongo.ui;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.wernerapps.ezbongo.R;

public class SearchResultsDialog extends DialogFragment
{
    private List<Address> results;
    private OnAddressPickedListener addressPickedListener;

    public SearchResultsDialog(List<Address> results, OnAddressPickedListener onAddressPickedListener)
    {
        this.results = results;
        this.addressPickedListener = onAddressPickedListener;
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View resultsDialogView = factory.inflate(R.layout.dialog_search_results, null);
        
        final ListView resultsList = (ListView) resultsDialogView.findViewById(R.id.listResultsList);
        Button cancelButton = (Button) resultsDialogView.findViewById(R.id.btnCancelDialog);
        
        resultsList.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                addressPickedListener.addressPicked(results.get(position));
                dismiss();
            }
        });

        setupResultsListView(resultsList);
        resultsList.setSelected(false);

        cancelButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Search Results");
        builder.setView(resultsDialogView);

        return builder.create();
    }

    private void setupResultsListView(ListView resultsListView)
    {
        String[] resultStrings = new String[results.size()];
        for (int i = 0; i < results.size(); i++)
        {
            String addressString = "";
            for (int k = 0; k < results.get(i).getMaxAddressLineIndex(); k++)
            {
                addressString += results.get(i).getAddressLine(k);
                if (k + 1 < results.get(i).getMaxAddressLineIndex())
                    addressString += "\n";
            }
            resultStrings[i] = addressString;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, resultStrings);
        resultsListView.setAdapter(adapter);
    }

    public interface OnAddressPickedListener
    {
        public void addressPicked(Address address);
    }
}
