package com.bitsg.sensoraggregator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.Vector;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by shubhamk on 20/1/18.
 */

public class SensorDataAdapter extends RecyclerView.Adapter<SensorDataAdapter.ViewHolder> {
    Context context;
    Vector<Sensor> sensors;

    public SensorDataAdapter(Context context, Vector<Sensor> sensors) {
        this.context = context;
        this.sensors = sensors;
    }

    @Override
    public SensorDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.sensor_item_format, parent, false);
        return new SensorDataAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(SensorDataAdapter.ViewHolder holder, int position) {
        holder.sensorname.setText(sensors.get(position).getSensor_name());
        holder.data.setText(Double.toString(sensors.get(position).getData()));
    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sensorname, data, graph;

        public ViewHolder(final View itemView) {
            super(itemView);
            sensorname = itemView.findViewById(R.id.sensor_title);
            data = itemView.findViewById(R.id.sensor_data);
            graph = itemView.findViewById(R.id.sensor_graph);
            graph.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = (LayoutInflater)
                            context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.graph_layout, null);

                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    // show the popup window
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    GraphView graph = popupView.findViewById(R.id.graph);
                    BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                            new DataPoint(0, -1),
                            new DataPoint(1, 5),
                            new DataPoint(2, 3),
                            new DataPoint(3, 2),
                            new DataPoint(4, 6)
                    });
                    graph.addSeries(series);
                    series.setSpacing(50);

                    series.setDrawValuesOnTop(true);
                    graph.getViewport().setScrollable(true); // enables horizontal scrolling
                    graph.getViewport().setScrollableY(true); // enables vertical scrolling
                    graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                    graph.getViewport().setScalableY(true);
                }
            });
        }
    }

}
