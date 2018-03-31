package com.localui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class TextInputDialog extends JTextArea {
	
	private static final long serialVersionUID = -4590965018753397444L;
	private static final String default_text = "Type here...";
	private boolean mUntouched = true;  
	
	public TextInputDialog(int rows, int columns){
		super(rows, columns);
		setBackground(Color.WHITE);
		setEditable(true);
		setWrapStyleWord(true);
		setLineWrap(true);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setText(default_text);
        setListeners();
	}	
	
	private void setListeners(){
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (e.getButton() == MouseEvent.BUTTON1 && mUntouched) {
					setText("");
					mUntouched = false;
				} 
			}
		});
	}
	
}
