package com.bitsg.sensoraggregator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

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
    GraphView graph;
    int click = 0;
    CardView default_card, graph_card, toi_card, tank_card, bed_card;
    String[] name = {"Anode pH", "Cathode pH", "Current Reactor", " Voltage Reactor", "Temp Anode", "Temp Cathode"};
    TextView sensor_default, graph_default;
    TextView default_txt_graph;
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
        default_card = findViewById(R.id.default_card);
        toi_card = findViewById(R.id.toi_card);
        tank_card = findViewById(R.id.tank_card);
        bed_card = findViewById(R.id.bed_card);
        graph = findViewById(R.id.sensor_graph);
        sensor_default = findViewById(R.id.sensor_text_def);
        graph_default = findViewById(R.id.graph_text_def);
        default_txt_graph = findViewById(R.id.graph_default_text);


        recyclerView.setVisibility(View.GONE);
        graph.setVisibility(View.GONE);
        default_txt_graph.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        default_card.setVisibility(View.GONE);
        graph_default.setVisibility(View.VISIBLE);
        sensor_default.setVisibility(View.VISIBLE);
        default_txt_graph.setVisibility(View.GONE);
        toi_card.setCardBackgroundColor(getResources().getColor(R.color.card_select));
        tank_card.setCardBackgroundColor(getResources().getColor(R.color.card_normal));
        bed_card.setCardBackgroundColor(getResources().getColor(R.color.card_normal));


        toi_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                default_card.setVisibility(View.GONE);
                graph_default.setVisibility(View.VISIBLE);
                sensor_default.setVisibility(View.VISIBLE);
                default_txt_graph.setVisibility(View.GONE);
                toi_card.setCardBackgroundColor(getResources().getColor(R.color.card_select));
                tank_card.setCardBackgroundColor(getResources().getColor(R.color.card_normal));
                bed_card.setCardBackgroundColor(getResources().getColor(R.color.card_normal));
            }
        });

        tank_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                default_txt_graph.setVisibility(View.VISIBLE);
                sensor_default.setVisibility(View.GONE);
                default_card.setVisibility(View.VISIBLE);
                graph_default.setVisibility(View.GONE);
                graph.setVisibility(View.GONE);
                toi_card.setCardBackgroundColor(getResources().getColor(R.color.card_normal));
                tank_card.setCardBackgroundColor(getResources().getColor(R.color.card_select));
                bed_card.setCardBackgroundColor(getResources().getColor(R.color.card_normal));
            }
        });

        bed_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                default_card.setVisibility(View.GONE);
                graph_default.setVisibility(View.VISIBLE);
                default_txt_graph.setVisibility(View.GONE);
                sensor_default.setVisibility(View.VISIBLE);
                toi_card.setCardBackgroundColor(getResources().getColor(R.color.card_normal));
                tank_card.setCardBackgroundColor(getResources().getColor(R.color.card_normal));
                bed_card.setCardBackgroundColor(getResources().getColor(R.color.card_select));
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                default_txt_graph.setVisibility(View.GONE);
                graph.setVisibility(View.VISIBLE);
                graph.removeAllSeries();

                BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
                SharedPreferences sp = getSharedPreferences("index_value", Activity.MODE_PRIVATE);
                int i = sp.getInt("index", -1);
                if (i > 100) {
                    for (int k = i - 100; k < i; k++) {
                        try {
                            JSONObject jo_inside = m_jArry.getJSONObject(k);
                            String data = jo_inside.getString("S" + String.valueOf(position + 1));

                            DataPoint dataPoint = new DataPoint(100 - i + k, Float.parseFloat(data));
                            series.appendData(dataPoint, true, 100);
                            //      Log.v("Test","yo");
                        } catch (Exception e) {
                        }
                    }
                } else {
                    for (int k = 0; k < i; k++) {
                        try {
                            JSONObject jo_inside = m_jArry.getJSONObject(k);
                            String data = jo_inside.getString("S" + String.valueOf(position + 1));

                            DataPoint dataPoint = new DataPoint(k, Float.parseFloat(data));
                            series.appendData(dataPoint, true, 100);
                            //      Log.v("Test","yo");
                        } catch (Exception e) {
                        }
                    }
                }

                graph.addSeries(series);
                series.setSpacing(5);

                series.setDrawValuesOnTop(true);
                graph.setTitle(name[position]);
                graph.getViewport().setScrollable(true); // enables horizontal scrolling
                graph.getViewport().setScrollableY(true); // enables vertical scrolling
                graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                graph.getViewport().setScalableY(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(100);
                graph.setTitleTextSize(50);
                //    graph.getViewport().setMinY(-5.0);
                //  graph.getViewport().setMaxY(0.0);

//       graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setXAxisBoundsManual(true);
                series.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(getApplicationContext(), "Data: " + dataPoint, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
