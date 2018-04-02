package com.session;

import java.util.LinkedList;

import com.proto.gen.MessageOuterClass;

public class SessionBase extends SessionInterface{
	LinkedList<MessageOuterClass.Message> dialog = new LinkedList<MessageOuterClass.Message>();

	@Override
	public void insertMessage(MessageOuterClass.Message m) {
		dialog.add(m);
	}

}
