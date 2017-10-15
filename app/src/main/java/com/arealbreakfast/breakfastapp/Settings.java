package com.arealbreakfast.breakfastapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.FileNotFoundException;
import java.io.IOException;


public class Settings extends AppCompatActivity {
    public static final int GET_FROM_GALLERY = 3;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");

    //lets the user select a pciture from galery and sets it as the user's pic in db
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            ImageButton ib = (ImageButton) findViewById(R.id.settingsImageButton); //make final?
            try {
                final Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ib.setImageBitmap(bm);
                Query q = userRef.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());
                q.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) { //todo: not working cuz async?
                        if (dataSnapshot.exists()) {
                            User user = dataSnapshot.getValue(User.class);
                            user.setPicture(bm);
                        }
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
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText username = (EditText) findViewById(R.id.settingsName);
        username.setText(mAuth.getCurrentUser().getDisplayName());
        ImageButton imageButton = (ImageButton) findViewById(R.id.settingsImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });


        ImageButton friend_request = (ImageButton) findViewById(R.id.toolbar_friend_request_button);
        friend_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FriendRequest.class);
                startActivity(intent);
            }
        });

        Button saveButton = (Button) findViewById(R.id.settingsSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainFragmentPager.class);
                //todo: update changes - display name, name in realtime db, pro pic, etc.
                startActivity(intent);
            }
        });

        Query q = userRef.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    User u = dataSnapshot.getValue(User.class);
                    ImageButton settingsImageButton = (ImageButton) findViewById(R.id.settingsImageButton);
                    settingsImageButton.setImageBitmap(u.getPicture());
                }
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
