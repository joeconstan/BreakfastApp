package com.arealbreakfast.breakfastapp;


import java.util.ArrayList;
import java.util.Date;

public class GroupMessage {
    //0-unread
    //1-read
    public String messageText;
    public String messageUser;
    public Long messageTime;
    public ArrayList<String> messageRecipient;
    public ArrayList<Integer> read;


    public GroupMessage(String messageText, String messageUser, ArrayList<String> messageRecipient) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageRecipient = messageRecipient;
        this.messageTime = new Date().getTime();
        for (int i = 0; i < read.size(); i++) {
            this.read.set(i, 0);
        }
    }

    public GroupMessage() {

    }

    public ArrayList<String> getMessageRecipient() {
        return messageRecipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public ArrayList<Integer> getRead() {
        return read;
    }

    public void setRead(ArrayList<Integer> read) {
        this.read = read;
    }


}
