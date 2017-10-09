package com.arealbreakfast.breakfastapp;


import java.util.Date;

public class Message {

    public String messageText;
    public String messageUser;
    public Long messageTime;

    public Message(String messageText, String messageUser){
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = new Date().getTime();
    }
    public Message(){

    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }


}
