package com.bitsg.sensoraggregator.ItemFormats;

/**
 * Created by shubhamk on 27/4/18.
 */

public class LogData {
    String data;
    String sensorID;
    boolean status;

    public LogData(String data, String sensorID, boolean status) {
        this.data = data;
        this.sensorID = sensorID;
        this.status = status;
    }

    public LogData() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
