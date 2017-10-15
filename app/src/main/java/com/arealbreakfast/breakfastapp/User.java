package com.arealbreakfast.breakfastapp;


import android.graphics.Bitmap;

public class User {

    private String name;
    private String email;
    private String uid;
    private Bitmap picture;


    public User() {
    }


    public User(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
