package com.arealbreakfast.breakfastapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MessageService extends Service {

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference messagesRef = rootRef.child("messages");
    private static final String TAG = "hey1: ";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) { //todo: only fires once cuz only 1 child is added: the convo. not the msgs.
                if (dataSnapshot.exists()) {
                    final Message m = dataSnapshot.getValue(Message.class);
                    //making a listener for the individ messages in this found convo
                    String k = dataSnapshot.getKey();
                    messagesRef.child(k).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.exists()) {
                                final Message e = dataSnapshot.getValue(Message.class);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent("newmessage");
                                        intent.putExtra("messageText", e.getMessageText());
                                        sendBroadcast(intent);
                                        //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                                        Log.v(TAG, "sent broadcast from service");
                                    }
                                }).start();
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent("newmessage");
                            intent.putExtra("messageText", m.getMessageText());
                            sendBroadcast(intent);
                            //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            Log.v(TAG, "sent broadcast from service");
                        }
                    }).start();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service destroyed!", Toast.LENGTH_SHORT).show();
    }
}
