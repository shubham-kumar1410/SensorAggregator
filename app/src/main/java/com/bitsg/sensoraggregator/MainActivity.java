package com.bitsg.sensoraggregator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    GoogleMap mMap;
    TextView active, inactive;
    LatLng bits, station_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        active = findViewById(R.id.active_text);
        inactive = findViewById(R.id.inactive_text);
        active.setText("Active Stations : 1");
        inactive.setText("Inactive Stations : 1");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        // Add a marker in Sydney and move the camera
        station_1 = new LatLng(23.831713, 77.913052);
        bits = new LatLng(15.391377, 73.879138);
        mMap.animateCamera(CameraUpdateFactory.zoomBy(1));

        mMap.addMarker(new MarkerOptions().position(station_1).title("Station 2"));
        mMap.addMarker(new MarkerOptions().position(bits).title("Bits Goa").icon(BitmapDescriptorFactory.defaultMarker(150)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bits));
        mMap.setOnMarkerClickListener(MainActivity.this);
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(bits));
            }
        });
        inactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(station_1));
            }
        });

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.getTitle().equals("Bits Goa")) {
            Intent intent = new Intent(this, LocationInfo.class);
            intent.putExtra("location", marker.getTitle());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Station is not Active", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
