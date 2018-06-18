package com.example.water.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class BlueDreamServer {
	//private Socket socket; 
	private ServerSocket server;
	private ServerSocket appServer;

	public BlueDreamServer(){
		try {
			server = new ServerSocket(5432);
			//appServer = new ServerSocket(26532);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		System.out.println("服务器开始运行");
		while(true){
			try {				
				Socket socket = server.accept();
				socket.setKeepAlive(true);
				System.out.println("新连接接入"+socket.getInetAddress()+":"+socket.getPort()+"  ");
				Thread t = new Thread(new TCPServerThread(socket));
				t.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
