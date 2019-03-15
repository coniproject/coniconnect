package com.example.coni;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;

import java.sql.Date;
import java.util.Calendar;

public class DBHelper extends SQLiteOpenHelper {

    //Database Name and Table Name

    public static final String dbname = "coniproject.db";
    public static final String guardianDetailModel = "guardianDetails";
    public static final String childDetailModel = "childDetails";
    public static final String deviceDetailModel = "deviceDetails";
    public static final String locationDetailModel = "locationDetails";

    //Table Guardian Columns

    public static final String G_ID_COL1 = "id";
    public static final String G_LASTNAME_COL2 = "lastname";
    public static final String G_FIRSTNAME_COL3 = "firstname";
    public static final String G_MIDNAME_COL4 = "middlename";
    public static final String G_AGE_COL5 = "age";
    public static final String G_BIRTHDAY_COL6 = "birthday";
    public static final String G_GENDER_COL7 = "gender";
    public static final String G_CONTACTNO_COL8 = "contactno";
    public static final String G_EMAIL_COL9 = "email";
    public static final String G_UNAME_COL10 = "username";
    public static final String G_PASSWORD_COL10 = "password";

    //Table Child Columns

    public static final String C_ID_COL1 = "id";
    public static final String C_PHOTO_COL2 = "childphoto";
    public static final String C_LASTNAME_COL3 = "childlastname";
    public static final String C_FIRSTNAME_COL4 = "childfirstname";
    public static final String C_MIDDLENAME_COL5 = "childmidname";
    public static final String C_AGE_COL6 = "childage";
    public static final String C_BIRTHDAY_COL7 = "childbday";
    public static final String C_GENDER_COL8 = "childgender";

    //Table Device Columns

    public static final String D_IDNO_COL1 = "id";
    public static final String D_DEVICENO_COL2 = "deviceno";
    public static final String D_DEVSTATUS_COL3 = "devicestatus";

    //Table Location Details

    public static final String L_ID_COL1 = "id";
    public static final String L_PHONE = "phonenumber";
    public static final String L_DATE = "date";
    public static final String L_BATT_STATUS = "battstatus";
    public static final String L_LAST_LAT = "latitude";
    public static final String L_CURR_LONG = "longitude";

    //Database

    private SQLiteDatabase mWriteableDb;

    // SMS

    public static final String SMS_URI = "/data/data/org.secure.sms/databases/";
    public static final int version =1;


    public DBHelper(Context context) {
        //create database
        super(context, dbname, null, 4);
        mWriteableDb = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+guardianDetailModel+" ( " +
                ""+G_ID_COL1+" integer primary key autoincrement, "
                +G_LASTNAME_COL2+" text not null, "
                +G_FIRSTNAME_COL3+" text not null, "
                +G_MIDNAME_COL4+" text, "
                +G_AGE_COL5+" integer not null, "
                +G_BIRTHDAY_COL6+" date not null, "
                +G_GENDER_COL7+" text not null, "
                +G_CONTACTNO_COL8+" integer unique not null, "
                +G_EMAIL_COL9+" text unique, "
                +G_UNAME_COL10+" text unique, "
                +G_PASSWORD_COL10+" text not null);");

        Log.e("Table Operations :", "Guardian Detail Created");

        db.execSQL("CREATE TABLE "+locationDetailModel+" ( " +
                ""+L_ID_COL1+" integer primary key autoincrement, "
                +L_DATE+" date, "
                +L_PHONE+" text, "
                +L_LAST_LAT+" text, "
                +L_CURR_LONG+" text);");

        Log.e("Table Operations :", "Location Detail Created");

        db.execSQL("CREATE TABLE "+childDetailModel+" ( " +
                ""+C_ID_COL1+" integer primary key autoincrement, "
                +C_PHOTO_COL2+"  not null, "
                +C_LASTNAME_COL3+" text not null, "
                +C_FIRSTNAME_COL4+" text not null, "
                +C_MIDDLENAME_COL5+" text, "
                +C_AGE_COL6+" text not null, "
                +C_BIRTHDAY_COL7+" text not null, "
                +C_GENDER_COL8+" text not null);");

