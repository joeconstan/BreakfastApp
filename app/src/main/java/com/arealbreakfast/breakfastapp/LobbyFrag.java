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
import java.util.HashSet;
import java.util.Set;

public class LobbyFrag extends android.support.v4.app.Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");
    private DatabaseReference messagesRef = rootRef.child("messages");
    private DatabaseReference groupMsgRef = rootRef.child("groupmessages");
    final ArrayList<String> conversations = new ArrayList<>();
    final ArrayList<String> uids = new ArrayList<>();
    final ArrayList<String> groupuids = new ArrayList<>();
    private static final String TAG = "lobbyfrag: ";
    final ArrayList<Integer> isGroup = new ArrayList<>(); //0-no, 1-yes
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


                        Query r = userRef.orderByChild("uid").equalTo(otherUid);
                        r.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.exists()) {
                                    User u = dataSnapshot.getValue(User.class);
                                    conversations.add(u.getName());
                                    isGroup.add(0);
                                    uids.add(u.getUid());
                                    Log.v(TAG, "u.getName(): " + u.getName());
                                    ListView lv = (ListView) getActivity().findViewById(R.id.convolist_lv);
                                    ConversationListAdapter ad = new ConversationListAdapter(getContext(), conversations, uids);
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


        //todo : this entire chunk needs modifying for group msgs
        //checking if group chats exist for this user
        final Query g = groupMsgRef.orderByKey();
        g.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    String key = dataSnapshot.getKey();
                    if ((key.contains(mAuth.getCurrentUser().getUid())) || (key.contains(mAuth.getCurrentUser().getUid().substring(0, 5)))) { //if user is part of chat

                        //remove no messages text
                        TextView nomsgstv = (TextView) rootView.findViewById(R.id.nomsgsmsgtv);
                        nomsgstv.setText("");

                        //display conversations
                        Query gr = groupMsgRef.child(key).orderByKey(); //only add these to conversations AL
                        //Query gr = groupMsgRef.orderByKey();
                        gr.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.exists()) {
                                    GroupMessage groupMessage = dataSnapshot.getValue(GroupMessage.class);
                                    conversations.add(groupMessage.getGroupName());
                                    //todo: there is probably a better soln to avoid duplicates. does this hurt the order? should order by time anyway, not sure if it does that anyway

                                    //to remove duplicates. todo: not efficient. should not add them in the first place
                                    Set<String> hs = new HashSet<>();
                                    hs.addAll(conversations);
                                    conversations.clear();
                                    conversations.addAll(hs);

                                    isGroup.add(1);
                                    intent = new Intent(rootView.getContext(), ComposeMessage.class);
                                    intent.putExtra("uid1", groupMessage.getCreator());
                                    for (int i = 0; i < groupMessage.getMessageRecipient().size(); i++) {
                                        uids.add(groupMessage.getMessageRecipient().get(i));
                                        groupuids.add(groupMessage.getMessageRecipient().get(i));
                                    }
                                    //todo: add all recipient uids to this--uids.add(u.getUid());
                                    ListView lv = (ListView) getActivity().findViewById(R.id.convolist_lv);
                                    ConversationListAdapter ad = new ConversationListAdapter(getContext(), conversations, uids);
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


                        ListView lv = (ListView) rootView.findViewById(R.id.convolist_lv);
                        final ArrayAdapter<String> ad = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, conversations);
                        lv.setAdapter(ad);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                intent.putExtra("recp", ad.getItem(position));
                                //putUidByName(ad.getItem(position));
                                if (isGroup.get(position) == 1) {
                                    intent.putExtra("isgroup", 1);
                                    intent.putExtra("groupuids", groupuids);
                                    startActivity(intent);
                                } else {
                                    intent.putExtra("uid1", mAuth.getCurrentUser().getUid());
                                    intent.putExtra("isgroup", 0);
                                    putUidByName(ad.getItem(position));
                                }
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
                if (dataSnapshot.exists()) {
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
