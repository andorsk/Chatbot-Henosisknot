package com.localui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * Chat dialog is a basic test dialog to with a basic interface and JFrame
 * @author andor
 *
 */
public class ChatDialog extends JPanel {

	private static final long serialVersionUID = -7692842795130305280L;
	private int mWidth;
	private int mHeight;

	public ChatDialog(int width, int height){
		System.out.println("Creating chat dialog");
		this.mWidth = width;
		this.mHeight = height;
		init();
	}
	
	private void init(){		
		setSize(this.mWidth, this.mHeight);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.black));

		JTextArea textArea = new JTextArea(20, 40);
		textArea.setName("Chat");
		textArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(textArea); 	
		textArea.setEditable(false);
		
		add(textArea);
		TextInputDialog ti = new TextInputDialog(2, 20);
		add(ti);
		
		JButton jbutton = new JButton("Enter");
		jbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { 
				ti.getText();
			 } 

		});
		
        add(jbutton);
	}
}
