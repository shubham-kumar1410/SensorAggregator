package com.bitsg.sensoragg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bitsg.sensoragg.ItemFormats.Sensor;
import com.bitsg.sensoragg.ItemFormats.SensorDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.Vector;

public class SensorDataActive extends AppCompatActivity {

    public static String stationid;
    String title;
    int index;
    SensorDataAdapter adapter;
    String json;
    JSONArray m_jArry;
    TextView station_name, station_lat, station_lon, text, text_cpy;
    EditText name_edit, lat_edit, lon_edit;
    FloatingActionButton edit, done;
    CardView edit_card, normal_card;
    ProgressDialog pd;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stations");
    DatabaseReference dbref;
    Thread t = new Thread();
    private Vector<Sensor> sensors = new Vector<>();
    private Vector<SensorDetails> sensorDetailsVector = new Vector<>();
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


        index = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(title);
        stationid = String.valueOf(index);
        // Log.v("tag",stationid);
        dbref = FirebaseDatabase.getInstance().getReference().child("Sensors").child(stationid);
        recyclerView = findViewById(R.id.sensor_data_rv);
        rvcopy = findViewById(R.id.sensor_data_rv_copy);

        text = findViewById(R.id.sensor_txt);
        text_cpy = findViewById(R.id.sensor_txt_copy);
        text_cpy.setVisibility(View.INVISIBLE);

        station_name = findViewById(R.id.station_name);
        station_lat = findViewById(R.id.station_lat);
        station_lon = findViewById(R.id.station_lon);

        name_edit = findViewById(R.id.station_name_edit);
        lat_edit = findViewById(R.id.station_lat_edit);
        lon_edit = findViewById(R.id.station_lon_edit);

        normal_card = findViewById(R.id.station_card);
        edit_card = findViewById(R.id.station_card_edit);
        edit_card.setVisibility(View.INVISIBLE);
        rvcopy.setVisibility(View.INVISIBLE);

        edit = findViewById(R.id.edit_station);
        done = findViewById(R.id.save_station);

        recyclerView.setHasFixedSize(true);
        rvcopy.setHasFixedSize(true);


        t = new Thread() {

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
                                    sensors.clear();
                                    dbref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            sensorDetailsVector.clear();
                                            for (DataSnapshot shot : dataSnapshot.getChildren()) {
                                                sensorDetailsVector.add(shot.getValue(SensorDetails.class));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                            Log.e("TAG", databaseError.getDetails());
                                        }
                                    });
                                    // Log.v("tag",String.valueOf(sensorDetailsVector.size()));
                                    for (int j = 0; j < sensorDetailsVector.size(); j++) {
                                        SensorDetails sensorDetails = sensorDetailsVector.get(j);
                                        if (sensorDetails.getName().equals("") || sensorDetails.getRegion().equals("") || sensorDetails.getType().equals("")) {
                                            Intent intent = new Intent(getApplicationContext(), EditSensorDetails.class);
                                            intent.putExtra("id", j);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            getApplication().startActivity(intent);
                                            finish();
                                            interrupt();
                                        } else {
                                            Sensor sensor = new Sensor();

                                            sensor.setData(sensorDetailsVector.get(j).getData());
                                            sensor.setSensor_name(sensorDetailsVector.get(j).getName());
                                            sensor.setId(sensorDetailsVector.get(j).getKey());
                                            double data = Double.valueOf(sensorDetailsVector.get(j).getData());
                                            if (data > 0.5) {
                                                sensor.setStatus(1);
                                            } else {
                                                sensor.setStatus(0);
                                            }
                                            sensors.add(sensor);
                                        }

                                    }
                                    adapter = new SensorDataAdapter(getApplicationContext(), sensors);
                                    recyclerView.setAdapter(adapter);
                                    rvcopy.setAdapter(adapter);
                                    GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                                    GridLayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), 1);
                                    recyclerView.setLayoutManager(layoutManager);
                                    rvcopy.setLayoutManager(layoutManager1);

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
                        if (!isNetworkAvailable(getApplicationContext())) {

                            Snackbar snack = Snackbar.make(done, "No Internet.", Snackbar.LENGTH_LONG);
                            TextView snackBarText = snack.getView().findViewById(android.support.design.R.id.snackbar_text);
                            snackBarText.setTextColor(Color.WHITE);
                            snack.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                            snack.show();

                        } else {
                            String temp_name = name_edit.getText().toString();
                            String temp_lat = lat_edit.getText().toString();
                            String temp_lon = lon_edit.getText().toString();

                            SplashScreen.stations.get(index).setName(temp_name);
                            SplashScreen.stations.get(index).setLatitude(Double.valueOf(temp_lat));
                            SplashScreen.stations.get(index).setLongitude(Double.valueOf(temp_lon));
                            String key = SplashScreen.stations.get(index).getKey();
                            DatabaseReference ref = databaseReference.child(key);

                            ref.child("name").setValue(temp_name);
                            ref.child("longitude").setValue(Double.valueOf(temp_lon));
                            ref.child("latitude").setValue(Double.valueOf(temp_lat));


                            update();

                            edit_card.setVisibility(View.INVISIBLE);
                            normal_card.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            rvcopy.setVisibility(View.INVISIBLE);
                            text.setVisibility(View.VISIBLE);
                            text_cpy.setVisibility(View.INVISIBLE);
                        }

                    }
                });

            } catch (Exception e) {

            }

        }


    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    void update() {
        station_name.setText(SplashScreen.stations.get(index).getName());
        station_lat.setText(String.valueOf(SplashScreen.stations.get(index).getLatitude()));
        station_lon.setText(String.valueOf(SplashScreen.stations.get(index).getLongitude()));

        name_edit.setText(SplashScreen.stations.get(index).getName());
        lat_edit.setText(String.valueOf(SplashScreen.stations.get(index).getLatitude()));
        lon_edit.setText(String.valueOf(SplashScreen.stations.get(index).getLongitude()));
    }

    @Override
    public void onPause() {
        super.onPause();
        t.interrupt();
    }

    @Override
    public void onStop() {
        super.onStop();
        t.interrupt();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

}

