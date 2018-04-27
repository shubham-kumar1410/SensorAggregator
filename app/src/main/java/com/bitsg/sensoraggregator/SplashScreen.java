package com.bitsg.sensoraggregator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitsg.sensoraggregator.ItemFormats.SensorDetails;
import com.bitsg.sensoraggregator.ItemFormats.Station;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class SplashScreen extends AppCompatActivity {

    public static Vector<SensorDetails> sensorDetails = new Vector<>();
    public static Vector<Station> stations = new Vector<>();
    ImageView splashimage;
    Button enter;
    ProgressDialog pd;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stations");
    DatabaseReference sensor = FirebaseDatabase.getInstance().getReference().child("Sensors");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashimage = findViewById(R.id.splashimage);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);

        }

        enter = findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable(getApplicationContext())) {

                    Snackbar snack = Snackbar.make(enter, "No Internet.", Snackbar.LENGTH_LONG);
                    TextView snackBarText = snack.getView().findViewById(android.support.design.R.id.snackbar_text);
                    snackBarText.setTextColor(Color.WHITE);
                    snack.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    snack.show();

                } else {
                    pd = new ProgressDialog(SplashScreen.this);
                    pd.setMessage("Loading...");
                    pd.show();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            stations.clear();
                            for (DataSnapshot shot : dataSnapshot.getChildren()) {
                                stations.add(shot.getValue(Station.class));
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            Log.e("TAG", databaseError.getDetails());
                        }
                    });

                    sensor.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            sensorDetails.clear();
                            for (DataSnapshot shot : dataSnapshot.getChildren()) {
                                sensorDetails.add(shot.getValue(SensorDetails.class));
                            }
                            pd.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            Log.e("TAG", databaseError.getDetails());
                        }
                    });
                    pd.dismiss();
                    if (sensorDetails.size() > 0 && stations.size() > 0) {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    } else {
                        Snackbar snack = Snackbar.make(enter, "Unable to fetch data. Try again.", Snackbar.LENGTH_LONG);
                        TextView snackBarText = snack.getView().findViewById(android.support.design.R.id.snackbar_text);
                        snackBarText.setTextColor(Color.WHITE);
                        snack.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                        snack.show();
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
