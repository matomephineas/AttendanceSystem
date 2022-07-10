package com.example.faceattendancesystem.DataHelper;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.faceattendancesystem.R;


public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer mp = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mp.start();

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"notifyLemubit")
                .setSmallIcon(R.drawable.alarm_24)
                .setContentTitle("Attendance time")
                .setContentText("Hey student please prepare for class")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =NotificationManagerCompat.from(context);
        notificationManager.notify(200,builder.build());
    }
}