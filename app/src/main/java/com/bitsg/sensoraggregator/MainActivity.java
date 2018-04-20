package com.bitsg.sensoraggregator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
    LatLng bits, latLng;
    int inactive_number, active_number;

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

        inactive_number = 0;
        active_number = 0;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);

        bits = new LatLng(15.391377, 73.879138);
        for (int i = 0; i < SplashScreen.stations.size(); i++) {
            boolean check = SplashScreen.stations.get(i).getStatus();
            if (check) {
                active_number++;
                Log.v("tag", "df");
            } else {
                inactive_number++;
            }
        }
        active.setText("Active Stations :" + String.valueOf(active_number));
        inactive.setText("Inactive Stations :" + String.valueOf(inactive_number));

        for (int i = 0; i < SplashScreen.stations.size(); i++) {
            latLng = new LatLng(SplashScreen.stations.get(i).getLatitude(), SplashScreen.stations.get(i).getLongitude());
            String name = SplashScreen.stations.get(i).getName();
            boolean check = SplashScreen.stations.get(i).getStatus();
            if (check) {
                mMap.addMarker(new MarkerOptions().position(latLng).title(name).icon(BitmapDescriptorFactory.defaultMarker(150)));
            } else {
                mMap.addMarker(new MarkerOptions().position(latLng).title(name));
            }
        }

        mMap.animateCamera(CameraUpdateFactory.zoomBy(1));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bits));
        mMap.setOnMarkerClickListener(MainActivity.this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        String title = marker.getTitle();
        for (int i = 0; i < SplashScreen.stations.size(); i++) {

            String name = SplashScreen.stations.get(i).getName();
            boolean check = SplashScreen.stations.get(i).getStatus();
            if (name.equals(title)) {
                if (check) {
                    Intent intent = new Intent(this, SensorDataActive.class);
                    intent.putExtra("name", name);
                    intent.putExtra("id", i);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Station is not Active", Toast.LENGTH_SHORT).show();
                }
            }

        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
