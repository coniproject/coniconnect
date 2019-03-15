package com.example.coni;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLInput;
import java.util.Calendar;

public class SmsReceiver extends BroadcastReceiver {

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
//                String[] separatedSMS = message.split(":");

                    Toast.makeText(context, senderNumber + message, Toast.LENGTH_LONG).show();
                    sendSMStoDatabase(sms, context);


            }

        }
    }

    public void sendSMStoDatabase(SmsMessage sms, Context context) {

        DBHelper mydb = new DBHelper(context);
//        SQLiteDatabase db = mydb.getWritableDatabase();
        String senderNumber = sms.getOriginatingAddress();
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String message = sms.getMessageBody();
        String[] separatedSMS = message.split("\\s+");

        if(senderNumber.equals("+639179562277")) {
            mydb.updateLocation(mydate,senderNumber,separatedSMS[1],separatedSMS[3]);

            abortBroadcast();
        }


//        ContentValues values = new ContentValues();
//        values.put("date", mydate);
//        values.put("recipient", senderNumber);
//        values.put("lat", separatedSMS[1]);
//        values.put("long", separatedSMS[3]);
//
//        db.insert("locationDetailModel",null,values);

    }



}
