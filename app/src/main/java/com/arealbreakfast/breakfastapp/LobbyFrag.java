package com.arealbreakfast.breakfastapp;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.NotificationCompat;
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
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LobbyFrag extends android.support.v4.app.Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");
    private DatabaseReference messagesRef = rootRef.child("messages");
    final ArrayList<String> conversations = new ArrayList<>();
    private static final String TAG = "lobbyfrag: ";
    Intent intent; //if doesnt work can make just Intent intent; and assign later

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_lobby, container, false);

        getActivity().startService(new Intent(getContext(), MessageService.class));




        //checking if chats exist for this user
        Query q = messagesRef.orderByKey();
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    String key = dataSnapshot.getKey();
                    if (key.contains(mAuth.getCurrentUser().getUid())) {
                        String otherUid = key.replace(mAuth.getCurrentUser().getUid(), "");
                        //Message m = dataSnapshot.getValue(Message.class); //todo : get name by uid & remove messageRecipient from message.class
                        TextView nomsgstv = (TextView) rootView.findViewById(R.id.nomsgsmsgtv);
                        nomsgstv.setText("");


                        Query q = userRef.orderByChild("uid").equalTo(otherUid);
                        q.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.exists()) {
                                    User u = dataSnapshot.getValue(User.class);
                                    conversations.add(u.getName()); //u.getname is null i think -- not working
                                    Log.v(TAG, "u.getName(): " + u.getName());
                                    ListView lv = (ListView) getActivity().findViewById(R.id.convolist_lv);
                                    ArrayAdapter<String> ad = new ArrayAdapter<String>(getContext(), R.layout.conversationsandfriends_list_item, R.id.conversationsandfriends_list_item, conversations);
                                    lv.setAdapter(ad);
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


                        ListView lv = (ListView) getActivity().findViewById(R.id.convolist_lv);
                        final ArrayAdapter<String> ad = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, conversations);
                        lv.setAdapter(ad);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                intent = new Intent(view.getContext(), ComposeMessage.class);
                                intent.putExtra("uid1", mAuth.getCurrentUser().getUid());
                                intent.putExtra("recp", ad.getItem(position));
                                putUidByName(ad.getItem(position));
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

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabl);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ComposeRecipient.class);
                startActivity(intent);
            }
        });


        return rootView;
    }

    private void putUidByName(String userName) {
        Query q = userRef.orderByChild("name").equalTo(userName);
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    intent.putExtra("uid2", user.getUid());
                    startActivity(intent);
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
