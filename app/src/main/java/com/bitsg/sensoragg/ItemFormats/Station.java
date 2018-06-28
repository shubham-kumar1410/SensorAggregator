package com.bitsg.sensoragg.ItemFormats;

/**
 * Created by shubhamk on 18/4/18.
 */

public class Station {
    String domain;
    double longitude;
    double latitude;
    float elevation;
    String name;
    boolean status;
    String key;

    public Station() {
    }

    public Station(String domain, double longitude, double latitude, float elevation, String name, boolean status, String key) {
        this.domain = domain;
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.name = name;
        this.status = status;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
