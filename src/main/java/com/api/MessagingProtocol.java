package com.api;

import com.config.PrimaryConfig;
import com.nlp.StatementProcessingPipeline;
import com.proto.gen.MessageOuterClass.Message;
import com.proto.gen.MessageOuterClass.Conversation;

import com.server.Server;
import com.session.SessionBase;

import javax.ws.rs.NotFoundException;

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
     * Receives a message. Returns a response. All responses are processed by the chatbot agent, therefore,
     * We give the chatbot agent a id of 0.
     * @param message
     * @return
     */
    public static Message generateResponseMessage(Message message, StatementProcessingPipeline spp) {
        String response = spp.process(message);
        Message msg = Message.newBuilder()
                .setText(response)
                .setCreationTime(System.currentTimeMillis())
                .setConversationId(message.getConversationId())
                .setSenderUserid(PrimaryConfig.DEFAULT_CHATBOT_USERID)
                .build();
        return msg;
    }

    /**
     * Convert the message into an response and prepare for post.
     * @param msg
     */
    public static void respond(Message msg, Server server){
        updateConversation(getConversation(msg, server), msg);
    }

    private static Conversation getConversation(Message msg, Server server){
        Conversation convo = null;
        for(SessionBase sess : server.getSessionList()){
            if(sess.getConversationMap().containsKey(msg.getConversationId())){
                convo = sess.getConversationMap().get(msg.getConversationId());
            } else{
                throw new NotFoundException("Conversation not found. Counld not update the convo");
            }
        };
        return convo;
    }

    private static Conversation updateConversation(Conversation convo, Message msg){
        return convo.toBuilder().addDialog(msg).build();
    }

    //Post
    public static Message post(String response){
        return null;
    }


    public static void responseController(String message){}{


    }

}
