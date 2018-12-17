package com.example.water.socket;

/**
 * @Author: waiter
 * @Date: 18-12-16 11:45
 * @Description:
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 空闲连接
 * 当超过60s没有数据收到时，就发送心跳到远端
 * 如果没有回应，关闭连接
 */
@Component
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 若60s没有收到消息，调用userEventTriggered方法
        pipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
        pipeline.addLast(new HeartBeatHandle());
    }

    public static final class HeartBeatHandle extends ChannelHandlerAdapter {
        private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.UTF_8));

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if(evt instanceof IdleStateEvent) {
                // 发送心跳到远端
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
                        .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);    // 关闭连接
            } else {
                // 传递给下一个处理程序
                super.userEventTriggered(ctx, evt);
            }
        }
    }

}