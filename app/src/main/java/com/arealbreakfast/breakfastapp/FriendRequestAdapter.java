package com.arealbreakfast.breakfastapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;


public class FriendRequestAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<>();
    private Context context;
    private String key;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference friendRef = rootRef.child("friends");


    public FriendRequestAdapter(ArrayList<String> list, Context context, String key) {
        this.list = list;
        this.context = context;
        this.key = key;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).getId();
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.friends_list_item, null);
            Button acceptButton = (Button) view.findViewById(R.id.acceptButton);
            Button rejectButton = (Button) view.findViewById(R.id.rejectButton);
            TextView textView = (TextView) view.findViewById(R.id.friends_list_item);
            textView.setText(getItem(position).toString());
            //onclick: reload page so friend request disapears, & set status to 1
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendRef.child(key).child("status").setValue("1");
                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return view;
    }


}
