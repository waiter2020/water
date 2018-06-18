package com.example.water.server;

public class BlueDreamServerController {
	
	private MessageHandler msgHandler = null;
	
	public BlueDreamServerController() {
		System.out.println("BlueDreamServerController初始化");
		msgHandler = MessageHandler.getInstance();
	}
	
	public void sendToDevice(String message,String device_id) {
		System.out.println("message:"+message);
		msgHandler.sendToDevice(message, device_id);
		//BlueDreamServer.sendToDevice(message, device_id);
	}
}
