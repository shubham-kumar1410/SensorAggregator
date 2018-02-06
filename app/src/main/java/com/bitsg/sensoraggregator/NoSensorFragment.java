package com.bitsg.sensoraggregator;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NoSensorFragment extends Fragment {
    final String url = "http://122.166.217.37:1880/find/bits-wastewatermanagement/sensor%20data/";
    ImageView diagram;
    LinearLayout toilet, septic, wet;
    TabLayout tabLayout;
    public NoSensorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_sensor, container, false);
        diagram = view.findViewById(R.id.fragment_1_diagram);
        toilet = view.findViewById(R.id.fragment_1_toilet_click);
        septic = view.findViewById(R.id.fragment_1_septic_click);
        wet = view.findViewById(R.id.fragment_1_wet_click);
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
        return view;
    }


}
