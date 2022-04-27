package com.example.s344224mappe2;

import static com.example.s344224mappe2.TimePreference.parseHour;
import static com.example.s344224mappe2.TimePreference.parseMinute;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SettPeriodiskService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String tid = prefs.getString("tid", "09:00");


        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, parseHour(tid));
        cal.set(Calendar.MINUTE, parseMinute(tid));

        Intent i = new Intent(this, SjekkRestaurantBestillingerService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000 * 60 * 60 * 24, pintent);
        return super.onStartCommand(intent, flags, startId);
    }

}
