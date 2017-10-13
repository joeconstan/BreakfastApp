package com.arealbreakfast.breakfastapp;


public class Friend {

    private String uid1; //requester
    private String uid2; //requestee
    private String status;

    public Friend() {

    }


    public Friend(String uid1, String uid2, String status) {
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.status = status;
    }


    public String getUid1() {
        return uid1;
    }

    public String getUid2() {
        return uid2;
    }

    public String getStatus() {
        return status;
    }

}
