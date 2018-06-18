package com.example.water.utils;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
//import cn.jpush.api.common.resp.APIConnectionException;
//import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class JPush {
	
	private static JPushClient jpushClient = null;
	private static String aliasName = "MyDevice"; 
	private String Content = null;
	private static Properties props = null;
	private static JPush jPush = new JPush();;
	static {
		// 给props进行初始化，即加载dbconfig.properties文件到props对象中
		try {
			InputStream in = JDBCUtils.class.getClassLoader()
					.getResourceAsStream("dbconfig.properties");
			props = new Properties();
			props.load(in);
			jpushClient = new JPushClient(props.getProperty("masterSecret"), props.getProperty("appKey"));  
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	private JPush() {
		
	}
	
	public static JPush getInstance(){

        return jPush;
    } 

	
	
	
	public void sendToMobileByToast(String aliasName,String Content){
	
	   
   PushPayload payload = buildPushObject_all_alias_alert(aliasName, Content);
	 //PushPayload payload = buildPushObject_all_alias_message(aliasName, Content);
    try {
        PushResult result = jpushClient.sendPush(payload); 
        
    } catch (APIConnectionException e) {
        // Connection error, should retry later  

    } catch (APIRequestException e) {
        // Should review the error, and fix the request
    }
	}
	
	public void sendToMobileByMessage(String aliasName,String Content){
		
		   
		   //PushPayload payload = buildPushObject_all_alias_alert(aliasName, Content);
			 PushPayload payload = buildPushObject_all_alias_message(aliasName, Content);
		    try {
		        PushResult result = jpushClient.sendPush(payload); 
		        
		    } catch (APIConnectionException e) {
		        // Connection error, should retry later  

		    } catch (APIRequestException e) {
		        // Should review the error, and fix the request       
		    }
			}
    
    public static PushPayload buildPushObject_all_alias_alert(String aliasName,String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(aliasName))
                .setNotification(Notification.alert(content))
                .build();
    }
    
    public static PushPayload buildPushObject_all_alias_message(String aliasName,String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(aliasName))
                 .setMessage(Message.content(content))
                .build();
    }

}
