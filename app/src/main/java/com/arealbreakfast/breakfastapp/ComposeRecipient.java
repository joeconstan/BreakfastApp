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


public class ComposeRecipient extends FireBaseInformationFunctions {

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
         //todo: add this "getfriends" function that ive made to a separate interface and implement it. do the same w getnamebyuid, and toolbar functions


        String[] friends = getFriends();


    }
}
