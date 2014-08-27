package com.test.Chat;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;

import com.test.Chat.ChatServer.Client;

public class ChatClient extends Frame {
	
	Socket s = null;
	ServerSocket ss = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	boolean started = false;
	private boolean bConnected = false;
	
	public static final int CHAT_WIDTH = 300 ;
	public static final int CHAT_HEIGTH = 800 ;
	
	TextField tfTxt = new TextField();
	TextArea taContent = new TextArea();
	Thread tRecv = new Thread(new RecvThread());

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
		tRecv.start();
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
			String str = "涛曰："+'\n'+tfTxt.getText().trim()+'\n'+'\n';
			//taContent.setText(str);
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
			s = new Socket("10.7.1.172",8888);
			
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "服务器连接失败!");
			s = null;
			System.exit(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "服务器连接失败!");
			s = null;
			System.exit(0);
		}
		
		System.out.println("connected!");
		
		try {
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
		} catch (IOException e) {
			System.out.println("管道建立失败!");
		}
		
		bConnected = true;
	}
	public void disconnect(){
		try {
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//下边的语句会导致客户端不能关闭
		/*try {
			bConnected = false;
			tRecv.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {

		}*/
		
	}

	public static void main(String args[]){
		ChatClient cc = new ChatClient();
		cc.LuanchFrame();

	}
	
	class RecvThread implements Runnable {
	
		@Override
		public void run() {
			try{
				while(bConnected){
					String str = dis.readUTF();
					//taContent.setText(taContent.getText()+str+'\n');
					taContent.append(str);				
					taContent.setCaretPosition(taContent.getText().length()); //自动滚动到最新一行
				}
			} catch(SocketException e){
				System.out.println("客户端退出了！( ^_^ )/~~拜拜");			
			} catch (EOFException e){
				System.out.println("某客户端退了 ╮(╯▽╰)╭");
			} catch (IOException e) {
				e.printStackTrace();
			} 
			/*finally{
				try{
					if(dis != null) dis.close();
					if(dos != null) dos.close();
					if(s != null) s.close();
				} catch(IOException e1){
					e1.printStackTrace();
				}
			}*/
			
		}
		
	}

}

