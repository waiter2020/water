package com.example.water.socket;

/**
 * Created by  waiter on 18-6-18.
 *
 * @author waiter
 */
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.example.water.model.EquipmentInfo;
import com.example.water.service.EquipmentInfoService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
@Component
public class TCPServer {
    @Autowired
    @Qualifier("serverBootstrap")
    private ServerBootstrap b;

    @Autowired
    @Qualifier("tcpSocketAddress")
    private InetSocketAddress tcpPort;

    @Autowired
    private Service service;
    @Autowired
    private EquipmentInfoService equipmentInfoService;

    private ChannelFuture serverChannelFuture;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void start() throws Exception {
        logger.info("Starting server at " + tcpPort);
        serverChannelFuture = b.bind(tcpPort).sync();
        ArrayList<EquipmentInfo> all = equipmentInfoService.findAll();
        for (EquipmentInfo a:all){
            a.setOnline(false);
            a.setLoginId(null);
        }
        equipmentInfoService.saveAll(all);
    }

    @PreDestroy
    public void stop() throws Exception {
        ConcurrentHashMap<String, Channel> map = OnlineDevices.getMap();
        for (Map.Entry<String, Channel> stringChannelEntry : map.entrySet()) {
            service.logout(stringChannelEntry.getValue());
        }
        serverChannelFuture.channel().closeFuture().sync();
    }

    public ServerBootstrap getB() {
        return b;
    }

    public void setB(ServerBootstrap b) {
        this.b = b;
    }

    public InetSocketAddress getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(InetSocketAddress tcpPort) {
        this.tcpPort = tcpPort;
    }
}