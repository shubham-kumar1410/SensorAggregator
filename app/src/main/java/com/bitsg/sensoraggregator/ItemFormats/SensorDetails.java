package com.bitsg.sensoraggregator.ItemFormats;

/**
 * Created by shubhamk on 20/4/18.
 */

public class SensorDetails {
    String name;
    String region;
    double ub;
    double lb;
    String type;
    boolean status;
    String key;

    public SensorDetails() {
    }

    public SensorDetails(String name, String region, double ub, double lb, String type, boolean status, String key) {
        this.name = name;
        this.region = region;
        this.ub = ub;
        this.lb = lb;
        this.type = type;
        this.status = status;
        this.key = key;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getUb() {
        return ub;
    }

    public void setUb(double ub) {
        this.ub = ub;
    }

    public double getLb() {
        return lb;
    }

    public void setLb(double lb) {
        this.lb = lb;
    }

    public String getRegion() {

        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
