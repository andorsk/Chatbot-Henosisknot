package com.server;


import javafx.util.Pair;

/**
 * Abstract class for a sender. All senders must have a session.
 */
public abstract class Sender {
    private Integer mID = null;

    public abstract Pair<String, String> getConversationId();

    public void setSenderID(int id){
        this.mID = id;
    }

    public int getSenderID() throws NullPointerException{
        if(this.mID == null){
            throw new NullPointerException();
        }
        System.out.println("Sender id is "+ this.mID);
        return this.mID;
    }
}
