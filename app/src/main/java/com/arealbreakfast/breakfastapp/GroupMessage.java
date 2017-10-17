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
    public String groupName;


    public GroupMessage(String messageText, String messageUser, ArrayList<String> messageRecipient, String groupName) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageRecipient = messageRecipient;
        this.messageTime = new Date().getTime();
        this.read = new ArrayList<>();
        this.groupName = groupName;
        for (int i = 0; i < messageRecipient.size(); i++) {
            this.read.add(0);
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

    public String getGroupName() {
        return groupName;
    }
}
