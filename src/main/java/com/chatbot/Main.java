package com.chatbot;

import com.localui.FrameContainer;
import com.localui.UIInstance;
import com.message.MessageHelpers;
import com.session.TemporarySession;

public class Main {

	public static void main(String args[]){
		start();
	}

	private static void start(){
	    UIInstance.getInstance();
       // System.out.println(UIInstance.getInstance().getFrameContainer().getDialogBox());

		TemporarySession ts = new TemporarySession();
		ts.insertMessage(MessageHelpers.buildMessage(1, "test text", 1));
	}
}
