package com.message;

import com.proto.gen.MessageOuterClass;
import com.server.Sender;

public class MessageHelpers {

	public static  MessageOuterClass.Message prepareMessage(String message, Sender sender){
		return MessageOuterClass.Message.newBuilder()
				.setConversationId(sender.getConversationId().getValue())
				.setText(message)
				.setCreationTime(System.currentTimeMillis())
				.setSenderUserid(sender.getSenderID())
				.build();

	}
}
