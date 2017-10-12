package com.arealbreakfast.breakfastapp;


import java.util.Date;

public class Message {

    public String messageText;
    public String messageUser;
    public Long messageTime;
    public String messageRecipient;



    public Message(String messageText, String messageUser, String messageRecipient){
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageRecipient = messageRecipient;
        this.messageTime = new Date().getTime();
    }
    public Message(){

    }

    public String getMessageRecipient() {
        return messageRecipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }


}
