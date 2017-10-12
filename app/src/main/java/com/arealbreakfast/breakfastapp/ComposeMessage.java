package com.arealbreakfast.breakfastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ComposeMessage extends BaseToolbarActivity {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    private DatabaseReference messagesRef = rootRef.child("messages");
    final ArrayList<String> friends = new ArrayList<>();
    ArrayList<String> allThemMessages = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);


        Intent intent = getIntent();
        //String recp = intent.getStringExtra("recp");


        Query q = messagesRef.child(getKey()).orderByKey();
        q.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        allThemMessages.clear();
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                                Message ms = postSnapshot   .getValue(Message.class);
                                                allThemMessages.add(ms.getMessageUser() + ": " + ms.getMessageText() + "\n");
                                            }
                                            displayMessages();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
       /* q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Message ms = dataSnapshot.getValue(Message.class);
                    allThemMessages.add(ms.getMessageUser() + ": " + ms.getMessageText() + "\n");
                    displayMessages();
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
        });*/

                //messagesRef.push().setValue(msg);
       /* messagesRef.push().setValue(msg, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        String uniqueKey = databaseReference.getKey();
                    }
                });*/


                //todo: set recp at top somewhere - toolbar if we can.

    }

    private String getKey() {
        Intent intent = getIntent();
        String uid1 = intent.getStringExtra("uid1");
        String uid2 = intent.getStringExtra("uid2");
        final String key;
        if (uid1.compareTo(uid2) > 0)
            key = uid1 + uid2;
        else
            key = uid2 + uid1;
        return key;
    }

    public void sendMessage(View view) {

        final EditText msgText = (EditText) findViewById(R.id.newmsg_et);
        String messageText = msgText.getText().toString();
        String messageUser = mAuth.getCurrentUser().getDisplayName();
        Message msg = new Message(messageText, messageUser);
        //messagesRef.push().setValue(msg);
        messagesRef.child(getKey()).push().setValue(msg, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   DatabaseReference databaseReference) {
                String uniqueKey = databaseReference.getKey();
                msgText.setText("");

                //display messages
               /* Query q = messagesRef.child(getKey()).orderByKey();
                q.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.exists()){
                            Message m = dataSnapshot.getValue(Message.class);
                            allThemMessages.add(m.getMessageUser() + ": " + m.getMessageText()+ "\n");
                            displayMessages();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });*/
            }
        });


    }

    public void displayMessages() {
        TextView tv = (TextView) findViewById(R.id.mainmsgarea_tv);
        String x = "";
        for (String y : allThemMessages) {
            x += y;
        }
        tv.setText(x);
    }
}
