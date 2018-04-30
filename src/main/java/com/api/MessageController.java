package com.api;

import com.message.MessageHelpers;
import com.nlp.StatementProcessingPipeline;
import com.proto.gen.MessageOuterClass;
import com.server.Server;

import java.util.function.Function;

/**
 * Message Controller takes in a message and delegates proper action.
 */
public class MessageController {

    public static void processMessage(MessageOuterClass.Message message, Server server){
        //For now, default each message to utterance.
        message = message.toBuilder().setMessageQualifier(MessageOuterClass.MessageQualifier.UTTERANCE).build();

        if(message.getMessageType() == MessageOuterClass.MessageType.RECIEVE && message.getMessageQualifier() == MessageOuterClass.MessageQualifier.UTTERANCE){
            StatementProcessingPipeline spp = server.getStatementProcessingPipeline();
            MessageOuterClass.Message response = MessageHelpers.prepareMessage(MessagingProtocol.generateResponse(message, spp) ,server);
            MessagingProtocol.respond(response);
        }
    }

    public static void processCommand(String command){

        class ReflectiveCommand implements Command{

            String command;
            Class aClass;
            Function mFunction;
            Object[] mArgs;

            public ReflectiveCommand(Class cls , String cmd){
                this.aClass = aClass;
                this.command = cmd;
            }

            public ReflectiveCommand(String cmd){
                this.command = cmd;
            }

            public ReflectiveCommand(Function f, Object... args){
                mFunction = f;
                mArgs = args;

            }

            public Object run(){
                if(this.aClass != null){
                    return null;
                }

                if(this.command != null && this.aClass == null){
                    return null;
                }
                if(this.mFunction != null){
                    return null;
                }

                return null;
            }
        }
        new ReflectiveCommand(command).run();
    }
    /**
     * Takes a JSON message and converts it to protobuf format.
     * @param input
     * @param server Server which contains the pipeline
     */
    public static void processInput(String input, Server server){
        //if message is an http message decode it first
        if(isPOSTRequest(input)){
            input = getPostBody(input);
        }
        //if message is a direct json message then build it into a builder
        MessageOuterClass.Message msg = MessageHelpers.prepareMessage(input, server);
        msg = msg.toBuilder().setMessageType(MessageOuterClass.MessageType.RECIEVE).build();
        processMessage(msg,server);
    }

    private static boolean isPOSTRequest(String input){
        return false;
    }
    //Parse post request
    private static String getPostBody(String input){
        return input;
    }

}
