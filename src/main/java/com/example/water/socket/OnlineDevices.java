package com.example.water.socket;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by  waiter on 18-7-26  下午3:16.
 * 在线设备
 * @author waiter
 */
@Component
public class OnlineDevices {
    private static final ConcurrentHashMap<String,Channel> map = new ConcurrentHashMap<>();

    public void addDevice(Channel channel){
        map.put(channel.id().asLongText(),channel);
    }

    public void removeDevice(String Id){
        if (!map.isEmpty()) {
            map.remove(Id);
        }
    }

    public void removeAll(){
        map.clear();

    }

    public Channel getDevice(String Id){
        return map.get(Id);
    }

    public int getSize(){
        return map.size();
    }

    public static ConcurrentHashMap<String, Channel> getMap() {
        return map;
    }
}
