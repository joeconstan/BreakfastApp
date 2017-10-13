package com.arealbreakfast.breakfastapp;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchableActivity extends ListActivity {

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");
    private DatabaseReference friendRef = rootRef.child("friends");
    private final static String TAG = "queryResults: ";
    String globalUID = ""; //todo: jesus christ this shouldnt be global - instead pass it to our custom listview adapter without displaying it

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        final ListView l = (ListView) findViewById(android.R.id.list);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendFriendRequest();
            }
        });

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchForUsers(query);
        }


    }

    private void sendFriendRequest() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String myUid = mAuth.getCurrentUser().getUid();
        String requestedUid = globalUID;
        Friend friend = new Friend(myUid, requestedUid, "0");
        friendRef.push().setValue(friend);
        Toast.makeText(this, "Friend Request Sent", Toast.LENGTH_SHORT).show();
    }


    public void SearchForUsers(String query) {
        //final User usersReturned[] = new User[1];
        final ArrayList<String> userNamesReturned = new ArrayList<>();
        Query q = userRef.orderByChild("name").equalTo(query);
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                Log.v(TAG, user.getName());
                userNamesReturned.add(user.getName());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchableActivity.this, android.R.layout.simple_list_item_1, userNamesReturned);
                setListAdapter(adapter);
                globalUID = user.getUid();
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
