package com.arealbreakfast.breakfastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;



public class ComposeMessage extends BaseToolbarActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);



        Intent intent = getIntent();
        String recp = intent.getStringExtra("recp");

        //TextView recptv = (TextView) findViewById(R.id.recp_tv);
        //recptv.setText(recp);
        //android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.custom_toolbar);
        //setSupportActionBar(myToolbar);
        //myToolbar.setTitle(recp);

    }
}
