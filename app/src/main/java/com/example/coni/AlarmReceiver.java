package com.example.coni;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(10000);

        Notification notification = new Notification.Builder(context)
                .setContentTitle("YOUR CHILD NEEDS HELP!")
                .setContentText("YOUR CHILD NEEDS HELP!")
                .build();

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags|= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0,notification);

        Uri notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone r = RingtoneManager.getRingtone(context,notif);
        r.play();
    }

}
