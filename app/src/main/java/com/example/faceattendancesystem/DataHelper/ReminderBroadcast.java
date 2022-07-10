package com.example.faceattendancesystem.DataHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.faceattendancesystem.R;


public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,"notifyLemubit")
                .setSmallIcon(R.drawable.alert_24)
                .setContentTitle("Attendance")
                .setContentText("Hey student,this is a reminder")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(context);
        notificationManager.notify(200,builder.build());
    }
}
