package com.example.coni;

public class LocationArray {

    private String id;
    private String date;
    private String recipient;
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

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
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

    public LocationArray(String id, String date, String recipient, Double latitude, Double longitude) {
        this.id = id;
        this.date = date;
        this.recipient = recipient;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
