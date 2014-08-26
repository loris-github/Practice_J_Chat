package com.test.Chat;

import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {

	boolean started = false;
	ServerSocket ss = null;
	List<Client> clients = new ArrayList<Client>();
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
				clients.add(c);
				System.out.println("a client connected! O(∩_∩)O~");
				new Thread(c).start();
			}
		} catch (EOFException e) {
			System.out.println("Client closed ( ^_^ )/~~拜拜");
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class Client implements Runnable{
		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean bConnected = false;
		public Client (Socket s){
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected  = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void send(String str){
			try {
				dos.writeUTF(str);
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
					
					for(int i=0;i<clients.size();i++){
						Client c = clients.get(i);
						c.send(str);
					}
					
					//以下两种方式需要锁定 所以效率低下，弃用；
					
					/*for(Iterator<Client> it = clients.iterator();it.hasNext();){
						Client c = it.next();
						c.send (str);
					}*/
					
					/*Iterator<Client> it = clients.iterator();
					while(it.hasNext()){
						Client c = it.next();
						c.send(str);
					}*/
					
					
				}
			} catch (EOFException e) {
				System.out.println("客户端那边断鸟 ╮(╯▽╰)╭");
			} catch (Exception e){
				e.printStackTrace();
			} finally{
				try{
					if(dis != null) dis.close();
					if(dos != null) dos.close();
					if(s != null) s.close();
				} catch(IOException e1){
					e1.printStackTrace();
				}
			}
		}
	}
}
