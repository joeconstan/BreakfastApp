package com.arealbreakfast.breakfastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NewGroup extends AppCompatActivity {
//todo: instead of entering names, could choose from friends list. also, override on backpressed to go to lobby always from composemessage
//todo: change messagerecipients for groups depending on who sent each msg
//todo: only show the group once in the list view - each new msg makes it appear another time
//todo: add everyone to msg recip for groupmessages, even the user
    private static final String TAG = "okay doke";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ArrayList<String> groupNames = new ArrayList<>();
    private ArrayList<String> groupUids = new ArrayList<>();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);


        Button createButton = (Button) findViewById(R.id.newgroupcreatebutton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ComposeMessage.class);
                EditText groupName = (EditText) findViewById(R.id.newgroupnameet);
                String name = groupName.getText().toString();
                intent.putExtra("recp", name);
                EditText et = (EditText) findViewById(R.id.newgroupmemberset);
                String str = et.getText().toString();

                List<String> items = Arrays.asList(str.split("\\s*,\\s*"));
                for (String x : items) {
                    groupNames.add(x);
                    Log.v(TAG, "groupmember 1: " + x);
                }

                putUidsbyNames(groupNames, intent);
                intent.putExtra("uid1", mAuth.getCurrentUser().getUid());
                intent.putExtra("isgroup", 1);
            }
        });
    }

    public void putUidsbyNames(final ArrayList<String> names, final Intent intent) {
        for (int i = 0; i < names.size(); i++) {
            Query q = userRef.orderByChild("name").equalTo(names.get(i));
            final int finalI = i;
            q.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        groupUids.add(user.getUid());
                        Log.v(TAG, "in listener for groupuids");
                        intent.putExtra("groupuids", groupUids);
                        intent.putExtra("membercount", groupUids.size());
                        if (finalI ==(names.size()-1)){
                            startActivity(intent);
                        }
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
        }
        //startActivity(intent);

    }
}
