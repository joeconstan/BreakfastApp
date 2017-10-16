package com.arealbreakfast.breakfastapp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MessageAdapter extends BaseAdapter implements ListAdapter{

    private ArrayList<String> msgs = new ArrayList<>();
    private Context context;
    private ArrayList<Integer> keys = new ArrayList<>();

    public MessageAdapter(Context context, ArrayList<String> msgs, ArrayList<Integer> keys) {
        this.msgs = msgs;
        this.context = context;
        this.keys = keys;
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int position) {
        return msgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        //if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.messages_list_item, null);

            TextView textView = (TextView) view.findViewById(R.id.messages_list_item);
            textView.setText(getItem(position).toString());
            if(keys.get(position) == 1)
                textView.setGravity(Gravity.RIGHT);
            else
                textView.setGravity(Gravity.LEFT);
        //}
        return view;
    }
}
