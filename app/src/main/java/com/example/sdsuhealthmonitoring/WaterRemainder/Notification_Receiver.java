package com.example.sdsuhealthmonitoring.WaterRemainder;


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

import static com.example.sdsuhealthmonitoring.App.CHANNEL_1_ID;

public class Notification_Receiver extends BroadcastReceiver {

    private NotificationManagerCompat notificationCompat;
   // private String notification_message;
    NotificationCompat.Builder builder;
    @Override
    public void onReceive(Context context, Intent intent) {

        notificationCompat= NotificationManagerCompat.from(context);

        //NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent repeating_Intent = new Intent(context,Repeating_Activity.class);
//        repeating_Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
////        notification_message=intent.getStringExtra("Remainder1");
//
//        PendingIntent pendingIntent=PendingIntent.getActivity(context,100,repeating_Intent,PendingIntent.FLAG_UPDATE_CURRENT);

//            builder =new NotificationCompat.Builder(context)
//                    .setSmallIcon(android.R.drawable.arrow_up_float)
//                    .setContentTitle("Water Remainder")
//                    .setContentText("It's time to have a cup of water")
//                    .setAutoCancel(true);
//            Toast.makeText(context,"Water Remainder"+String.valueOf(context),Toast.LENGTH_LONG).show();
//
//
//        assert notificationManager != null;
//        notificationManager.notify(100,builder.build());

        Notification notification=new NotificationCompat.Builder(context,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_announcement_black_24dp)
                .setContentTitle("Water Remainder")
                .setContentText("Its time to have a cup of water")
                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                .build();
        notificationCompat.notify(1,notification);

    }
}
