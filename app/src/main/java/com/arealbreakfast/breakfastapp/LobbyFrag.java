package com.arealbreakfast.breakfastapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LobbyFrag extends android.support.v4.app.Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_lobby, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabl);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: pull up friends list and make clickable. if you click on one, opens up new message screen
                Intent intent = new Intent(view.getContext(), ComposeRecipient.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
