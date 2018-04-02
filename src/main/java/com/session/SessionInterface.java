package com.session;

import java.util.LinkedList;

import com.proto.gen.MessageOuterClass;

public abstract class SessionInterface {

    abstract void insertMessage(MessageOuterClass.Message m);

}
