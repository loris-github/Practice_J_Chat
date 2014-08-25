package com.test.Chat;

import java.io.IOException;
import java.net.*;
import java.io.*;

public class ChatServer {

	boolean started = false;
	ServerSocket ss = null;
	
	public static void main(String[] args) {
		new ChatServer().start();
	}

	public void start(){
		try {
			ss = new ServerSocket(8888);
			started = true;
		} catch (BindException e){
			System.out.println("端口被占了 ╮(╯▽╰)╭");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {			
			while(started){
				Socket s = ss.accept();
				Client c = new Client(s);
				System.out.println("a client connected!");
				new Thread(c).start();

				//dis.close();
			}
		} catch (EOFException e) {
			//e.printStackTrace();
			System.out.println("Client closed");
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class Client implements Runnable{
		private Socket s;
		private DataInputStream dis = null;
		private boolean bConnected = false;
		public Client (Socket s){
			this.s = s;
			try {
				dis  = new DataInputStream(s.getInputStream());
				bConnected  = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try{
				while(bConnected){
					String str = dis.readUTF();
					System.out.println(str);
				}
			} catch (EOFException e) {
				//e.printStackTrace();
				System.out.println("Client closed");
			} catch (Exception e){
				e.printStackTrace();
			} finally{
				try{
					if(dis != null) dis.close();
					if(s != null) s.close();
				} catch(IOException e1){
					e1.printStackTrace();
				}
			}

			
		}
		
	}
}
