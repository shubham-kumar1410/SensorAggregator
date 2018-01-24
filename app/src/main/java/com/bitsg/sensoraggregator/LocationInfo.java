package com.bitsg.sensoraggregator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class LocationInfo extends AppCompatActivity {
    String location;
    SensorDataAdapter adapter;
    String json;
    JSONArray m_jArry;
    private Vector<Sensor> sensors = new Vector<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.content_sensor_rv);
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
        location = getIntent().getStringExtra("location");
        if (location != null) {
            getSupportActionBar().setTitle(location + " | Sensor List");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
            getWindow().setStatusBarColor(colorPrimary);
            getWindow().setNavigationBarColor(colorPrimary);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        recyclerView.setHasFixedSize(true);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("index_value", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //  editor.putInt("index", 0);
        // editor.apply();
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
            m_jArry = obj.getJSONArray("sensor");
            //   Toast.makeText(LocationInfo.this,String.valueOf(m_jArry.length()),Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences sp = getSharedPreferences("index_value", Activity.MODE_PRIVATE);
                                int i = sp.getInt("index", -1);
                                updateData(i);
                                adapter = new SensorDataAdapter(LocationInfo.this, sensors);
                                recyclerView.setAdapter(adapter);
                                GridLayoutManager layoutManager = new GridLayoutManager(LocationInfo.this, 2);
                                recyclerView.setLayoutManager(layoutManager);
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
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    private void updateData(int i) {
        sensors.clear();

        try {
            JSONObject jo_inside = m_jArry.getJSONObject(i);
            Sensor sensor = new Sensor();

            sensor.setData(jo_inside.getString("S1"));
            sensor.setSensor_name("Anode pH");
            sensors.add(sensor);

            Sensor sensor1 = new Sensor();

            sensor1.setData(jo_inside.getString("S2"));
            sensor1.setSensor_name("Cathode pH");
            sensors.add(sensor1);

            Sensor sensor2 = new Sensor();
            sensor2.setData(jo_inside.getString("S3"));
            sensor2.setSensor_name("Current Reactor");
            sensors.add(sensor2);

            Sensor sensor3 = new Sensor();
            sensor3.setData(jo_inside.getString("S4"));
            sensor3.setSensor_name("Voltage Reactor");
            sensors.add(sensor3);

            Sensor sensor4 = new Sensor();
            sensor4.setData(jo_inside.getString("S5"));
            sensor4.setSensor_name("Temp Anode");
            sensors.add(sensor4);

            Sensor sensor5 = new Sensor();
            sensor5.setData(jo_inside.getString("S6"));
            sensor5.setSensor_name("Temp Cathode");
            sensors.add(sensor5);

//                Sensor sensor6 = new Sensor();
//                sensor6.setData(jo_inside.getString("S7"));
//                sensor6.setSensor_name("Motor Voltage");
//                sensors.add(sensor6);
        } catch (Exception e) {


        }
    }
}
