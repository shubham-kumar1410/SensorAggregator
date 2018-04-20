package com.bitsg.sensoraggregator;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditSensorDetails extends AppCompatActivity {
    android.support.design.widget.TextInputEditText sensorname;
    android.support.design.widget.TextInputEditText sensortype;
    android.support.design.widget.TextInputEditText sensorregion;
    android.support.design.widget.TextInputEditText sensorub;
    android.support.design.widget.TextInputEditText sensorlb;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Sensors");
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
            sensorname.setText(SplashScreen.sensorDetails.get(index).getName());
            sensorregion.setText(SplashScreen.sensorDetails.get(index).getRegion());
            sensorub.setText(String.valueOf(SplashScreen.sensorDetails.get(index).getUb()));
            sensorlb.setText(String.valueOf(SplashScreen.sensorDetails.get(index).getLb()));
            sensortype.setText(SplashScreen.sensorDetails.get(index).getType());
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = sensorname.getText().toString();
                String region = sensorregion.getText().toString();
                String ub = sensorub.getText().toString();
                String lb = sensorlb.getText().toString();
                String type = sensortype.getText().toString();

                SplashScreen.sensorDetails.get(index).setName(name);
                SplashScreen.sensorDetails.get(index).setRegion(region);
                SplashScreen.sensorDetails.get(index).setType(type);
                SplashScreen.sensorDetails.get(index).setLb(Double.valueOf(lb));
                SplashScreen.sensorDetails.get(index).setUb(Double.valueOf(ub));

                String key = SplashScreen.sensorDetails.get(index).getKey();
                DatabaseReference ref = databaseReference.child(key);

                ref.child("name").setValue(name);
                ref.child("lb").setValue(Double.valueOf(lb));
                ref.child("ub").setValue(Double.valueOf(ub));
                ref.child("type").setValue(type);
                ref.child("region").setValue(region);

                finish();
            }
        });

    }

}
