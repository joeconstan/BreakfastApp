package com.arealbreakfast.breakfastapp;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchableActivity extends ListActivity {

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");
    private final static String TAG = "queryResults: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchForUsers(query);
        }

    }


    public void SearchForUsers(String query) {
        Log.v(TAG, userRef.orderByChild("name").equalTo(query).toString());
        ListView lv = (ListView) findViewById(android.R.id.list);
        
    }
}
