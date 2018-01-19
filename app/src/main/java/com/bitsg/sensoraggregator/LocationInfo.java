package com.bitsg.sensoraggregator;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class LocationInfo extends AppCompatActivity {
    String location;
    SensorDataAdapter adapter;
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

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateData();
                                adapter = new SensorDataAdapter(LocationInfo.this, sensors);
                                recyclerView.setAdapter(adapter);
                                GridLayoutManager layoutManager = new GridLayoutManager(LocationInfo.this, 2);
                                recyclerView.setLayoutManager(layoutManager);

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    private void updateData() {
        sensors.clear();
        Sensor sensor = new Sensor();
        double random = ThreadLocalRandom.current().nextDouble(3.04, 3.06);
        sensor.setData(random);
        sensor.setSensor_name("Sensor 1");
        sensors.add(sensor);

        Sensor sensor1 = new Sensor();
        double random1 = ThreadLocalRandom.current().nextDouble(6.54, 6.57);
        sensor1.setData(random1);
        sensor1.setSensor_name("Sensor 2");
        sensors.add(sensor1);

        Sensor sensor2 = new Sensor();
        double random2 = ThreadLocalRandom.current().nextDouble(0.77, 0.85);
        sensor2.setData(random2);
        sensor2.setSensor_name("Sensor 3");
        sensors.add(sensor2);

        Sensor sensor3 = new Sensor();
        double random3 = ThreadLocalRandom.current().nextDouble(23.945, 23.96);
        sensor3.setData(random3);
        sensor3.setSensor_name("Sensor 4");
        sensors.add(sensor3);

        Sensor sensor4 = new Sensor();
        double random4 = ThreadLocalRandom.current().nextDouble(45.18, 46.4);
        sensor4.setData(random4);
        sensor4.setSensor_name("Sensor 5");
        sensors.add(sensor4);

        Sensor sensor5 = new Sensor();
        double random5 = ThreadLocalRandom.current().nextDouble(38.09, 38.6);
        sensor5.setData(random5);
        sensor5.setSensor_name("Sensor 6");
        sensors.add(sensor5);
    }

}
