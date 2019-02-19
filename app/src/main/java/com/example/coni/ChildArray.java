package com.example.coni;

public class ChildArray {

    private int id;
    private byte[] child_photo;
    private String child_lname;
    private String child_fname;
    private String child_midname;
    private String child_age;
    private String child_birthday;
    private String child_gender;


    public ChildArray(int id, byte[] child_photo, String child_lname, String child_fname, String child_midname, String child_age, String child_birthday, String child_gender) {
        this.id = id;
        this.child_photo = child_photo;
        this.child_lname = child_lname;
        this.child_fname = child_fname;
        this.child_midname = child_midname;
        this.child_age = child_age;
        this.child_birthday = child_birthday;
        this.child_gender = child_gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChild_lname() {
        return child_lname;
    }

    public void setChild_lname(String child_lname) {
        this.child_lname = child_lname;
    }

    public String getChild_fname() {
        return child_fname;
    }

    public void setChild_fname(String child_fname) {
        this.child_fname = child_fname;
    }

    public String getChild_midname() {
        return child_midname;
    }

    public void setChild_midname(String child_midname) {
        this.child_midname = child_midname;
    }

    public String getChild_age() {
        return child_age;
    }

    public void setChild_age(String child_age) {
        this.child_age = child_age;
    }

    public String getChild_birthday() {
        return child_birthday;
    }

    public void setChild_birthday(String child_birthday) {
        this.child_birthday = child_birthday;
    }

    public String getChild_gender() {
        return child_gender;
    }

    public void setChild_gender(String child_gender) {
        this.child_gender = child_gender;
    }

    public byte[] getChild_photo() {
        return child_photo;
    }

    public void setChild_photo(byte[] child_photo) {
        this.child_photo = child_photo;
    }
}