        Log.e("Table Operations :", "Child Detail Created");

        db.execSQL("CREATE TABLE "+deviceDetailModel+" ( " +
                ""+D_IDNO_COL1+" integer primary key autoincrement, "
                +D_DEVICENO_COL2+" text unique, "
                +D_DEVSTATUS_COL3+" text );");

        Log.e("Table Operations :", "Device Detail Created");


        // inserts a default user into the db //
        db.execSQL("insert into guardianDetails (lastname, firstname, middlename, age, birthday,gender, contactno, email, username, password) values " +
                "('dela cruz', 'juan', 'jose', 33 , '1972-12-08', 'male', 09123456789 , 'juandelacruz@gmail.com', 'juandcruz' , 'testing1')");

        db.execSQL("insert into deviceDetails(deviceno, devicestatus) values" +
                "('A12345B','released')," +
                "('A23456B','released')," +
                "('A34567B','manufacturer')," +
                "('A45678B','blocked')," +
                "('A56789B','returned')");





    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Removes Existing Table

        db.execSQL("drop table if exists "+guardianDetailModel);
        db.execSQL("drop table if exists " + childDetailModel);
        db.execSQL("drop table if exists " + deviceDetailModel);
        db.execSQL("drop table if exists " + locationDetailModel);
        onCreate(db);

        Log.e("Table Operations :", "Dropped Existing Tables");
    }

    //Insert --> Guardian Registration

    public boolean addguardian(String lastname, String firstname, String midname, String age, String bday, String gender, String contactno, String email, String uname, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(G_LASTNAME_COL2, lastname);
        contentValues.put(G_FIRSTNAME_COL3, firstname);
        contentValues.put(G_MIDNAME_COL4, midname);
        contentValues.put(G_AGE_COL5, age);
        contentValues.put(G_BIRTHDAY_COL6, bday);
        contentValues.put(G_GENDER_COL7, gender);
        contentValues.put(G_CONTACTNO_COL8, contactno);
        contentValues.put(G_EMAIL_COL9, email);
        contentValues.put(G_UNAME_COL10, uname);
        contentValues.put(G_PASSWORD_COL10, pass);
        Log.e("Table Operations :", "Inserted One User on Guardian");
        long result = db.insert(guardianDetailModel, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;


    }


    //Insert --> Child Registration

    public boolean addchild(byte[] photo, String clname, String cfname, String cmname, String cage, String cbday, String cgender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C_PHOTO_COL2, photo);
        contentValues.put(C_LASTNAME_COL3, clname);
        contentValues.put(C_FIRSTNAME_COL4, cfname);
        contentValues.put(C_MIDDLENAME_COL5, cmname);
        contentValues.put(C_AGE_COL6, cage);
        contentValues.put(C_BIRTHDAY_COL7, cbday);
        contentValues.put(C_GENDER_COL8, cgender);
        Log.e("Table Operations : ", "Inserted One Child-Device User");
        long result = db.insert(childDetailModel, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updateLocation(String date, String recipient, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(L_DATE,date);
        contentValues.put(L_PHONE,recipient);
        contentValues.put(L_LAST_LAT,latitude);
        contentValues.put(L_CURR_LONG,longitude);
        Log.e("Table Operations : ", "Inserted Location");
        long result = db.insert(locationDetailModel,null,contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }



    //Update

    //Cursors : Responsible for searching data requirements

    public Cursor userlogin(String loginame, String loginpword, SQLiteDatabase db){
        String query = "select * from guardianDetails where username = '"+loginame+"' and password = '"+loginpword+"' ";
        Log.d("query", query);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor usernameValidation(String valusername, SQLiteDatabase db) {
        String query = "select * from guardianDetails where username = '"+valusername+"' ";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor emailValidation(String valemail, SQLiteDatabase db) {
        String query = "select * from guardianDetails where email ='"+valemail+"' ";
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public Cursor deviceValidation(String valdeviceno, SQLiteDatabase db) {
        String query = "select * from deviceDetails  where deviceno ='"+valdeviceno+"' and" +
                " devicestatus = 'released' ";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }




}
