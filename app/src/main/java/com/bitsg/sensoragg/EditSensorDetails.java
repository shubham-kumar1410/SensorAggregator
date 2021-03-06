package com.bitsg.sensoragg;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditSensorDetails extends AppCompatActivity {
    android.support.design.widget.TextInputEditText sensorname;
    android.support.design.widget.TextInputEditText sensortype;
    android.support.design.widget.TextInputEditText sensorregion;
    android.support.design.widget.TextInputEditText sensorub;
    android.support.design.widget.TextInputEditText sensorlb;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Sensors").child(SensorDataActive.stationid);
    Button done;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sensor_details);
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
        getSupportActionBar().setTitle("Edit Sensor Config");
        sensorname = findViewById(R.id.sensor_name_editText);
        sensorregion = findViewById(R.id.sensor_region_editText);
        sensortype = findViewById(R.id.sensor_type_editText);
        sensorlb = findViewById(R.id.sensor_lb_editText);
        sensorub = findViewById(R.id.sensor_ub_editText);
        done = findViewById(R.id.sensor_edit_done);


        index = getIntent().getIntExtra("id", -1);
        if (index >= 0) {
            sensorname.setText(MainActivity.sensorDetails.get(index).getName());
            sensorregion.setText(MainActivity.sensorDetails.get(index).getRegion());
            sensorub.setText(String.valueOf(MainActivity.sensorDetails.get(index).getUb()));
            sensorlb.setText(String.valueOf(MainActivity.sensorDetails.get(index).getLb()));
            sensortype.setText(MainActivity.sensorDetails.get(index).getType());
        }

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
                    String name = sensorname.getText().toString();
                    String region = sensorregion.getText().toString();
                    String ub = sensorub.getText().toString();
                    String lb = sensorlb.getText().toString();
                    String type = sensortype.getText().toString();
                    if (index >= 0) {
                        MainActivity.sensorDetails.get(index).setName(name);
                        MainActivity.sensorDetails.get(index).setRegion(region);
                        MainActivity.sensorDetails.get(index).setType(type);
                        MainActivity.sensorDetails.get(index).setLb(Double.valueOf(lb));
                        MainActivity.sensorDetails.get(index).setUb(Double.valueOf(ub));

                        String key = MainActivity.sensorDetails.get(index).getKey();
                        DatabaseReference ref = databaseReference.child(key);

                        ref.child("name").setValue(name);
                        ref.child("lb").setValue(Double.valueOf(lb));
                        ref.child("ub").setValue(Double.valueOf(ub));
                        ref.child("type").setValue(type);
                        ref.child("region").setValue(region);


                        finish();
                    }


                }

            }
        });

    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
