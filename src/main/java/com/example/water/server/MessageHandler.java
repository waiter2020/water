package com.example.water.server;

import com.example.water.model.EquipmentInfo;
import com.example.water.service.EquipmentInfoService;
import com.example.water.service.WaterConditionService;

import com.example.water.utils.JPush;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class MessageHandler {
	
	private static MessageHandler msgHandler = new MessageHandler();;
	private SocketPool socketPool = null;
	private JPush jPush = null;
	private PrintWriter pWriter;
	@Autowired
	private WebSocketController webSocketController;
	@Autowired
	private EquipmentInfoService equipmentInfoService ;
	@Autowired
	private WaterConditionService waterConditionService;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageHandler(){
		socketPool = SocketPool.getInstance();
		jPush = JPush.getInstance();

	}
	
	public static MessageHandler getInstance(){

		return msgHandler;
	}
	
	public void addDevice(Socket clientSocket, String recvStr) {
		// TODO Auto-generated method stub
		socketPool.addSocket(recvStr, clientSocket);
		
	}
	
	public void deleteDevice(String device_Id){
		socketPool.deleteSocket(device_Id);
	}
	
	public void deleteDevice(String device_Id,Socket clientSocket){
		Socket socket = socketPool.getSocket(device_Id);
		if(clientSocket.getPort()==socket.getPort())
			deleteDevice(device_Id);
		
	}
	public boolean isDeviceConnect(String device_Id){
		return !(null==socketPool.getSocket(device_Id));
	}
	
	public int countDevices(){
		return socketPool.getSocketCounts();
	}
	
	//向水阀 发送消息
	public void sendToDevice(String message,String device_id) {
		
		Socket s = socketPool.getSocket(device_id);
		char c1 = 0x0D;
		char c2 = 0x0A;
		message = message+c1+c2;
		System.out.println("要向"+device_id+"发送的消息是："+message);
		if(s!=null){
			try {
				pWriter = new PrintWriter(s.getOutputStream());
				pWriter.write(message+"\n");
				pWriter.flush();
				logInfo("要向"+device_id+"发送的消息是："+message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
	}
	/**
	 * 通过消息的形式发送给app
	 * @param aliasName
	 * @param content
	 */
	public void sendToSoftWareByMessage(String aliasName,String content){
		
		 try {
			 jPush.sendToMobileByMessage(aliasName, content);
			 
			 //webSocketController.sendMessageToWeb(content);
			 logInfo("给app发送的消息是"+content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 通过通知的形式发送给app
	 * @param aliasName
	 * @param content
	 */
	public void sendToSoftWareByToast(String aliasName,String content){
		
		 try {
			 jPush.sendToMobileByToast(aliasName, content);
			 
			 webSocketController.sendMessageToWeb(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 当水阀大漏失或者用水结束时 存储设备的状态和用水总量
	 * @param equip_id
	 * @param water_usage
	 * @param equip_state
	 */
	public void saveEquipInfo(String equip_id,int water_usage,int equip_state){
		equipmentInfoService.saveEquipmentInfo(equip_id, water_usage, equip_state);
	}
	
	public void saveWaterInfo(String watermeter_id,long startTime,long  endTime,int volumn){
		waterConditionService.saveWaterInfo(watermeter_id, new Date(startTime), new Date(endTime), volumn);
	}
	
	public EquipmentInfo getEquipInfoById(String equip_id){
		return equipmentInfoService.getEquipmentById(equip_id);
	}
	
	public void printSockets(){
		socketPool.printSocketPool();
	}
	
	private void logInfo(String message){
		logger.info(message);
	}

}
