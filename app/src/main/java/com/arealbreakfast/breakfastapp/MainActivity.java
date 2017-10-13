package com.arealbreakfast.breakfastapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");

    private FirebaseAuth.AuthStateListener mAuthListener;
    private final static String TAG = "here: ";

    //todo : only ask for username if registering
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* if (mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(this, MainFragmentPager.class);
            startActivity(intent);
        }*/

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String id = sharedPref.getString("uid", "");
        if (!id.equals("")) {
            final EditText un = (EditText) findViewById(R.id.usernameET);
            final EditText em = (EditText) findViewById(R.id.emailET);
            final EditText ps = (EditText) findViewById(R.id.passwordET);
            //query with uid for login details and set the edittexts to these so that Login(view) will read them and proceed -
            // theres a better solution though, I'm sure. something like php session with uid?
            Query q = userRef.orderByChild("uid").equalTo(id);
            q.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        un.setText(user.getName());
                        em.setText(user.getEmail());
                        //Login(new View(MainActivity.this));

                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void Register(final View view) {
        final EditText em = (EditText) findViewById(R.id.emailET);
        EditText ps = (EditText) findViewById(R.id.passwordET);
        final EditText un = (EditText) findViewById(R.id.usernameET);
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String email = em.getText().toString();
        String password = ps.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "register failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String nameUser = un.getText().toString();
                            String email = em.getText().toString();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nameUser)
                                    .build();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            user.updateProfile(profileUpdate);


                            //add user to database
                            User us = new User(nameUser, email, uid);
                            userRef.push().setValue(us);


                            //commit to sharedpref
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("uid", uid);
                            editor.apply();


                            Intent intent = new Intent(view.getContext(), MainFragmentPager.class);
                            startActivity(intent);
                        }
                    }
                });
    }


    public void Login(final View view) {
        final EditText un = (EditText) findViewById(R.id.usernameET);
        EditText em = (EditText) findViewById(R.id.emailET);
        EditText ps = (EditText) findViewById(R.id.passwordET);
        String email = em.getText().toString();
        String password = ps.getText().toString();
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        //todo: show a toast if device offline - rn it does nothing if you try to log in while offline
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "login failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String nameUser = un.getText().toString();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nameUser)
                                    .build();
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.updateProfile(profileUpdate);

                            String uid = user.getUid();
                            //commit to sharedpref
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("uid", uid);
                            editor.apply();

                            Intent intent = new Intent(view.getContext(), MainFragmentPager.class);
                            startActivity(intent);
                        }

                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }


}
