package com.localui;

import java.awt.Color;

public class UIInstance {

	private static UIInstance single_instance = null; 
	
	private UIInstance(){
		init();
	}
	
	private void init(){
		System.out.println("Starting UI container");
		FrameContainer fc = new FrameContainer("Henosisknot Chatbot");
//		fc.addPanel(new ChatDialog(200,200));
	}
	
	//Use the getInstance method to retrieve the class.
	public static UIInstance getInstance(){
		if(single_instance == null){
			single_instance = new UIInstance();
		}
		return single_instance;
	}
	
}
