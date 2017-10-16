package com.arealbreakfast.breakfastapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class BackgroundColor extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_color);


        Button saveButton = (Button) findViewById(R.id.backgroundcolorbuttonsave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.hexcodebackgroundet);
                SharedPreferences prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("backgroundcolor", et.getText().toString());
                editor.apply();
                //Intent intent = new Intent(v.getContext(), Settings.class);
                //startActivity(intent);
                BackgroundColor.super.onBackPressed();


            }
        });

    }
}
