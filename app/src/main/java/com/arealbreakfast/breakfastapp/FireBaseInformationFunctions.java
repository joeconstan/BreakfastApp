package com.arealbreakfast.breakfastapp;


import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

class FireBaseInformationFunctions extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");
    private DatabaseReference friendRef = rootRef.child("friends");

    private FirebaseAuth.AuthStateListener mAuthListener;

    private final static String TAG = "here: ";

    public String getNameByUid(String uid1) {
        final String[] name = new String[1]; //is making this an array to avoid 'final' problems really the best solution? it was the alt+enter solution
        name[0] = ""; //in case the snapshot doesnt exist
        Query q = userRef.orderByChild("uid").equalTo(uid1);

        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    User u = dataSnapshot.getValue(User.class);
                    name[0] = u.getName();
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
        return name[0];
    }


    public String[] getFriends(){ //change to arraylist?
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
        return new String[] {""};
    }
}
