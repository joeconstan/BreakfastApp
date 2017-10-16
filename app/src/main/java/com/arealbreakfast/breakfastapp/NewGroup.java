package com.arealbreakfast.breakfastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class NewGroup extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ArrayList<String> groupuids = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        Button createButton = (Button) findViewById(R.id.newgroupcreatebutton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ComposeMessage.class);
                EditText groupName = (EditText) findViewById(R.id.newgroupnameet);
                String name = groupName.getText().toString();
                intent.putExtra("recp", name);
                intent.putExtra("groupuids", groupuids);
                intent.putExtra("membercount", groupuids.size());
                intent.putExtra("uid1", mAuth.getCurrentUser().getUid());
                intent.putExtra("isgroup", 1);
                startActivity(intent);
            }
        });
    }
}
