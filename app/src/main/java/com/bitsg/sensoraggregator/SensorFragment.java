package com.bitsg.sensoraggregator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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


public class SensorFragment extends Fragment {

    SensorDataAdapter adapter;
    String json;
    JSONArray m_jArry;
    GraphView graph;
    int click = -1;
    CardView diagram;
    String[] name = {"Anode pH", "Cathode pH", "Current Reactor", " Voltage Reactor", "Temp Anode", "Temp Cathode"};
    TextView default_txt_graph;
    LinearLayout toilet, septic, wet;
    TabLayout tabLayout;
    private Vector<Sensor> sensors = new Vector<>();
    private RecyclerView recyclerView;

    public SensorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);
        recyclerView = view.findViewById(R.id.fragment_sensor_rv);
        toilet = view.findViewById(R.id.fragment_toilet_click);
        septic = view.findViewById(R.id.fragment_septic_click);
        wet = view.findViewById(R.id.fragment_wet_click);
        tabLayout = getActivity().findViewById(R.id.tabs);

        toilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabLayout != null) {
                    tabLayout.getTabAt(0).select();
                }
            }
        });
        septic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabLayout != null) {
                    tabLayout.getTabAt(1).select();
                }
            }
        });
        wet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabLayout != null) {
                    tabLayout.getTabAt(2).select();
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("index_value", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //  editor.putInt("index", 0);
        // editor.apply();
        try {
            InputStream is = getActivity().getAssets().open("data.json");
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
        } catch (Exception e) {

        }


        graph = view.findViewById(R.id.fragment_sensor_graph);
        default_txt_graph = view.findViewById(R.id.fragment_graph_default_text);
        diagram = view.findViewById(R.id.fragment_diagram_card);
        graph.setVisibility(View.GONE);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new LocationInfo.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                default_txt_graph.setVisibility(View.GONE);
                graph.setVisibility(View.VISIBLE);
                click = position;

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
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences sp = getActivity().getSharedPreferences("index_value", Activity.MODE_PRIVATE);
                                    int i = sp.getInt("index", -1);
                                    updateData(i);
                                    adapter = new SensorDataAdapter(getContext(), sensors);
                                    recyclerView.setAdapter(adapter);
                                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                                    recyclerView.setLayoutManager(layoutManager);
                                    if (click >= 0) {
                                        graph.removeAllSeries();

                                        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
                                        if (i > 100) {
                                            for (int k = i - 100; k < i; k++) {
                                                try {
                                                    JSONObject jo_inside = m_jArry.getJSONObject(k);
                                                    String data = jo_inside.getString("S" + String.valueOf(click + 1));

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
                                                    String data = jo_inside.getString("S" + String.valueOf(click + 1));

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
                                        graph.setTitle(name[click]);
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
                                                Toast.makeText(getActivity().getApplicationContext(), "Data: " + dataPoint, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
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

        return view;
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
