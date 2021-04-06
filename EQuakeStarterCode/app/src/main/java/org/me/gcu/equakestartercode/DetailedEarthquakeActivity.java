package org.me.gcu.equakestartercode;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

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
    protected void onStart() {
        super.onStart();
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

        LatLng latLng = new LatLng(earthquake.getLatitude(), earthquake.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f));
        map.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(MagnitudeColourCoding.getMagnitudeHue(earthquake.getMagnitude(), this)))
                .title(earthquake.getLocation())
                .snippet(earthquake.getDate()));
    }
}