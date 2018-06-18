package com.example.water.server;


import com.example.water.model.EquipmentInfo;
import com.example.water.utils.ApplicationData;
import com.example.water.utils.DataUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class TCPServerThread implements Runnable{
	private Socket socket; 
	private int equipState = 5;
	long startTime,endTime,time_use;
	InputStream isr = null;
	private boolean flag = true;
	private MessageHandler msgHandler = null;
	private String equipId = null;
	private int total_water = 0;
	String aliasName = "MyDevice";
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	boolean b = true;
	
	private long timeInterval = 1*60*60*1000+60*1000*2; 
	
	private long lastTime = 0;
	
	private boolean connect = false;
    
    public TCPServerThread(Socket i) {  
        // TODO Auto-generated constructor stub  
    	socket = i;  
    	msgHandler = MessageHandler.getInstance();   
    	new Thread(new DetectConnectThread()).start();
    }  

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {			
			isr = socket.getInputStream();
			/**
			 * 给客户端发送心跳包   
			 */
			/*new Thread(new Runnable() {
				boolean b = true;
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(b){
						try{
						      socket.sendUrgentData(0xFF);
						      Thread.sleep(5*1000);
						}catch(Exception ex){
						      flag = false;
						      b = false;
						}	
					}
				}
			}).start();*/
			while(flag&&socket.isConnected()&&!socket.isClosed()){
				byte[] b = new byte[24];				
				int i = isr.read(b);				
				if(i<=0){
					logger.info("连接断开，收到"+i+"个字符");
					System.out.println("连接断开，收到"+i+"个字符");
					flag = false;
				}
					
				else {
					handleRecvData(socket, new String(b, 0, i));
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(null!=equipId&&!"".equals(equipId)){
					msgHandler.deleteDevice(equipId,socket);
					msgHandler.sendToSoftWareByMessage(aliasName, equipId+"5"+String.format("%05d", total_water)+"0");
					ApplicationData.num--;
				}
				b = false;	
				msgHandler.printSockets();
				if(isr!=null) {
					isr.close();
				}
				if(socket!=null){
					socket.close();
					socket = null;
				}					
				logger.info("socket连接关闭"+ApplicationData.num+"   当前有"+msgHandler.countDevices()+"终端连接");	
				System.out.println("socket连接关闭"+ApplicationData.num+"   当前有"+msgHandler.countDevices()+"终端连接");
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
        
	}	
	
	public void handleRecvData(Socket clientSocket ,String recvStr) {//处理接收的消息
		//System.out.println("Hello World!" + recvStr);
		System.out.println("收到"+clientSocket.getRemoteSocketAddress()+"的端口"+clientSocket.getPort()+"的消息"
				+recvStr);
		logger.info("收到"+clientSocket.getRemoteSocketAddress()+"的端口"+clientSocket.getPort()+"的消息"
				+recvStr);
		if(24==recvStr.length()){
			handleMessageFromDevice(clientSocket,recvStr);
		}
        /*if(recvStr.equals("OVER"))
        	return;
        if(recvStr.length()==3&&DataUtils.isNumeric(recvStr)){        	
        	handleConnect(clientSocket,recvStr);//添加设备 
        	
        }else if(null!=equipId&&5==recvStr.length()){//处理来水阀的反馈消息
        	handleFeedBackFromDevice(clientSocket,recvStr);
        }
        
        else if(recvStr.length()==23&&DataUtils.isNumeric(recvStr)){        	
        	handleDeviceData(clientSocket,recvStr);  //判断消息是否是水阀发来的
        	msgHandler.sendToDevice("z"+equipId+"b", equipId);
        	
        }     */
        else if(null==equipId&&recvStr.length()>=4&&recvStr.length()<=20){
        	handleAppData(clientSocket,recvStr);//判断消息是否是App发来的
        } 
	
	}

	private void handleMessageFromDevice(Socket clientSocket,String recvStr) {
		// TODO Auto-generated method stub
		if('0'>recvStr.charAt(0)||'9'<recvStr.charAt(0)) {
			return;
		}
		lastTime = System.currentTimeMillis();
		int messageType = recvStr.charAt(0)-'0';
		if(messageType!=3&&(null==equipId||0==equipId.length())) {
			return;
		}
		String message;
		switch(messageType){
			case 0:
				message = recvStr.substring(1);
				handleDeviceData(clientSocket,message);  //判断消息是否是水阀发来的
	        	//msgHandler.sendToDevice("z"+equipId+"b", equipId);
				break;
			case 1:
				message = recvStr.substring(1,6);
				handleFeedBackFromDevice(clientSocket,message);
				break;
			case 2:
				message = recvStr.substring(1,5);
				msgHandler.sendToDevice("z"+equipId+"b", equipId);
				break;
			
			case 3:
				message = recvStr.substring(1, 4);
				handleConnect(clientSocket,message);
				break;
			case 4:
				message = recvStr.substring(1,6);
				handleFeedBackFromDevice(clientSocket,message);
				break;
			default :
				break;
				
			
			
			
		}
		
	}

	private void handleFeedBackFromDevice(Socket clientSocket, String recvStr) {
		// TODO Auto-generated method stub
		String aliasName = "MyDevice";
		//当水阀漏失重启时 修改数据库中水阀的状态（每次发生漏失时，会修改数据库中水阀状态）
		if('q' == recvStr.charAt(4)){
			msgHandler.saveEquipInfo(equipId,  total_water, 5);
		}
		System.out.println("给app发送："+recvStr.substring(1));
		msgHandler.sendToSoftWareByMessage(aliasName, recvStr.substring(1));
	}

	public void handleDeviceData(Socket clientSocket ,String recvStr) {//处理水阀发来的消息 
	    
	     
	     int volumn = 0;
	    
	     int stateNow = Integer.parseInt(recvStr.substring(3,4));
	     
	     int year = Integer.parseInt(recvStr.substring(4,8));
	     int month = Integer.parseInt(recvStr.substring(8,10));
	     int day = Integer.parseInt(recvStr.substring(10,12));
	     int hour = Integer.parseInt(recvStr.substring(12,14));
	     int minute = Integer.parseInt(recvStr.substring(14,16));
	     int second = Integer.parseInt(recvStr.substring(16,18));
	     if(stateNow==1||stateNow==2){
	     	//发生漏失
	    	 String message = year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second;
	    	 msgHandler.sendToSoftWareByMessage(aliasName, recvStr.substring(0,4)+String.format("%05d", total_water)+"1");
	    	 msgHandler.sendToSoftWareByToast(aliasName, message+equipId+"发生漏失");
	    	 msgHandler.saveEquipInfo(equipId,  total_water, stateNow);
	    	 
	     }
	     if(stateNow==0){   
	    	 startTime = DataUtils.getTimeInMillis(year, month, day, hour, minute, second);
	    	 if(1==equipState||2==equipState)
	    		 msgHandler.saveEquipInfo(equipId,  total_water, 5);
	    	 
	     }
	     if(equipState==0&&stateNow==5){   	 
	    	 
	    	 endTime = DataUtils.getTimeInMillis(year, month, day, hour, minute, second);
	    	 String watermeter_id = recvStr.substring(0,2);
	    	 volumn = Integer.parseInt(recvStr.substring(18,23));
	    	 msgHandler.saveWaterInfo(watermeter_id, startTime, endTime, volumn);
	    	 total_water = volumn+total_water;
	    	 msgHandler.saveEquipInfo(equipId,  total_water, 5);
	    	 msgHandler.sendToSoftWareByMessage(aliasName, recvStr.substring(0,4)+String.format("%05d", total_water)+"1");
	    	 //waterConditionService.saveWaterInfo(watermeter_id, startTime, endTime, volumn);
	     }
	     equipState=stateNow;		
	}
	
	/**
	 * 处理App发来的命令
	 * @param recvPacket
	 * @param recvStr 命令字符串
	 */
	public void handleAppData(Socket clientSocket ,String recvStr) {
		String equip_id =  recvStr.substring(1,4);
		if('q'==recvStr.charAt(0)){
			String message = getEquipInfo(equip_id);
			System.out.println("给app发送"+message);
			msgHandler.sendToSoftWareByMessage(aliasName, message);
			
			
		}else{			
			msgHandler.sendToDevice(recvStr, equip_id);
		}
		
		flag = false;
		 
	}
	
	/**
	 * 水阀连接时记下信息，便于后续发送命令
	 * @param clientSocket tcp连接的socket 记录了终端的Ip、端口等信息
	 * @param recvStr    终端 的Id
	 */
	public void handleConnect(Socket clientSocket, String recvStr) {
		// TODO Auto-generated method stub
		System.out.println("增加设备"+recvStr);
		equipId = recvStr;
		msgHandler.addDevice(clientSocket, recvStr);
		EquipmentInfo eInfo = msgHandler.getEquipInfoById(equipId);
		if(null!=eInfo)
			total_water = eInfo.getWaterUsage();
		//设置终端的时间
		String message = DataUtils.getTimeString();
		message = "z"+equipId+"a"+message;	
		//msgHandler.sendToDevice("z"+equipId+"b", equipId);
		msgHandler.sendToDevice(message, equipId);
		
		msgHandler.sendToSoftWareByMessage(aliasName, equipId+"5"+String.format("%05d", total_water)+"1");
		
		ApplicationData.num++;
		
		connect = true;
		
		new Thread(new TimeThread()).start();
		
	}
	
	private String getEquipInfo(String equip_id){
		StringBuffer sb = new StringBuffer();
		EquipmentInfo eInfo = msgHandler.getEquipInfoById(equip_id);
		sb.append(equip_id);
		sb.append(eInfo.getEquipState());
		sb.append(String.format("%05d", eInfo.getWaterUsage()));
		if(msgHandler.isDeviceConnect(equip_id))
			sb.append(1);
		else
			sb.append(0);
		return sb.toString();
	}
	
	class TimeThread implements Runnable{
		boolean threadFlag = true;
		@Override
		public void run() {
			System.out.println("线程执行");
			// TODO Auto-generated method stub
			while(threadFlag){
				try {
					Thread.sleep(32*60*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println(System.currentTimeMillis()+"  "+lastTime);
				if(System.currentTimeMillis()-lastTime>timeInterval){
					threadFlag = false;
					try {
						flag = false;
						socket.close();
						System.out.println("超时，断开连接 ");
						logger.info("超时，断开连接");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
					
				
			}
			
			
		}
		
		 
		
	}
	
	class DetectConnectThread implements Runnable{
		
		@Override
		public void run() {
			System.out.println("检测连接线程执行");
			// TODO Auto-generated method stub
			
				try {
					Thread.sleep(2*60*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!connect)
					if(null==equipId||0==equipId.length()){
						try {
							flag = false;
							if(socket!=null)
								socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
			
		}
		
		 
		
	}
	
	
}
