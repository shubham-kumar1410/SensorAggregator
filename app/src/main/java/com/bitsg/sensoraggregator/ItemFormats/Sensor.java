package com.bitsg.sensoraggregator.ItemFormats;

/**
 * Created by shubhamk on 18/1/18.
 */

public class Sensor {
    String data;
    String sensor_name;
    int status;
    String id;

    public Sensor(String data, String sensor_name, int status, String id) {
        this.data = data;
        this.sensor_name = sensor_name;
        this.status = status;
        this.id = id;
    }

    public Sensor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

