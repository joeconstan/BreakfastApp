package com.arealbreakfast.breakfastapp;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ConversationListAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> convos = new ArrayList<>();
    private Context context;
    private ArrayList<String> uids;


    public ConversationListAdapter(Context context, ArrayList<String> convos, ArrayList<String> uids) {
        this.convos = convos;
        this.context = context;
        this.uids = uids;
    }

    @Override
    public int getCount() {
        return convos.size();
    }

    @Override
    public Object getItem(int position) {
        return convos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.conversations_list_item, null); //todo: get user pics and add them to the view

            TextView textView = (TextView) view.findViewById(R.id.conversations_list_item);
            textView.setText(getItem(position).toString());

            ImageView imageView = (ImageView) view.findViewById(R.id.conversationpicIV);
            ArrayList<String> pics = new ArrayList<>();
            //get pics by uid from storage
            for (int i = 0; i < getCount(); i++) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference pathReference = storageReference.child(uids.get(i));
                Glide.with(view.getContext()) //todo: view.getcontext may be the wrong context
                        .using(new FirebaseImageLoader())
                        .load(pathReference)
                        .into(imageView);
            }
        }
        return view;
    }


}
