package com.example.water.socket;

/**
 * Created by  waiter on 18-6-18.
 *
 * @author waiter
 */

import java.net.InetAddress;
import java.util.Date;

import com.example.water.model.Greeting;
import com.example.water.service.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
@Qualifier("serverHandler")
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    @Autowired
    private Service service;
    @Autowired
    private GreetingService greetingService;
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void messageReceived(ChannelHandlerContext ctx, String msg)
            throws Exception {
        log.info("client msg:" + msg);
        String clientIdToLong = ctx.channel().id().asLongText();

        log.info("client long id:" + clientIdToLong);
        String clientIdToShort = ctx.channel().id().asShortText();
        log.info("client short id:" + clientIdToShort);
//        if (msg.contains("bye")) {
//            //close
//            ctx.channel().close();
//        } else {
//            //send to client
//            ctx.channel().writeAndFlush("Yoru msg is:" + msg);
//        }
        greetingService.save(new Greeting(msg,new Date()));

        switch (msg.charAt(0)) {
            case '0':
                service.deviceData(ctx.channel(),msg.substring(4));
                break;
            case '1':
                service.respond(ctx.channel(),msg.substring(1,4));
                break;
            case '2':
                service.respond(ctx.channel(),msg.substring(1,4));
                break;
            case '3':
                service.initDevice(ctx,msg);

                break;
            default:

                break;
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


        log.info("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
        log.info(ctx.name());


//        ctx.channel().writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");

        super.channelActive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("\nChannel is disconnected");
        service.logout(ctx.channel());
        super.channelInactive(ctx);
    }


}