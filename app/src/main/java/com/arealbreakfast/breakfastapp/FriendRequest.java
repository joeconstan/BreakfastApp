package com.arealbreakfast.breakfastapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class FriendRequest extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    //private DatabaseReference userRef = rootRef.child("users");
    private DatabaseReference friendRef = rootRef.child("friends");
    private DatabaseReference userRef = rootRef.child("users");
    private final static String TAG = "nameofPerson: ";
    final ArrayList<String> friendrequestsAL = new ArrayList<>();
    String friendkey;

    //friend codes:
    //0 - requested, no response
    //1 - friends
    //2 - rejected

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        /*Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        mActionBarToolbar.setTitle("");
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitle("Friend Requests");*/


        ListView listView = (ListView) findViewById(R.id.friendrequestlv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //take to user profile or something i dunno. not important at this time.
            }
        });


        Query w = friendRef.orderByChild("uid2").equalTo(mAuth.getCurrentUser().getUid());
        w.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    if (friend.getStatus().equals("0")) {
                        friendkey = dataSnapshot.getKey();
                        getNameandDisplay(friend.getUid1());
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

    //gets name by the uid passed, adds it to friendsAL and displays friendsAL
    private void getNameandDisplay(String uid2) {
        TextView textView = (TextView) findViewById(R.id.nofriendrequestsmsgtv);
        textView.setText("");

        Query q = userRef.orderByChild("uid").equalTo(uid2);
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    friendrequestsAL.add(user.getName());
                    ListView listView = (ListView) findViewById(R.id.friendrequestlv);
                    FriendRequestAdapter adapter = new FriendRequestAdapter(friendrequestsAL, FriendRequest.this, friendkey);
                    listView.setAdapter(adapter);

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


}
