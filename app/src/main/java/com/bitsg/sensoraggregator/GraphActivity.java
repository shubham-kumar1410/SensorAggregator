package com.bitsg.sensoraggregator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
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

public class GraphActivity extends AppCompatActivity {
    int index;
    GraphView graph;
    String json;
    JSONArray m_jArry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
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
        graph = findViewById(R.id.sensor_graph_activity);
        index = getIntent().getIntExtra("id", -1);

        if (index >= 0) {
            getSupportActionBar().setTitle(SplashScreen.sensorDetails.get(index).getName());
        }

        SharedPreferences sp = getApplicationContext().getSharedPreferences("index_value", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
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


                                graph.removeAllSeries();

                                BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
                                if (i > 100) {
                                    for (int k = i - 100; k < i; k++) {
                                        try {
                                            JSONObject jo_inside = m_jArry.getJSONObject(k);
                                            String data = jo_inside.getString("sensor" + String.valueOf(index + 1));

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
                                            String data = jo_inside.getString("sensor" + String.valueOf(index + 1));

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
                                graph.setTitle(SplashScreen.sensorDetails.get(index).getName());
                                graph.getViewport().setScrollable(true); // enables horizontal scrolling
                                graph.getViewport().setScrollableY(true); // enables vertical scrolling
                                graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                                graph.getViewport().setScalableY(true);
                                graph.getViewport().setMinX(0);
                                graph.getViewport().setMaxX(100);
                                graph.setTitleTextSize(50);

                                graph.getViewport().setXAxisBoundsManual(true);
                                series.setOnDataPointTapListener(new OnDataPointTapListener() {
                                    @Override
                                    public void onTap(Series series, DataPointInterface dataPoint) {
                                        Toast.makeText(getApplicationContext(), "Data: " + dataPoint, Toast.LENGTH_SHORT).show();
                                    }
                                });

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

}
