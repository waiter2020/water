package com.example.water.server;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketPool {
	
	private ConcurrentHashMap<String,Socket> socketMap = new ConcurrentHashMap<>();
	private static SocketPool instance = new SocketPool();  
	
	private SocketPool(){
		
	}
	
	public static SocketPool getInstance(){

        return instance;
		
	}
	
	public 	Socket getSocket(String key){
		return socketMap.get(key);
		
	}
	
	public void addSocket(String key,Socket socket){
		socketMap.put(key, socket);
	}
	
	public void deleteSocket(String key){
		if(socketMap.containsKey(key))
			socketMap.remove(key);
	}
	
	public void printSocketPool(){
		for(Map.Entry<String,Socket> map:socketMap.entrySet())
			System.out.println(map.getKey());
	}
	
	public int getSocketCounts(){
		return socketMap.size();
	}

}
