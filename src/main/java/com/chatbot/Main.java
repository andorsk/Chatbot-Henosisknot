package com.chatbot;

import com.cli.CLI_Instance;
import com.localui.FrameContainer;
import com.localui.UIInstance;
import com.message.MessageHelpers;
import com.session.TemporarySession;
import com.training.ManualTrainer;

public class Main {

	public static void main(String args[]){
		start();
	}

	private static void start(){
	   // UIInstance.getInstance();

        CLI_Instance cli = new CLI_Instance();
        cli.start();
//        ManualTrainer m = new ManualTrainer();
//        m.train();

//		TemporarySession ts = new TemporarySession();
//		ts.insertMessage(MessageHelpers.buildMessage(1, "test text", 1));
	}
}
