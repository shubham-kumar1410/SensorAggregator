package com.bitsg.sensoragg;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitsg.sensoragg.ItemFormats.Sensor;

import java.util.Vector;

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
        holder.data.setText(sensors.get(position).getData());
        if (sensors.get(position).getStatus() == 1) {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.green500));
        } else {

            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.red500));
        }
    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sensorname, data, graph;
        Button edit, graph_button;
        LinearLayout layout;
        public ViewHolder(final View itemView) {
            super(itemView);
            sensorname = itemView.findViewById(R.id.sensor_title);
            data = itemView.findViewById(R.id.sensor_data);
            edit = itemView.findViewById(R.id.sensor_edit);
            graph_button = itemView.findViewById(R.id.sensor_graph_button);
            layout = itemView.findViewById(R.id.sensor_background);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditSensorDetails.class);
                    intent.putExtra("id", getAdapterPosition());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Log.v("tag", String.valueOf(sensors.get(getAdapterPosition()).getStatus()));
                }
            });

            graph_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, GraphActivity.class);
                    intent.putExtra("id", getAdapterPosition());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("key", sensors.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                    Log.v("tag", String.valueOf(sensors.get(getAdapterPosition()).getStatus()));
                }
            });
        }
    }

}
