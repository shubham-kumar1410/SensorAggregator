package com.bitsg.sensoraggregator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bitsg.sensoraggregator.ItemFormats.Sensor;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class SensorDataActive extends AppCompatActivity {

    String title;
    int index;
    SensorDataAdapter adapter;
    String json;
    JSONArray m_jArry;
    TextView station_name, station_lat, station_lon, station_url, text, text_cpy;
    EditText name_edit, lat_edit, lon_edit, url_edit;
    FloatingActionButton edit, done;
    CardView edit_card, normal_card;
    ProgressDialog pd;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stations");
    private Vector<Sensor> sensors = new Vector<>();
    private RecyclerView recyclerView, rvcopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data_active);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
            int colorDarkPrimary = ContextCompat.getColor(this, R.color.colorPrimaryDark);
            getWindow().setStatusBarColor(colorDarkPrimary);
            getWindow().setNavigationBarColor(colorPrimary);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            JSONObject obj = new JSONObject(json);
            m_jArry = obj.getJSONArray("data");
        } catch (Exception e) {

        }

        index = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(title);

        recyclerView = findViewById(R.id.sensor_data_rv);
        rvcopy = findViewById(R.id.sensor_data_rv_copy);

        text = findViewById(R.id.sensor_txt);
        text_cpy = findViewById(R.id.sensor_txt_copy);
        text_cpy.setVisibility(View.INVISIBLE);

        station_name = findViewById(R.id.station_name);
        station_lat = findViewById(R.id.station_lat);
        station_lon = findViewById(R.id.station_lon);
        station_url = findViewById(R.id.ip_localhost);

        name_edit = findViewById(R.id.station_name_edit);
        lat_edit = findViewById(R.id.station_lat_edit);
        lon_edit = findViewById(R.id.station_lon_edit);
        url_edit = findViewById(R.id.ip_localhost_edit);

        normal_card = findViewById(R.id.station_card);
        edit_card = findViewById(R.id.station_card_edit);
        edit_card.setVisibility(View.INVISIBLE);
        rvcopy.setVisibility(View.INVISIBLE);

        edit = findViewById(R.id.edit_station);
        done = findViewById(R.id.save_station);

        recyclerView.setHasFixedSize(true);
        rvcopy.setHasFixedSize(true);

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(500);
                        if (getApplicationContext() != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences sp = getSharedPreferences("index_value", Activity.MODE_PRIVATE);
                                    int i = sp.getInt("index", -1);
                                    updateData(i);
                                    adapter = new SensorDataAdapter(getApplicationContext(), sensors);
                                    recyclerView.setAdapter(adapter);
                                    rvcopy.setAdapter(adapter);
                                    GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                                    GridLayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), 1);
                                    recyclerView.setLayoutManager(layoutManager);
                                    rvcopy.setLayoutManager(layoutManager1);
                                    if (i <= 900) {
                                        i++;
                                    } else {
                                        i = 0;
                                    }
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putInt("index", i);
                                    editor.apply();
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
        if (index >= 0) {
            try {
                update();
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        normal_card.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        rvcopy.setVisibility(View.VISIBLE);
                        edit_card.setVisibility(View.VISIBLE);
                        text_cpy.setVisibility(View.VISIBLE);
                        text.setVisibility(View.INVISIBLE);
                    }
                });

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String temp_name = name_edit.getText().toString();
                        String temp_lat = lat_edit.getText().toString();
                        String temp_lon = lon_edit.getText().toString();
                        String temp_url = url_edit.getText().toString();

                        SplashScreen.stations.get(index).setDomain(temp_url);
                        SplashScreen.stations.get(index).setName(temp_name);
                        SplashScreen.stations.get(index).setLatitude(Double.valueOf(temp_lat));
                        SplashScreen.stations.get(index).setLongitude(Double.valueOf(temp_lon));
                        String key = SplashScreen.stations.get(index).getKey();
                        DatabaseReference ref = databaseReference.child(key);

                        ref.child("name").setValue(temp_name);
                        ref.child("longitude").setValue(Double.valueOf(temp_lon));
                        ref.child("latitude").setValue(Double.valueOf(temp_lat));
                        ref.child("domain").setValue(temp_url);


                        update();

                        edit_card.setVisibility(View.INVISIBLE);
                        normal_card.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        rvcopy.setVisibility(View.INVISIBLE);
                        text.setVisibility(View.VISIBLE);
                        text_cpy.setVisibility(View.INVISIBLE);
                    }
                });

            } catch (Exception e) {

            }

        }


    }

    private void updateData(int i) {
        sensors.clear();

        try {
            JSONObject jo_inside = m_jArry.getJSONObject(i);
            Sensor sensor = new Sensor();

            sensor.setData(jo_inside.getString("sensor1"));
            sensor.setSensor_name(SplashScreen.sensorDetails.get(0).getName());
            double data = Double.valueOf(jo_inside.getString("sensor1"));
            if (data > 0.5) {
                sensor.setStatus(1);
            } else {
                sensor.setStatus(0);
            }
            sensors.add(sensor);

            Sensor sensor1 = new Sensor();

            sensor1.setData(jo_inside.getString("sensor2"));
            sensor1.setSensor_name(SplashScreen.sensorDetails.get(1).getName());
            data = Double.valueOf(jo_inside.getString("sensor2"));
            if (data > 0.5) {
                sensor1.setStatus(1);
            } else {
                sensor1.setStatus(0);
            }
            sensors.add(sensor1);


        } catch (Exception e) {


        }
    }

    void update() {
        station_name.setText(SplashScreen.stations.get(index).getName());
        station_lat.setText(String.valueOf(SplashScreen.stations.get(index).getLatitude()));
        station_lon.setText(String.valueOf(SplashScreen.stations.get(index).getLongitude()));
        station_url.setText(SplashScreen.stations.get(index).getDomain());

        name_edit.setText(SplashScreen.stations.get(index).getName());
        lat_edit.setText(String.valueOf(SplashScreen.stations.get(index).getLatitude()));
        lon_edit.setText(String.valueOf(SplashScreen.stations.get(index).getLongitude()));
        url_edit.setText(SplashScreen.stations.get(index).getDomain());
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

}

