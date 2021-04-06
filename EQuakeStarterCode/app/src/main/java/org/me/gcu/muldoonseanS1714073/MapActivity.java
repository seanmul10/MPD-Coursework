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

    private String[] markerIDs;

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
    public void onAsyncTaskComplete() {

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);

        List<Earthquake> earthquakeList = EarthquakeData.getEarthquakes();

        markerIDs = new String[earthquakeList.size()];
        // Add a new marker with the correct colour
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