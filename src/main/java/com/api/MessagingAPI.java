package com.api;

import com.proto.gen.MessageOuterClass;

/**
 * Messaging API is the main interface to receive/post messages from a source. Currently, we are
 * not using an API and bypassing it directly with listeners on the button, but ultimately the MessageAPI class will be responsible
 * for parsing the request and directing the request to the MessageController class, which will determine
 * what is the appropriate form of action.
 *
 * This will be on a thread listening, and so will run in the background to process requests.
 */
public class MessagingAPI {

    public static void recieve(MessageOuterClass.Message message){
        //Send text to the Process Engine Thread
    }

    //Post
    public static void post(MessageOuterClass.Message message){

    }
}
