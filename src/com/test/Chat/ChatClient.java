package com.test.Chat;

import java.awt.*;
import java.awt.event.*;

public class ChatClient extends Frame {
	
	public static final int CHAT_WIDTH = 300 ;
	public static final int CHAT_HEIGTH = 800 ;
	TextField tfTxt = new TextField();
	TextArea taContent = new TextArea();
	
	
	
	
	public void LuanchFrame(){
		this.setLocation(300,600);
		this.setSize(CHAT_WIDTH,CHAT_HEIGTH);
		add(tfTxt,BorderLayout.SOUTH);
		add(taContent,BorderLayout.NORTH);
		pack();
		
		this.setTitle("¡ƒÃÏ “");
		this.setBackground(Color.DARK_GRAY);
		this.setVisible(true);
		this.setResizable(false);
		
		this.addWindowListener(new WindowAdapter()	{
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}		
		});
		
		
		new Thread(new PaintThread()).start();
		
	}
	
	private class PaintThread implements Runnable{
		public void run(){
			while(true){
				repaint();
				try{
					Thread.sleep(50);
				} catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	public static void main(String args[]){
		ChatClient cc = new ChatClient();
		cc.LuanchFrame();
	}
	
	
}

