package org.me.gcu.equakestartercode;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.menuFragmentPlaceholder, new MenuFragment());
        fragmentTransaction.commit();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        for (int i = 0; i < MainActivity.earthquakes.length; i++) {
            LatLng latLng = new LatLng(MainActivity.earthquakes[i].getLatitude(), MainActivity.earthquakes[i].getLongitude());
            if (i == 0)
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.addMarker(new MarkerOptions().position(latLng).title(MainActivity.earthquakes[i].getLocation()));
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        for (int i = 0; i < MainActivity.earthquakes.length; i++) {
            if (marker.getTitle().equals(MainActivity.earthquakes[i].getLocation())) {
                Intent intent = new Intent(this, DetailedEarthquakeActivity.class);
                intent.putExtra("earthquakeIndex", i);
                if (intent != null)
                    startActivity(intent);
            }
        }
        return false;
    }
}