package com.example.coni;

public class LocationArray {

    private String id;
    private String date;
    private Double latitude;
    private Double longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocationArray(String id, String date, Double latitude, Double longitude) {
        this.id = id;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
