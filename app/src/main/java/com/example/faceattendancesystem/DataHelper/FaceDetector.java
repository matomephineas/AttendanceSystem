package com.example.faceattendancesystem.DataHelper;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class FaceDetector extends Application {
    public static final String CHANNEL_1_ID="Warning";
    public static final String CHANNEL_2_ID="Warning";
    @Override
    public void onCreate()
    {
        super.onCreate();
        oncreateNotificationChannel();
    }

    private void oncreateNotificationChannel()
    {
         if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
         {
             NotificationChannel channel = new NotificationChannel(
                     CHANNEL_1_ID,"Time Warning",
                     NotificationManager.IMPORTANCE_HIGH
             );
             channel.setDescription("Time Warning");
             NotificationManager manager=getSystemService(NotificationManager.class);
             manager.createNotificationChannel(channel);
         }
    }
}
