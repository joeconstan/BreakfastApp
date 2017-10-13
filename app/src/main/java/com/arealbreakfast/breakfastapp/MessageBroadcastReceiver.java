package com.arealbreakfast.breakfastapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;


public class MessageBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "hey1: ";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        Log.v(TAG, "Broadcast received: " + action);

        if (action.equals("newmessage")) {
            android.support.v4.app.NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.icon_bubble)
                            .setContentTitle("my title")
                            .setContentText("like this notification? ;)")
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setDefaults(Notification.DEFAULT_ALL);

            int notificationID = 1;
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(notificationID, mBuilder.build());
        }

    }
}
