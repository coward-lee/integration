package org.lee.aiguigu.netty.chat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("stringDecoder",new StringDecoder());
        pipeline.addLast("stringEncoder",new StringEncoder());
        pipeline.addLast("self ",new ChatServerChannelHandler());
        /**
         * readerIdle  netty 提供的处理空间状态的处理器
         * long readerIdleTime 表示多长时间没有读，就会发送一个心跳检测包是否链接
         * writerIdleTime 表示多长时间 没有写，就会发送一个心跳检测包是否链接
         * allIdleTime 表示多长时间 没有写，就会发送一个心跳检测包是否链接
         * 当idle state event 发生后，会传递给管道的下一个handle去处理，会去出发handler的userEventTriggered方法去处理
         */
        pipeline.addLast(new IdleStateHandler(10,15,20, TimeUnit.SECONDS));
        pipeline.addLast(new IdleStateEventHandler());

    }
}
