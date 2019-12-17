package edu.uga.cs.textapp;

import java.util.Date;


/**
 *
 * A class that holds a Chat room message
 *
 */

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;

    public ChatMessage(String messageText, String messageUser) {
        /**
         *
         * Constructor method that initiliazes all the message aspects
         *
         */

        this.messageText = messageText;
        this.messageUser = messageUser;
        messageTime = new Date().getTime();
    }

    public ChatMessage() {

    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
