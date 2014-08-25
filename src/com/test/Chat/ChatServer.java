package com.test.Chat;

import java.io.IOException;
import java.net.*;
import java.io.*;

public class ChatServer {

	public static void main(String[] args) {
		boolean started = false;
		ServerSocket ss = null;
		Socket s = null;
		DataInputStream dis = null;
		
		try {
			ss = new ServerSocket(8888);
		} catch(BindException e){
			System.out.println("端口被占了 ╮(╯▽╰)╭");
		}catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {			
			started = true;
			while(started){
				boolean bConnected = false;
				s = ss.accept();
				System.out.println("a client connected!");
				bConnected = true;				
				dis = new DataInputStream(s.getInputStream());				
				while(bConnected){
					String str = dis.readUTF();
					System.out.println(str);
				}
				//dis.close();
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
