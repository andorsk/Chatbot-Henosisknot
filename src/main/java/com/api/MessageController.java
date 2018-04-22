package com.api;

import com.proto.gen.MessageOuterClass;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Message Controller takes in a message and delegates proper action.
 */
public class MessageController {

    public static void processMessage(MessageOuterClass.Message message){

        if(message.getMessageType() == MessageOuterClass.MessageType.RECIEVE && message.getMessageQualifier() == MessageOuterClass.MessageQualifier.UTTERANCE){
            //get Session and then conversation from session
            String conversationId = message.getConversationId();
            //upload message to session conversation
            //generate response

        }
    }

}
