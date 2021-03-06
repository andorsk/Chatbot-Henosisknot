package com.session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.UUID;

import com.proto.gen.MessageOuterClass;

import static org.reflections.util.ConfigurationBuilder.build;

public class SessionBase extends SessionInterface{

    HashMap<String, MessageOuterClass.Conversation> conversationMap = new HashMap<String,MessageOuterClass.Conversation>() ;
    HashSet<Integer> participants = new HashSet<Integer>();
    private long sessionStartTime;
    private final String SESSION_UUID = UUID.randomUUID().toString();

    public HashMap<String, MessageOuterClass.Conversation> getConversationMap(){
        return this.conversationMap;
    }

    public String getSessionUUID(){
        return this.SESSION_UUID;
    }
    @Override
	public void insertMessage(MessageOuterClass.Message m) {

	    //add Users to session if not present.
	    for (int id : m.getReceiversUseridsList()){
	        if(!participants.contains(id)){
	            participants.add(id);
            }
        }
        if(!participants.contains(m.getSenderUserid())){
                participants.add(m.getSenderUserid());
	    }

	    //Add message to dialog list.
        conversationMap.get(m.getConversationId()).getDialogList().add(m);
	}

	public void startSession(){
	    System.out.println("Starting session...");
        sessionStartTime = System.currentTimeMillis();
        if(conversationMap.isEmpty()){
            String id = UUID.randomUUID().toString();
            System.out.println("No conversations found. Loading conversation for client with id." + id);

            MessageOuterClass.Conversation conversation = MessageOuterClass.Conversation
                    .newBuilder()
                    .setStartTime(System.currentTimeMillis())
                    .setId(id)
                    .build();

            conversationMap.put(id, conversation);
        }
    }



}
