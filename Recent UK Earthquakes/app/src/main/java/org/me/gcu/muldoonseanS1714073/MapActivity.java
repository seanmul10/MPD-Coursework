// Code written by Sean Muldoon
// S1714073
// Mobile Platform Development coursework submission

package org.me.gcu.muldoonseanS1714073;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends EarthquakeActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener
{
    private GoogleMap map;

    private String[] markerIDs; // Array of marker IDs is used to determine the right earthquake data to show when a pin is clicked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onAsyncTaskStarted() {}

    @Override
    public void onAsyncTaskComplete() {}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);

        List<Earthquake> earthquakeList = EarthquakeData.getEarthquakes();

        markerIDs = new String[earthquakeList.size()]; // Initialise the markerIDs array

        // Create a marker at the location with a hue based on the magnitude colour coding
        for (int i = 0; i < earthquakeList.size(); i++) {
            LatLng latLng = new LatLng(earthquakeList.get(i).getLatitude(), earthquakeList.get(i).getLongitude());
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(MagnitudeColourCoding.getMagnitudeHue(earthquakeList.get(i).getMagnitude(), this))));
            markerIDs[i] = marker.getId();
        }

        // Move the camera and zoom it into roughly the centre of the UK
        LatLng centre = new LatLng(Double.parseDouble(getResources().getString(R.string.start_lat)), Double.parseDouble(getResources().getString(R.string.start_lon)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(centre, Float.parseFloat(getResources().getString(R.string.start_zoom))));
    }

    // When a marker is clicked, cycle through the earthquakes until the corresponding ID is found
    @Override
    public boolean onMarkerClick(final Marker marker) {
        for (int i = 0; i < EarthquakeData.getEarthquakes().size(); i++) {
            if (marker.getId().equals(markerIDs[i])) {
                startDetailedViewActivity(i);
            }
        }
        return false;
    }
}