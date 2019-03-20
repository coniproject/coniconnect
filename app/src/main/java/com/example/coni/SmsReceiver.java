package com.example.coni;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class SmsReceiver extends BroadcastReceiver {

    DatabaseReference databaseLocation;
    private HashMap<String, Double> coordinates = new HashMap<String, Double>();
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            String senderNumber = null;

            for (int i = 0; i < pdus.length; i++) {

                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);

                senderNumber = sms.getOriginatingAddress();
                String message = sms.getMessageBody();


                    Toast.makeText(context, senderNumber + message, Toast.LENGTH_LONG).show();
//                    sendSMStoDatabase(sms, context);

                sendDataToFirebase(sms,context);


            }

        }
    }

    public void sendSMStoDatabase(SmsMessage sms, Context context) {

        DBHelper mydb = new DBHelper(context);
        SQLiteDatabase db = mydb.getWritableDatabase();
        String senderNumber = sms.getOriginatingAddress();
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String message = sms.getMessageBody();
        String[] separatedSMS = message.split("\\s+");

        if(senderNumber.equals("+639179562277")) {
            mydb.updateLocation(mydate,senderNumber,separatedSMS[1],separatedSMS[3]);

            abortBroadcast();
        }


        ContentValues values = new ContentValues();
        values.put("date", mydate);
        values.put("recipient", senderNumber);
        values.put("lat", separatedSMS[1]);
        values.put("long", separatedSMS[3]);

        db.insert("locationDetailModel",null,values);

    }

    public void sendDataToFirebase(SmsMessage sms, Context context) {

        FirebaseApp.initializeApp(context);
        databaseLocation = FirebaseDatabase.getInstance().getReference("conilocationdata");

        String senderNumber = sms.getOriginatingAddress();
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String message = sms.getMessageBody();
        String[] separatedSMS = message.split("\\s+");
        String needHelp = "I NEED HELP";

//        if(senderNumber.equals("+639179562277")) {
            DatabaseReference mRef = databaseLocation.push();
//            Double latitudedb = Double.parseDouble(separatedSMS[1]);
//            Double longitudedb = Double.parseDouble(separatedSMS[3]);

            double latitudedb = Double.parseDouble(separatedSMS[1]);
            double longitudedb = Double.parseDouble(separatedSMS[3]);



//            LocationArray locationArray = new LocationArray(mydate,senderNumber,latitudedb,longitudedb);
//            databaseLocation.child("conilocationdata").push().setValue(locationArray);

//            HashMap<String, String> data = new HashMap<String, String>();
//            data.put("date", mydate);
//            data.put("recipient", senderNumber);
//            mRef.setValue(data);

//            HashMap<String,Double> coordinates = new HashMap<String,Double>();
            coordinates.put("latitude", latitudedb);
            coordinates.put("longitude", longitudedb);
            mRef.setValue(coordinates);

            Double lcoords = coordinates.get("latitude");
            Double lcoords2 = coordinates.get("longitude");




            mRef.getKey();

            System.out.println(lcoords);
            System.out.println(lcoords2);

            Intent toMap = new Intent(context,MapActivity.class);
//            Bundle coords = new Bundle();
            toMap.putExtra("lat",lcoords);
            toMap.putExtra("lon",lcoords2);
            toMap.putExtras(toMap);
            context.startActivity(toMap);



//            System.out.print(toMap);

//        }


        }

    public HashMap<String, Double> getLocMap() {
        return coordinates;

    }






}
