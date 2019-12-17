package edu.uga.cs.textapp;

import java.util.Date;


/**
 *
 * A class that holds an Inbox (1 on 1) message
 *
 */
public class InboxMessage {

    //All the information that needs to be stored for an inbox message
    private String message;
    private String in;
    private String out;
    private long time;

    /**
     * A constructor where everything is initialized here
     */
    public InboxMessage(String message, String out, String in) {
        this.message = message;
        time = new Date().getTime();
        this.out = out;
        this.in = in;
    }

    public InboxMessage(){}

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public void setIn(String in) { this.in = in; }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getOut() {
        return out;
    }

    public String getIn() {
        return in;
    }

    public long getTime() {
        return time;
    }

    public String toString(){
        return "\nFrom: "+out +"\nTo: "+in+"\nTime: "+time +"\nMessage: "+message;
    }

    public boolean equals(InboxMessage message){

        return this.message.equals(message.getMessage()) && this.out.equals(message.getOut()) && this.in.equals(message.getIn()) && this.time == message.getTime();
    }
}
