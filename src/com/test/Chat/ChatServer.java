package com.test.Chat;

import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane; 


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
		} catch (BindException e){
			JOptionPane.showMessageDialog(null, "端口被占了 ╮(╯▽╰)╭");
			System.exit(0);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		
		started = true;
		
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
				clients.remove(this);
				System.out.println("某人退了！");
				//e.printStackTrace();
			}
		}
		
		/*public void send(String str) throws IOException {
				dos.writeUTF(str);
		}*/
		
		@Override
		public void run() {
			Client c = null;		
			try{
				while(bConnected){
					String str = dis.readUTF();
					System.out.println(str);
					
					for(int i=0;i<clients.size();i++){
						c = clients.get(i);
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
					if(s != null) {
						s.close();
						s=null; //比较严格的写法，只有设置成空值之后垃圾收集器才会处理
					}
				} catch(IOException e1){
					e1.printStackTrace();
				}
			}
		}
	}
}
