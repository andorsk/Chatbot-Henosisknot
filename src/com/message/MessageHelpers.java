package com.message;

import com.proto.gen.MessageOuterClass;

public class MessageHelpers {

	public static MessageOuterClass.Message buildMessage(long creation_time, String text, int userid){
		MessageOuterClass.Message.Builder m = MessageOuterClass.Message.newBuilder();
		m.setCreationTime(creation_time);
		m.setText(text);
		m.setSenderUserid(userid);
		return m.build();
	}
}
