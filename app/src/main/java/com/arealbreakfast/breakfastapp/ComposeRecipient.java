package com.arealbreakfast.breakfastapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ComposeRecipient extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference friendRef = rootRef.child("friends");

    //private final static String TAG = "here: ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_recipient);
        //todo: get list of friends from database (then store that info locally?) and put into a clickable listview
        //ListView lv = (ListView) findViewById(R.id.);
        //getFriends();
        final String friends[] = new String[1]; //todo: add this "getfriends" function that ive made to a separate interface and implement it. do the same w getnamebyuid, and toolbar functions
        friends[0] = "hi";

        Query q = friendRef.orderByChild("status").equalTo("1"); //limit with two where clauses or filter out the objects that are returned?
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    if ((mAuth.getCurrentUser()!=null) && (friend.getUid1().equals(mAuth.getCurrentUser().getUid()))){
                        //friends[0] = getnamebyuid(friend.getUid1() or 2?!);
                    }
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

    }
}
