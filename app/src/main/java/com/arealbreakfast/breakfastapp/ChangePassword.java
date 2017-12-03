package com.arealbreakfast.breakfastapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangePassword extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        Button saveButton = (Button) findViewById(R.id.newpasswordbutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newpswd = (EditText) findViewById(R.id.newpasswordet);
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                firebaseUser.updatePassword(newpswd.getText().toString());
                ChangePassword.super.onBackPressed();
            }
        });

    }
}

