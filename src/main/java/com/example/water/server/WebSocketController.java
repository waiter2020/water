package com.example.water.server;

import java.io.IOException;

public class WebSocketController {
	public WebSocketController(){}
	 public void sendMessageToWeb(String message) throws IOException{
		  //群发消息  
		 System.out.println("发送消息给web"+MyWebSocket.getWebSocketSet().size());
		
	        for(MyWebSocket item: MyWebSocket.getWebSocketSet()){               
	            try {  
	            	System.out.println(item);
	                item.sendMessage(message);  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	                continue;  
	            }  
	        }  
	 }

}
