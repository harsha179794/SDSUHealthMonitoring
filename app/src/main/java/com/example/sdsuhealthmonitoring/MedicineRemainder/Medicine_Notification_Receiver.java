package com.example.sdsuhealthmonitoring.MedicineRemainder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.example.sdsuhealthmonitoring.R;

import static com.example.sdsuhealthmonitoring.App.CHANNEL_2_ID;

public class Medicine_Notification_Receiver extends BroadcastReceiver {

    private NotificationManagerCompat notificationCompat;

    //private String notification_message;
    NotificationCompat.Builder builder;
    @Override
    public void onReceive(Context context, Intent intent) {


        notificationCompat= NotificationManagerCompat.from(context);

        Notification notification=new NotificationCompat.Builder(context,CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_announcement_black_24dp)
                .setContentTitle("Medicine Remainder")
                .setContentText("Please take your medicines")
                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                .build();
        notificationCompat.notify(2,notification);

    }
}
