package com.arealbreakfast.breakfastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
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

    private static final String TAG = "recp: ";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");
    private DatabaseReference messagesRef = rootRef.child("messages");
    private DatabaseReference groupMsgRef = rootRef.child("groupmessages");
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
        Log.v(TAG, getIntent().getStringExtra("recp"));


        if (getIntent().getIntExtra("isgroup", 0) == 0) {
            Query q = messagesRef.child(getKey()).orderByKey();
            q.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    allThemMessages.clear();
                    msgUserKeys.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) { //todo: everytime a msg is sent does this reload every msg? way too much overhead
                            Message ms = postSnapshot.getValue(Message.class);
                            if (ms.getMessageRecipient().equals(mAuth.getCurrentUser().getUid())) {
                                ms.setRead(1);
                                String k = postSnapshot.getKey();
                                messagesRef.child(getKey()).child(k).setValue(ms);

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


        } else { //it's a group message
            Query q = groupMsgRef.child(getGroupKey()).orderByKey();
            q.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    allThemMessages.clear();
                    msgUserKeys.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) { //todo: everytime a msg is sent does this reload every msg? way too much overhead
                            GroupMessage ms = postSnapshot.getValue(GroupMessage.class);
                            int i = 0;
                            for (String x : ms.getMessageRecipient()) {
                                if (x.equals(mAuth.getCurrentUser().getUid())) {
                                    ArrayList<Integer> read = ms.getRead();
                                    read.set(i, 1);
                                    ms.setRead(read);
                                    String k = postSnapshot.getKey();
                                    groupMsgRef.child(getGroupKey()).child(k).setValue(ms);
                                }
                                i++;
                            }
                            allThemMessages.add(ms.getMessageText() + "\n"); //todo: this must not be happening
                            Log.v(TAG, "made it into listener");
                            if (ms.getCreator().equals(mAuth.getCurrentUser().getUid())) {
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

        }

    }

    //create key by taking the entire creator uid plus the first 5 digits of each other uid
    private String getGroupKey() {
        Intent intent = getIntent();
        String uid1 = intent.getStringExtra("uid1");
        ArrayList<String> uids = intent.getStringArrayListExtra("groupuids"); //todo: add uid1 to this? and just sort all alphabetically w/o regard to creator
        String key = uid1;
        for (int i=0;i<uids.size();i++) {
            String x = uids.get(i);
            key += x.substring(0, 5);
        }

        return key;
    }

    //create key by combining the two uids alphabetically
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
        if (getIntent().getIntExtra("isgroup", 0) == 0) {
            final EditText msgText = (EditText) findViewById(R.id.newmsg_et);
            String messageText = msgText.getText().toString();
            String messageUser = mAuth.getCurrentUser().getDisplayName();
            String messageRecipient = getIntent().getStringExtra("uid2");
            Message msg = new Message(messageText, messageUser, messageRecipient);
            messagesRef.child(getKey()).push().setValue(msg);
            msgText.setText("");
        } else {   //todo: modify this chunk && display messages in lobby or wherever from group msgs
            final EditText msgText = (EditText) findViewById(R.id.newmsg_et);
            String messageText = msgText.getText().toString();
            String messageUser = mAuth.getCurrentUser().getUid();
            ArrayList<String> messageRecipients = getIntent().getStringArrayListExtra("groupuids");
            messageRecipients.add(messageUser);
            GroupMessage msg = new GroupMessage(messageText, messageUser, messageRecipients, getIntent().getStringExtra("recp"));
            groupMsgRef.child(getGroupKey()).push().setValue(msg);
            msgText.setText("");
        }

    }

    public void displayMessages() {
        ListView lv = (ListView) findViewById(R.id.mainmsgarea_lv);
        MessageAdapter adapter = new MessageAdapter(this, allThemMessages, msgUserKeys);
        lv.setAdapter(adapter);
    }
}
