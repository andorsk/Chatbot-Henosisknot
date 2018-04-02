package com.session;

import java.util.LinkedList;

import com.proto.gen.MessageOuterClass;

public interface SessionInterface {
	public abstract void insertMessage(MessageOuterClass.Message m);
}
