package com.example.coni;

import java.util.HashMap;
import java.util.Map;

public class LocationArray {

    public LocationArray() {
        //Default Constructor

    }

    public String date;
    public String recipient;
    public double latitudedb;
    public double longitudedb;

    public String getDate() {
        return date;
    }

    public String getRecipient() {
        return recipient;
    }

    public double getLatitudedb() {
        return latitudedb;
    }

    public double getLongitudedb() {
        return longitudedb;
    }



    public LocationArray(String date, String recipient, double latitudedb, double longitudedb) {
        this.date = date;
        this.recipient = recipient;
        this.latitudedb = latitudedb;
        this.longitudedb = longitudedb;
    }


}
