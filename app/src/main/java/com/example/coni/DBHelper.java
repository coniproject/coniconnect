package com.example.coni;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    //Database Name and Table Name

    public static final String dbname = "coniproject.db";
    public static final String guardianDetailModel = "guardianDetails";
    public static final String childDetailModel = "childDetails";
    public static final String deviceDetailModel = "deviceDetails";

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
    public static final String C_LASTNAME_COL2 = "childlastname";
    public static final String C_FIRSTNAME_COL3 = "childfirstname";
    public static final String C_MIDDLENAME_COL4 = "childmidname";
    public static final String C_AGE_COL5 = "childage";
    public static final String C_BIRTHDAY_COL6 = "childbday";
    public static final String C_GENDER_COL7 = "childgender";
    public static final String C_PHOTO_COL8 = "childphonto";

    //Table Device Columns

    public static final String D_IDNO_COL1 = "id";
    public static final String D_DEVICENO_COL2 = "deviceno";
    public static final String D_DEVSTATUS_COL3 = "devicestatus";

    //Database

    private SQLiteDatabase mWriteableDb;

    public DBHelper(Context context) {
        //create database
        super(context, dbname, null, 4);
        mWriteableDb = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+guardianDetailModel+" ( " +
                ""+G_ID_COL1+" integer primary key autoincrement, "
                +G_LASTNAME_COL2+" text , "
                +G_FIRSTNAME_COL3+" text, "
                +G_MIDNAME_COL4+" text, "
                +G_AGE_COL5+" integer , "
                +G_BIRTHDAY_COL6+" date , "
//                +G_GENDER_COL7+" text , "
                +G_CONTACTNO_COL8+" integer , "
                +G_EMAIL_COL9+" text unique, "
                +G_UNAME_COL10+" text unique, "
                +G_PASSWORD_COL10+" text);");

        Log.e("Table Operations :", "Guardian Detail Created");

        db.execSQL("CREATE TABLE "+childDetailModel+" ( " +
                ""+C_ID_COL1+" integer primary key autoincrement, "
                +C_LASTNAME_COL2+" text, "
                +C_FIRSTNAME_COL3+" text, "
                +C_MIDDLENAME_COL4+" text, "
                +C_AGE_COL5+" text, "
                +C_BIRTHDAY_COL6+" text, "
                +C_GENDER_COL7+" text, "
                +C_PHOTO_COL8+");");

        Log.e("Table Operations :", "Child Detail Created");

        db.execSQL("CREATE TABLE "+deviceDetailModel+" ( " +
                ""+D_IDNO_COL1+" integer primary key autoincrement, "
                +D_DEVICENO_COL2+" text unique, "
                +D_DEVSTATUS_COL3+" text );");

        Log.e("Table Operations :", "Device Detail Created");

        // inserts a default user into the db //
        db.execSQL("insert into guardianDetails (lastname, firstname, middlename, age, birthday,gender, contactno, email, username, password) values " +
                "('dela cruz', 'juan', 'jose', 33 , '1972-12-08', 'male', 09123456789 , 'juandelacruz@gmail.com', 'juandcruz' , 'testing1')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Removes Existing Table

        db.execSQL("drop table if exists "+guardianDetailModel);
        db.execSQL("drop table if exists " + childDetailModel);
        db.execSQL("drop table if exists " + deviceDetailModel);
        onCreate(db);

        Log.e("Table Operations :", "Dropped Existing Tables");
    }

    //Insert --> Guardian Registration

    public boolean addguardian(String lastname, String firstname, String midname, String age,String contactno, String email, String uname, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(G_LASTNAME_COL2, lastname);
        contentValues.put(G_FIRSTNAME_COL3, firstname);
        contentValues.put(G_MIDNAME_COL4, midname);
        contentValues.put(G_AGE_COL5, age);
//        contentValues.put(G_BIRTHDAY_COL6, bday);
//        contentValues.put(G_GENDER_COL7, gender);
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

    public boolean addGuardianGender(String selectedGender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(G_GENDER_COL7,selectedGender);
        long result = db.insert(guardianDetailModel, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;

    }

    //Insert --> Child Registration

    public boolean addchild(String clname, String cfname, String cmname, String cage, String cbday, String cgender, byte[]   cphoto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C_LASTNAME_COL2, clname);
        contentValues.put(C_FIRSTNAME_COL3, cfname);
        contentValues.put(C_MIDDLENAME_COL4, cmname);
        contentValues.put(C_AGE_COL5, cage);
        contentValues.put(C_BIRTHDAY_COL6, cbday);
        contentValues.put(C_GENDER_COL7, cgender);
        contentValues.put(C_PHOTO_COL8, cphoto);
        Log.e("Table Operations : ", "Inserted One Child-Device User");
        long result = db.insert(childDetailModel, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    //Update

    //Cursors : Responsible for searching data requirements

    public Cursor userlogin(String loginame, String loginpword, SQLiteDatabase db){
        String query = "select * from guardianDetails where username = '"+loginame+"' and password = '"+loginpword+"'";
        Log.d("query", query);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }


}
