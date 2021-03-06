package com.localui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Frame container is a container JFrame.
 */

public class FrameContainer extends JFrame {
	
	int mWidth, mHeight;
	private static final long serialVersionUID = 4179673315950936919L;
	static GraphicsConfiguration gc;
	private ChatDialog mChatDialog;

	public FrameContainer(){
		new FrameContainer("Frame Container");
	}

	public FrameContainer(String s){
		this(100, 100, s);
	}
	
	public FrameContainer(int width, int height, String s){
		super(gc);
		this.mWidth = width;
		this.mHeight = height;
		setTitle(s);
		init();
    }

	public ChatDialog getDialogBox(){
		return this.mChatDialog;
	}

	private void init(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        setLocation(0,0);
		setSize(this.mWidth, this.mHeight);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().setBackground(Color.WHITE);
		
		//Add input dialogs
		ChatDialog cd = new ChatDialog(200,200);
		addPanel(cd);
		this.mChatDialog = cd;

		setResizable(true);
		pack();
		setVisible(true);
	}

	
	public void addPanel(JPanel panel){
		getContentPane().add(panel);
	}
}
	
	

