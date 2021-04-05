package org.me.gcu.equakestartercode;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

public class SearchResultsFragment extends Fragment implements View.OnClickListener
{
    String startDate;
    String endDate;

    TextView searchResultsText;

    TextView largestEqLocation;
    TextView largestEqDate;
    TextView largestEqMagnitude;

    TextView northEqLocation;
    TextView northEqDate;
    TextView northEqLatLon;

    TextView eastEqLocation;
    TextView eastEqDate;
    TextView eastEqLatLon;

    TextView southEqLocation;
    TextView southEqDate;
    TextView southEqLatLon;

    TextView westEqLocation;
    TextView westEqDate;
    TextView westEqLatLon;

    TextView shallowestEqLocation;
    TextView shallowestEqDate;
    TextView shallowestEqDepth;

    TextView deepestEqLocation;
    TextView deepestEqDate;
    TextView deepestEqDepth;

    public SearchResultsFragment() {

    }

    public SearchResultsFragment(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, parent, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            startDate = savedInstanceState.getString("startDate");
            endDate = savedInstanceState.getString("endDate");
        }

        searchResultsText = (TextView)view.findViewById(R.id.searchResults);

        largestEqLocation = (TextView)view.findViewById(R.id.largestEqLocation);
        largestEqDate = (TextView)view.findViewById(R.id.largestEqDate);
        largestEqMagnitude = (TextView)view.findViewById(R.id.largestEqMagnitude);

        northEqLocation = (TextView)view.findViewById(R.id.northEqLocation);;
        northEqDate = (TextView)view.findViewById(R.id.northEqDate);
        northEqLatLon = (TextView)view.findViewById(R.id.northEqLatLong);

        eastEqLocation = (TextView)view.findViewById(R.id.eastEqLocation);
        eastEqDate = (TextView)view.findViewById(R.id.eastEqDate);
        eastEqLatLon = (TextView)view.findViewById(R.id.eastEqLatLong);

        southEqLocation = (TextView)view.findViewById(R.id.southEqLocation);
        southEqDate = (TextView)view.findViewById(R.id.southEqDate);
        southEqLatLon = (TextView)view.findViewById(R.id.southEqLatLong);

        westEqLocation = (TextView)view.findViewById(R.id.westEqLocation);
        westEqDate = (TextView)view.findViewById(R.id.westEqDate);
        westEqLatLon = (TextView)view.findViewById(R.id.westEqLatLong);

        shallowestEqLocation = (TextView)view.findViewById(R.id.shallowestEqLocation);
        shallowestEqDate = (TextView)view.findViewById(R.id.shallowestEqDate);
        shallowestEqDepth = (TextView)view.findViewById(R.id.shallowestEqDepth);

        deepestEqLocation = (TextView)view.findViewById(R.id.deepestEqLocation);
        deepestEqDate = (TextView)view.findViewById(R.id.deepestEqDate);
        deepestEqDepth = (TextView)view.findViewById(R.id.deepestEqDepth);

        update();
    }

    @Override
    public void onClick(View view) {

    }

    public void update() {
        List<Earthquake> earthquakes = EarthquakeData.getEarthquakesInRange(startDate, endDate);

        searchResultsText.setText("Search returned " + earthquakes.size() + " earthquake(s) in the range of dates entered.");

        Earthquake largestEarthquake = EarthquakeData.getBiggestEarthquake(earthquakes);
        largestEqLocation.setText(largestEarthquake.getLocation());
        largestEqDate.setText(largestEarthquake.getDate());
        largestEqMagnitude.setText(Float.toString(largestEarthquake.getMagnitude()));

        Earthquake northernmostEarthquake = EarthquakeData.getMostNorthernEarthquake(earthquakes);
        northEqLocation.setText(northernmostEarthquake.getLocation());
        northEqDate.setText(northernmostEarthquake.getDate());
        northEqLatLon.setText(northernmostEarthquake.getLatLngString());

        Earthquake easternmostEarthquake = EarthquakeData.getMostEasterlyEarthquake(earthquakes);
        eastEqLocation.setText(easternmostEarthquake.getLocation());
        eastEqDate.setText(easternmostEarthquake.getDate());
        eastEqLatLon.setText(easternmostEarthquake.getLatLngString());

        Earthquake southernmostEarthquake = EarthquakeData.getMostSoutherlyEarthquake(earthquakes);
        southEqLocation.setText(southernmostEarthquake.getLocation());
        southEqDate.setText(southernmostEarthquake.getDate());
        southEqLatLon.setText(southernmostEarthquake.getLatLngString());

        Earthquake westernmostEarthquake = EarthquakeData.getMostWesterlyEarthquake(earthquakes);
        westEqLocation.setText(westernmostEarthquake.getLocation());
        westEqDate.setText(westernmostEarthquake.getDate());
        westEqLatLon.setText(westernmostEarthquake.getLatLngString());

        Earthquake shallowestEarthquake = EarthquakeData.getShallowestEarthquake(earthquakes);
        shallowestEqLocation.setText(shallowestEarthquake.getLocation());
        shallowestEqDate.setText(shallowestEarthquake.getDate());
        shallowestEqDepth.setText(shallowestEarthquake.getDepthString());

        Earthquake deepestEarthquake = EarthquakeData.getDeepestEarthquake(earthquakes);
        deepestEqLocation.setText(deepestEarthquake.getLocation());
        deepestEqDate.setText(deepestEarthquake.getDate());
        deepestEqDepth.setText(deepestEarthquake.getDepthString());
    }

    // Saves the start and end dates so they can be reused when the user rotates the device
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("startDate", startDate);
        savedInstanceState.putString("endDate", endDate);

        super.onSaveInstanceState(savedInstanceState);
    }
}
