package com.arealbreakfast.breakfastapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class FriendsFrag extends android.support.v4.app.Fragment {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");
    private DatabaseReference friendRef = rootRef.child("friends");
    private static final String TAG = "friendsfrag: ";

    final ArrayList<String> friendsAL = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_friends, container, false);
        //todo: this class does not update in real time, probably because friendsAL does not clear itself. where to clear it though?
        Query q = friendRef.orderByChild("uid1").equalTo(mAuth.getCurrentUser().getUid()); //todo: the saame for uid2, and only if status = 1!
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    TextView textView = (TextView) rootView.findViewById(R.id.nofriendsmsgtv);
                    textView.setText("");
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    if (friend.getStatus().equals("1")) {
                        getNameandDisplay(friend.getUid2());
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


        //for uid2
        Query w = friendRef.orderByChild("uid2").equalTo(mAuth.getCurrentUser().getUid()); //todo: only if status = 1!
        w.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    if (friend.getStatus().equals("1")) {
                        TextView textView = (TextView) rootView.findViewById(R.id.nofriendsmsgtv);
                        textView.setText("");
                        getNameandDisplay(friend.getUid1());
                    }


                    ListView listView = (ListView) rootView.findViewById(R.id.friendslist_lv);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, friendsAL);
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


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabf);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddFriends.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    //gets name by the uid passed, adds it to friendsAL and displays friendsAL
    private void getNameandDisplay(String uid2) {
        Query q = userRef.orderByChild("uid").equalTo(uid2);
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    friendsAL.add(user.getName());
                    ListView listView = (ListView) getActivity().findViewById(R.id.friendslist_lv);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, friendsAL);
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
