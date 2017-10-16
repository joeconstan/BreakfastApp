package com.arealbreakfast.breakfastapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Settings extends AppCompatActivity {
    public static final int GET_FROM_GALLERY = 3;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private Uri selectedImage;


    private static final String TAG = "cool: ";

    //lets the user select a pciture from galery and sets it as the user's pic in db
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            ImageButton ib = (ImageButton) findViewById(R.id.settingsImageButton); //make final?
            try {
                final Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ib.setImageBitmap(bm);

                String userUid = mAuth.getCurrentUser().getUid();

                UploadTask uploadTask = mStorageRef.child(userUid).putFile(selectedImage);
                uploadTask
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.v(TAG, "image uploaded!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

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
    protected void onResume() {
        super.onResume();


        SharedPreferences settings = getSharedPreferences("settings",
                Context.MODE_PRIVATE);
        String colorId = settings.getString("backgroundcolor", "#FFFFFF");
        if (!colorId.equals("")) {
            RelativeLayout settingslayout = (RelativeLayout) findViewById(R.id.settingslayout);
            settingslayout.setBackgroundColor(Color.parseColor(colorId));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences settings = getSharedPreferences("settings",
                Context.MODE_PRIVATE);
        String colorId = settings.getString("backgroundcolor", "#FFFFFF");
        if (!colorId.equals("")) {
            RelativeLayout settingslayout = (RelativeLayout) findViewById(R.id.settingslayout);
            settingslayout.setBackgroundColor(Color.parseColor(colorId));
        }
        EditText username = (EditText) findViewById(R.id.settingsName);
        username.setText(mAuth.getCurrentUser().getDisplayName());
        final ImageButton imageButton = (ImageButton) findViewById(R.id.settingsImageButton);


        TextView backgroundtv = (TextView) findViewById(R.id.settingsChangeBackgroundtv);
        backgroundtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BackgroundColor.class);
                startActivity(intent);
            }
        });
        TextView passwordtv = (TextView) findViewById(R.id.settingsChangePasswordtv);
        passwordtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference pathReference = storageReference.child(mAuth.getCurrentUser().getUid());

                    // Load the image using Glide
                    Glide.with(Settings.this)
                            .using(new FirebaseImageLoader())
                            .load(pathReference)
                            .into(imageButton);

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
