// Code written by Sean Muldoon
// S1714073
// Mobile Platform Development coursework submission

package org.me.gcu.muldoonseanS1714073;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailedEarthquakeActivity extends EarthquakeActivity implements OnMapReadyCallback
{
    private Earthquake earthquake;

    private TextView locationText;
    private TextView magnitudeText;
    private TextView dateText;
    private TextView timeText;
    private TextView latLongText;
    private TextView depthText;

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detailed);

        // Set up graphical components
        locationText = (TextView)findViewById(R.id.detailedLocation);
        magnitudeText = (TextView)findViewById(R.id.detailedMagnitude);
        dateText = (TextView)findViewById(R.id.detailedDate);
        timeText = (TextView)findViewById(R.id.detailedTime);
        latLongText = (TextView)findViewById(R.id.detailedLatLong);
        depthText = (TextView)findViewById(R.id.detailedDepth);

        // Get earthquake index and set the earthquake to show a detailed display of
        int index = getIntent().getExtras().getInt("earthquakeIndex");
        earthquake = EarthquakeData.getEarthquakes().get(index);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailedMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onAsyncTaskStarted() {
    }

    @Override
    public void onAsyncTaskComplete() {
        locationText.setText(earthquake.getLocation());
        magnitudeText.setText("Magnitude: " + earthquake.getMagnitude());
        dateText.setText(earthquake.getDate());
        timeText.setText(earthquake.getTime());
        latLongText.setText(earthquake.getLatLngString());
        depthText.setText(earthquake.getDepthString());

        magnitudeText.setBackgroundColor(Color.parseColor(MagnitudeColourCoding.getColour(earthquake.getMagnitude(), getApplicationContext())));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Create a marker at the location with a hue based on the magnitude colour coding
        LatLng latLng = new LatLng(earthquake.getLatitude(), earthquake.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f));
        map.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(MagnitudeColourCoding.getMagnitudeHue(earthquake.getMagnitude(), this)))
                .title(earthquake.getLocation())
                .snippet(earthquake.getDate()));
    }
}