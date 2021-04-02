package org.me.gcu.equakestartercode;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import java.util.*;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MainActivity extends EarthquakeActivity implements OnClickListener, AdapterView.OnItemSelectedListener, RecyclerClickListener
{
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;

    private Spinner sortSpinner;
    private Button refreshButton;

    private TextView lastBuildDateText;

    private SortMode currentSortMode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Set up the raw links to the graphical components

        sortSpinner = (Spinner)findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sort_modes, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);
        sortSpinner.setOnItemSelectedListener(this);

        refreshButton = (Button)findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.eqRecyclerView);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);

        lastBuildDateText = (TextView)findViewById(R.id.buildDate);

        startProgress();
    }

    @Override
    public void onThreadComplete() {
        recyclerAdapter.notifyDataSetChanged();

        lastBuildDateText.setText("Earthquake data correct as of " + EarthquakeData.getLastBuildDate());
    }

    public void onClick(View view)
    {
        if (view == refreshButton) {
            startProgress(currentSortMode);
        }
    }

    // Called when a new sort mode has been clicked in the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentSortMode = SortMode.getSortMode(position);
        startProgress(currentSortMode);
    }

    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onRecyclerItemClicked(View view, int position) {
        Log.d("RecyclerView", "Clicked item at position: " + position);
        startDetailedViewActivity(position);
    }
}