package com.bitsg.sensoraggregator;

/**
 * Created by shubhamk on 18/1/18.
 */

public class Sensor {
    String data;
    String sensor_name;

    public Sensor(String data, String sensor_name) {
        this.data = data;
        this.sensor_name = sensor_name;
    }

    public Sensor() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSensor_name() {
        return sensor_name;
    }

    public void setSensor_name(String sensor_name) {
        this.sensor_name = sensor_name;
    }
}

