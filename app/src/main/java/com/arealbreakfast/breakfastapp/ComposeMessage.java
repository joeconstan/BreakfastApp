package com.arealbreakfast.breakfastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
    private DatabaseReference userRef = rootRef.child("users");
    private DatabaseReference messagesRef = rootRef.child("messages");
    final ArrayList<String> friends = new ArrayList<>();
    final ArrayList<Integer> msgUserKeys = new ArrayList<>(); //1: current user 0: other person
    ArrayList<String> allThemMessages = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);

        //set toolbar text to name of recipient
        TextView toolbarText = (TextView) findViewById(R.id.toolbartext);
        toolbarText.setText(getIntent().getStringExtra("recp"));


        Query q = messagesRef.child(getKey()).orderByKey();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allThemMessages.clear();
                msgUserKeys.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Message ms = postSnapshot.getValue(Message.class);
                        if (ms.getMessageRecipient().equals(mAuth.getCurrentUser().getUid())) {
                            //ms.setRead(1);
                            //messagesRef.child(getKey()).setValue(ms);

                        }
                        allThemMessages.add(ms.getMessageText() + "\n");
                        if (ms.getMessageUser().equals(mAuth.getCurrentUser().getDisplayName())) {
                            msgUserKeys.add(1);
                        } else
                            msgUserKeys.add(0);
                    }
                    displayMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
//schema changed somehow - should be by convo, not by msg
        final EditText msgText = (EditText) findViewById(R.id.newmsg_et);
        String messageText = msgText.getText().toString();
        String messageUser = mAuth.getCurrentUser().getDisplayName();
        String messageRecipient = getIntent().getStringExtra("uid2");
        //Query q = userRef.orderByChild("uid").equalTo();
        Message msg = new Message(messageText, messageUser, messageRecipient);
        messagesRef.child(getKey()).push().setValue(msg, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   DatabaseReference databaseReference) {
                String uniqueKey = databaseReference.getKey();
                msgText.setText("");
            }
        });

    }

    public void displayMessages() {
        ListView lv = (ListView) findViewById(R.id.mainmsgarea_lv);
        MessageAdapter adapter = new MessageAdapter(this, allThemMessages, msgUserKeys);
        lv.setAdapter(adapter);
    }
}
