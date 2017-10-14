package com.arealbreakfast.breakfastapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MessageBroadcastReceiver extends BroadcastReceiver {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        int read = intent.getIntExtra("read", 0);
        String recipient = intent.getStringExtra("recipient");

        if (action.equals("newmessage") && read == 0 && (recipient.equals(mAuth.getCurrentUser().getUid()))) {
            String messageText = intent.getStringExtra("messageText");
            android.support.v4.app.NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.icon_bubble)
                            .setContentTitle("my title")
                            .setContentText(messageText)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setDefaults(Notification.DEFAULT_ALL);

            int notificationID = 1;
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(notificationID, mBuilder.build());
        }

    }
}
