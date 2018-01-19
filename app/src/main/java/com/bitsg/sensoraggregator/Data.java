package com.bitsg.sensoraggregator;

import java.util.ArrayList;

/**
 * Created by shubhamk on 20/1/18.
 */

public class Data {
    double time;
    ArrayList<Sensor> sensors;

    public Data(double time, ArrayList<Sensor> sensors) {
        this.time = time;
        this.sensors = sensors;
    }

    public Data() {
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public ArrayList<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(ArrayList<Sensor> sensors) {
        this.sensors = sensors;
    }
}
