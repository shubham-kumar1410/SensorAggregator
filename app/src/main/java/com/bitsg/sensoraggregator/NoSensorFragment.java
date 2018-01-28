package com.bitsg.sensoraggregator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class NoSensorFragment extends Fragment {
    ImageView diagram;

    public NoSensorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_sensor, container, false);
        diagram = view.findViewById(R.id.fragment_1_diagram);

        return view;
    }


}
