package com.api;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.message.MessageHelpers;
import com.nlp.StatementProcessingPipeline;
import com.proto.gen.MessageOuterClass;
import com.server.Server;
import org.omg.CORBA.portable.ResponseHandler;

import javax.xml.ws.http.HTTPException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
/**
 * Message Controller takes in a message and delegates proper action.
 */
public class MessageController {

    public static void processIncomingMessage(MessageOuterClass.Message message, Server server){
        StatementProcessingPipeline spp = server.getStatementProcessingPipeline();
        MessagingProtocol.respond(MessagingProtocol.generateResponseMessage(message, spp));
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
     * Primary method responsible for parsing string input and converting it to a message. If its
     * from the client then no http response. if its an http response needs to create session infomration and
     * creditials. etgc.
     * @param input
     * @param server Server which contains the pipeline
     */
    public static void processInput(String input, Server server) throws HTTPException {
        //if message is an http message decode it first
        System.out.println("Trying to interpret " + input + " at server " + server);
        if(isPOSTRequest(input)){
            input = getPostBody(input);
        }

        try {
            MessageOuterClass.Message msg = MessageOuterClass.Message.parseFrom(input.getBytes());
            msg = setMessageDefaults(msg);
            if(msg.getMessageType().equals(MessageOuterClass.MessageType.RECIEVE)){
                processIncomingMessage(msg, server);
            }

        } catch (InvalidProtocolBufferException e) {
            System.err.println("Failed to convert message to InvalidProtocolBufferException");
            e.printStackTrace();
        }
    }

    /**
     * TODO: Review. See if I can dynamically get msg Headers.
     * @param msg
     * @return
     */
    private static MessageOuterClass.Message setMessageDefaults(MessageOuterClass.Message msg){
        HashSet<String> fields = new HashSet<String>();
        if(msg.getMessageType() == null) {
            fields.add("msgType");
        }

        if(fields.size() > 0){
            Message.Builder m = msg.toBuilder();
            for(String field: fields){
                if(field.equals("msgType")){
                    ((MessageOuterClass.Message.Builder) m).setMessageType(MessageOuterClass.MessageType.RECIEVE);
                }
            }
            msg = ((MessageOuterClass.Message.Builder) m).build();
        }
        return msg;
    }

    private static boolean isPOSTRequest(String input){
        return false;
    }
    //Parse post request
    private static String getPostBody(String input){
        return input;
    }

}
