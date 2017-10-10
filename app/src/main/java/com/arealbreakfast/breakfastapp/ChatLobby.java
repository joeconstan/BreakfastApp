package com.arealbreakfast.breakfastapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ChatLobby extends AppCompatActivity {
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private String fullmsgContent = "";
    private static final String TAG = "messageText: ";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_lobby);
        //todo: bring up soft keyboard

        final EditText msgET = (EditText) findViewById(R.id.messageET);
        final Button sendButton = (Button) findViewById(R.id.sendButton);
        final TextView msgArea = (TextView) findViewById(R.id.msgViewArea);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msgET.getText().toString();
                msg+="\n";
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String userId = mAuth.getCurrentUser().getUid();
                FirebaseUser user = mAuth.getCurrentUser();
                String name = user.getDisplayName();
                

                msgET.setText("");

                Message message = new Message(msg, name);
                rootRef.child("messages").push().setValue(message);


            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        rootRef.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Message m = dataSnapshot.getValue(Message.class);
                    String mess = m.getMessageText();
                    Log.v(TAG, mess);
                    String usr = m.getMessageUser();
                    fullmsgContent += (usr + ": " + mess);
                    displayMessages();

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

    private void displayMessages() {
        TextView msgArea = (TextView) findViewById(R.id.msgViewArea);
        msgArea.setText(fullmsgContent);
    }
}
