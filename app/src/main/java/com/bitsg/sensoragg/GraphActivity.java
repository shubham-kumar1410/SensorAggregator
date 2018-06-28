package com.bitsg.sensoragg;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bitsg.sensoragg.ItemFormats.LogData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.json.JSONArray;

import java.util.Vector;

public class GraphActivity extends AppCompatActivity {
    int index;
    GraphView graph;
    String json;
    JSONArray m_jArry;
    String sensor_key;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("log").child(SensorDataActive.stationid);
    Vector<LogData> logData = new Vector<>();
    Vector<LogData> logDatafinal = new Vector<>();
    Vector<String> key = new Vector<>();
    Thread t1 = new Thread();
    Query query = databaseReference.limitToLast(300);
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
        sensor_key = getIntent().getStringExtra("key");
        if (index >= 0) {
            getSupportActionBar().setTitle(MainActivity.sensorDetails.get(index).getName());
        }

        t1 = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                graph.removeAllSeries();

                                BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        logData.clear();
                                        key.clear();
                                        for (DataSnapshot shot : dataSnapshot.getChildren()) {
                                            logData.add(shot.getValue(LogData.class));
                                            key.add(shot.getKey());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                        Log.e("TAG", databaseError.getDetails());
                                    }
                                });
                                logDatafinal.clear();
                                for (int k = 0; k < logData.size(); k++) {
                                    if (logData.get(k).getSensorID().equals(sensor_key)) {
                                        logDatafinal.add(logData.get(k));
                                    }
                                }
                                int log_size = logDatafinal.size();
                                if (log_size > 100) {
                                    int k = 0;
                                    for (int u = log_size - 100; u < log_size; u++) {

                                        DataPoint dataPoint = new DataPoint(k, Float.parseFloat(logDatafinal.get(u).getData()));
                                        series.appendData(dataPoint, true, 100);
                                        k++;
                                    }
                                } else {
                                    for (int u = 0; u < log_size; u++) {
                                        DataPoint dataPoint = new DataPoint(u, Float.parseFloat(logDatafinal.get(u).getData()));
                                        series.appendData(dataPoint, true, 100);
                                    }
                                }
                                graph.addSeries(series);
                                series.setSpacing(5);

                                series.setDrawValuesOnTop(true);
                                Log.v("in", String.valueOf(MainActivity.sensorDetails.size()));
                                graph.setTitle(MainActivity.sensorDetails.get(index).getName());
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

                            }
                        });
                    }

                } catch (InterruptedException e) {
                }
            }
        };

        t1.start();


    }

    @Override
    public void onPause() {
        super.onPause();
        t1.interrupt();
    }

    @Override
    public void onStop() {
        super.onStop();
        t1.interrupt();
    }

}
