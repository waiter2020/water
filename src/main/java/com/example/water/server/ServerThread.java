package com.example.water.server;


public class ServerThread implements Runnable{
	
	private BlueDreamServer blueDreamServer = null;
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		blueDreamServer = new BlueDreamServer();
		blueDreamServer.run();
		
	}

}
