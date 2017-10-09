package com.arealbreakfast.breakfastapp;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = rootRef.child("users");

    private FirebaseAuth.AuthStateListener mAuthListener;
    private final static String TAG = "here: ";

    //todo : use shared preferences to remember login info
    //todo : only ask for username if registering
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                // ...
            }
        };
    }

    public void Register(final View view) {
        EditText em = (EditText) findViewById(R.id.emailET);
        EditText ps = (EditText) findViewById(R.id.passwordET);
        final EditText un = (EditText) findViewById(R.id.usernameET);

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
                        }
                        else{
                            String nameUser = un.getText().toString();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nameUser)
                                    .build();
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.updateProfile(profileUpdate);
                            Intent intent = new Intent(view.getContext(), FindContacts.class);
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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "login failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String nameUser = un.getText().toString();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nameUser)
                                    .build();
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.updateProfile(profileUpdate);
                            Intent intent = new Intent(view.getContext(), FindContacts.class);
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
    public void onResume(){
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }


}
