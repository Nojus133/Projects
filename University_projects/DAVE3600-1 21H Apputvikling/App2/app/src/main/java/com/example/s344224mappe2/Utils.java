package com.example.s344224mappe2;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;

public class Utils {
    public static void startService(Context context) {
        Intent intent= new Intent();
        intent.setAction("com.example.s344224mappe2.MinBroadcastReceiver");
        context.sendBroadcast(intent);
    }

    public static void stoppService(Context context) {
        Intent i = new Intent(context, SjekkRestaurantBestillingerService.class);
        PendingIntent pintent = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarm != null) alarm.cancel(pintent);
    }

    public static void tillattService(Context context, boolean b) {
        if (b) startService(context);
        else stoppService(context);
    }

    public static void restartService(Context context) {
        stoppService(context);
        startService(context);
    }
}
