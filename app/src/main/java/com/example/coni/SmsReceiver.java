package com.example.coni;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.view.View;
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

//                    sendSMStoSQLiteDatabase(sms, context);

                sendDataToFirebase(sms,context);


            }

        }
    }

    public void sendSMStoSQLiteDatabase(SmsMessage sms, Context context) {

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

        if (senderNumber.equals("+639959315552") && message.contains("LATITUDE:")) {
            DatabaseReference mRef = databaseLocation.push();
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

            Intent toMap = new Intent(context, MapActivity.class);
            toMap.putExtra("lat", lcoords);
            toMap.putExtra("lon", lcoords2);
            toMap.putExtras(toMap);
            context.startActivity(toMap);
        }

//        if (senderNumber.equals("+639959315552") && message.equals(needHelp)) {
//
//        }



     }



}
