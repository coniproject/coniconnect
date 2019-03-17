package com.example.coni;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.SQLInput;
import java.util.Calendar;

public class SmsReceiver extends BroadcastReceiver {

    DatabaseReference databaseLocation;
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

        separatedSMS[1].replace('.',',');
        separatedSMS[3].replace('.',',');

//        databaseLocation.setValue(mydate);
//        databaseLocation.setValue(senderNumber);
//        databaseLocation.setValue(separatedSMS[1]);
//        databaseLocation.setValue(separatedSMS[3]);

        String recipient = "recipient";
        String latitude = "latitude";
        String longitude = "longitude";
        DatabaseReference mRef = databaseLocation.push();
        mRef.child(mydate).setValue(mydate);
        mRef.child(recipient).setValue(senderNumber);
        mRef.child(latitude).setValue(separatedSMS[1]);
        mRef.child(longitude).setValue(separatedSMS[3]);
        Toast.makeText(context, "Location Updated", Toast.LENGTH_SHORT).show();

    }



}
