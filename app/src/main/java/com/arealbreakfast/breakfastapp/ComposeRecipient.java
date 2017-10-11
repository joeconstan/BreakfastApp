package com.arealbreakfast.breakfastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;


public class ComposeRecipient extends AppCompatActivity {

    static String name; //better soln?
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");
    private DatabaseReference friendRef = rootRef.child("friends");
    final ArrayList<String> friends = new ArrayList<>();
    private FirebaseAuth.AuthStateListener mAuthListener;


    private static final String TAG = "okok: ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_recipient);

        final ArrayList<String> friends = new ArrayList<>();

        Query q = friendRef.orderByChild("status").equalTo("1");
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    if ((mAuth.getCurrentUser() != null) && (friend.getUid1().equals(mAuth.getCurrentUser().getUid()))) {
                        Log.v(TAG, "1 passed");
                        Query q = userRef.orderByChild("uid").equalTo(friend.getUid2()); //todo: https://stackoverflow.com/questions/30659569/wait-until-firebase-retrieves-data
                        q.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.exists()) {
                                    User u = dataSnapshot.getValue(User.class);

                                    name = u.getName();
                                    friends.add(name);
                                    displayFriends(friends);
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
                    } else if ((mAuth.getCurrentUser() != null) && (friend.getUid2().equals(mAuth.getCurrentUser().getUid()))) {
                        Query q = userRef.orderByChild("uid").equalTo(friend.getUid1()); //todo: https://stackoverflow.com/questions/30659569/wait-until-firebase-retrieves-data
                        q.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.exists()) {
                                    User u = dataSnapshot.getValue(User.class);
                                    name = u.getName();
                                    friends.add(name);
                                    displayFriends(friends);
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


    public void displayFriends(ArrayList<String> f) {
        ListView lv = (ListView) findViewById(R.id.compose_recipient_lv);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, f);
        Log.v(TAG, "friends.get(0):-------  " + f.get(0));
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedName = adapter.getItem(position);
                Intent intent = new Intent(view.getContext(), ComposeMessage.class);
                intent.putExtra("recp", selectedName);
                startActivity(intent);
            }
        });

    }
}