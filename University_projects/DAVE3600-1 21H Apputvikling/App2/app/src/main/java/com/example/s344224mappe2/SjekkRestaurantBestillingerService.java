package com.example.s344224mappe2;

import static com.example.s344224mappe2.MainActivity.CHANNEL_ID;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SjekkRestaurantBestillingerService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DBHandler db = new DBHandler(getApplicationContext());
        List<Bestilling> bestillinger = db.getAlleBestillinger();
        String today = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        List<Bestilling> bestillingerIdag = compareDates(today, bestillinger);
        if (bestillingerIdag.size() != 0) {
            String melding = "Du har "+bestillingerIdag.size()+" bestillinger i dag";
            if (bestillingerIdag.size() == 1) melding = "Du har "+bestillingerIdag.size()+" bestilling i dag";
            //Toast.makeText(getApplicationContext(), " I MinService", Toast.LENGTH_SHORT).show();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent i = new Intent(this, MainActivity.class);
            PendingIntent pIntent= PendingIntent.getActivity(this, 0, i, 0);
            Notification notifikasjon = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Restaurant Bestillinger")
                    .setContentText(melding)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pIntent).build();
            notifikasjon.flags|= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, notifikasjon);
            sendSMStilVenner(bestillingerIdag);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public List<Bestilling> compareDates(String date, List<Bestilling> bestillinger) {
        List<Bestilling> newList = new ArrayList<>();
        for (Bestilling b : bestillinger) {
            String dato = new SimpleDateFormat("dd.MM.yyyy").format(b.getDato());
            if (dato.equals(date)) newList.add(b);
        }
        return newList;
    }

    public void sendSMStilVenner(List<Bestilling> bestillinger) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String melding = prefs.getString("sms", "Du har en reservasjon i dag");
        for (Bestilling b : bestillinger) {
            List<Venn> venner = b.getVenner();
            for (Venn v : venner) {
                String tel = v.getTelefon();
                sendSMS(tel, melding);
            }
        }
    }

    public void sendSMS(String phoneNo, String message) {
        final int MY_PERMISSIONS_REQUEST_SEND_SMS = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        final int MY_PHONE_STATE_PERMISSION = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (MY_PERMISSIONS_REQUEST_SEND_SMS == PackageManager.PERMISSION_GRANTED && MY_PHONE_STATE_PERMISSION == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsMan = SmsManager.getDefault();
            smsMan.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(this, "Har sendt sms", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Mangler tillatelse for å sende SMS", Toast.LENGTH_SHORT).show();
        }
    }
}
