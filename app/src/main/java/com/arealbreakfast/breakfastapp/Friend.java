package com.arealbreakfast.breakfastapp;


public class Friend {

    private String uid1;
    private String uid2;
    private int status;

    public Friend() {

    }


    public Friend(String uid1, String uid2, int status) {
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

    public int getStatus() {
        return status;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
