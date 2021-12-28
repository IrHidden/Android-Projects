package ir.myapp.controller3.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import ir.myapp.controller3.R;
import ir.myapp.controller3.activity.MainActivity;
import ir.myapp.controller3.activity.Restarter;
import ir.myapp.controller3.comunication.Information;
import ir.myapp.controller3.comunication.SMSInformation;
import ir.myapp.controller3.settings.Constants;
import ir.myapp.controller3.receiver.SMSReceiver;

public class SMSService extends Service {
    SMSReceiver smsReceiver = null;
    BroadcastReceiver notifupdate = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground(getStatusNotification());
        } else {
            startForeground(Constants.STATUS_NOTIFICATION_ID, getStatusNotification());
        }
    }


    private Notification getPowerOffNotification(){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID);
        String content = "Power is Off";
        String title = "Power Problem";
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(NotificationManager.IMPORTANCE_LOW)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
        } else {
            notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(title)
                    .setContentText(content)
                    .build();
        }
        return notification;
    }

    private Notification getStatusNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID);

        Information information = new SMSInformation(this, Information.PreferenceNames.New);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String Content = "Temperature: " + information.getAVGSensorsTemperatures() + "°C" + " -> " + information.getAVGHeatersExpectedTemp() + "°C"
                + "\tHumidity: " + information.getAVGSensorsHumidity() + "%" + " -> " + information.getAVGVapoursExpectedHumidity() + "%";
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("Status: ")
                    .setContentText(Content)
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setContentIntent(pendingIntent)
                    .build();
        } else {
            notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("Status: ")
                    .setContentText(Content)
                    .setContentIntent(pendingIntent)
                    .build();
        }
        return notification;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground(Notification notification) {

        Log.d("SMS Service", "using android oreo version");

        NotificationChannel chan = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_Name, NotificationManager.IMPORTANCE_LOW);
        chan.setDescription("controller app");
        chan.setLightColor(Color.BLUE);
        chan.enableLights(true);
        chan.enableVibration(true);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        chan.setSound(uri, null); // This is IMPORTANT
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        startForeground(Constants.STATUS_NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notifupdate);
        unregisterReceiver(smsReceiver);
        stopForeground(true);
        smsReceiver = null;
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (smsReceiver != null) {
                unregisterReceiver(smsReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        notifupdate = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//              stopForeground(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startMyOwnForeground(getStatusNotification());
                } else {
                    startForeground(Constants.STATUS_NOTIFICATION_ID, getStatusNotification());
                }
            }
        };
        registerReceiver(notifupdate, new IntentFilter("ir.myapp.controller3.UPDATE_NOTIFICATION"));
        smsReceiver = new SMSReceiver();
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
//      Toast.makeText(this, "registered receiver", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }
}
