package com.test.Chat;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.io.*;

public class ChatClient extends Frame {
	
	Socket s = null;
	DataOutputStream dos = null;
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
		
		this.setTitle("聊天室");
		this.setBackground(Color.DARK_GRAY);
		this.setResizable(false);
		
		tfTxt.addActionListener(new TFListener());
		
		this.addWindowListener(new WindowAdapter()	{
			
			@Override
			public void windowClosing(WindowEvent e){
				disconnect();
				System.exit(0);
			}		
		});
		
		connect();
		this.setVisible(true);
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
	
	private class TFListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String str = tfTxt.getText().trim();
			taContent.setText(str);
			tfTxt.setText("");
			try {
				//DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeUTF(str);
				dos.flush();
				//dos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	public void connect(){
		try {
			s = new Socket("127.0.0.1",8888);
			dos = new DataOutputStream(s.getOutputStream());
			System.out.println("connected!");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void disconnect(){
		try {
			dos.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		ChatClient cc = new ChatClient();
		cc.LuanchFrame();
	}
	
	
}

