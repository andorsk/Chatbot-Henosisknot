package com.api;

import com.message.MessageHelpers;
import com.nlp.StatementProcessingPipeline;
import com.proto.gen.MessageOuterClass;

/**
 * Messaging API is the main interface to receive/post messages from a source. Currently, we are
 * not using an API and bypassing it directly with listeners on the button, but ultimately the MessageAPI class will be responsible
 * for parsing the request and directing the request to the MessageController class, which will determine
 * what is the appropriate form of action.
 *
 * This will be on a thread listening, and so will run in the background to process requests.
 */
public class MessagingProtocol {

    /**
     * Receives a message. Returns a response.
     * @param message
     * @return
     */
    public static String generateResponse(MessageOuterClass.Message message, StatementProcessingPipeline spp){
        String response = spp.process(message);
        return response;
    }

    /**
     * Convert the message into an response and prepare for post.
     * @param msg
     */
    public static void respond(MessageOuterClass.Message msg){
        System.out.println("Responding and posting to " + msg.getConversationId() + " with text " + msg.getText());
    }

    //Post
    public static MessageOuterClass.Message post(String response){
        return null;
    }

}
