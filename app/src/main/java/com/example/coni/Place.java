package com.example.coni;

public class Place {

    private int id;
    private int photo;
    private String cname;
    private String currloc;


    public Place(int id, int photo, String cname, String currloc) {
        this.id = id;
        this.photo = photo;
        this.cname = cname;
        this.currloc = currloc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCurrloc() {
        return currloc;
    }

    public void setCurrloc(String currloc) {
        this.currloc = currloc;
    }

}
