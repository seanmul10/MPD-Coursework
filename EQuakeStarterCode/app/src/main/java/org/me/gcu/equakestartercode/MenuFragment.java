package org.me.gcu.equakestartercode;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import org.me.gcu.equakestartercode.R;

public class MenuFragment extends Fragment implements View.OnClickListener
{
    Button listViewButton;
    Button dateRangeButton;
    Button mapButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, parent, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listViewButton = (Button)view.findViewById(R.id.listViewButton);
        listViewButton.setOnClickListener(this);

        dateRangeButton = (Button)view.findViewById(R.id.dateRangeButton);
        dateRangeButton.setOnClickListener(this);

        mapButton = (Button)view.findViewById(R.id.mapButton);
        mapButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (view == listViewButton) {
            intent = new Intent(getActivity(), MainActivity.class);
        }

        if (view == dateRangeButton) {
            intent = new Intent(getActivity(), SearchByDateActivity.class);
        }

        if (view == mapButton) {
            intent = new Intent(getActivity(), MapActivity.class);
        }
        if (intent != null)
            startActivity(intent);
    }
}
