package com.arealbreakfast.breakfastapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FriendRequest extends FireBaseInformationFunctions {

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");
    private DatabaseReference friendRef = rootRef.child("friends");
    private final static String TAG = "nameofPerson: ";

    //friend codes:
    //0 - requested, no response
    //1 - friends
    //2 - rejected

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        ListView lv = (ListView) findViewById(R.id.friendrequestlv);
        final String friendRequests[] = new String[1]; //todo: make size dynamic - use (array)list instead of string[]?
        friendRequests[0] = "hi";

        Query q = friendRef.orderByChild("status").equalTo("0");
        q.addChildEventListener(new ChildEventListener() { //snapshot is null - query not returning anything
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "made it into listener");
                if (dataSnapshot.exists()) {
                    Friend person = dataSnapshot.getValue(Friend.class);
                    String uid1 = person.getUid1();
                    String nameofPerson = getNameByUid(uid1);
                    Log.v(TAG, nameofPerson);
                    friendRequests[0] = nameofPerson;
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

        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendRequests);
        lv.setAdapter(adapter);
    }





}
