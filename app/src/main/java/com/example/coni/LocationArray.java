package com.example.coni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationArray {


    public LocationArray() {
        //Default Constructor

    }

    public double latitudedb;
    public double longitudedb;



    public double getLatitudedb() {
        return latitudedb;
    }

    public double getLongitudedb() {
        return longitudedb;
    }



    public LocationArray(double latitudedb, double longitudedb) {
        this.latitudedb = latitudedb;
        this.longitudedb = longitudedb;
    }


}
