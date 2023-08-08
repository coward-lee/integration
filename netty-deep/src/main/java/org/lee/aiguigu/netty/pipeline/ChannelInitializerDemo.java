package org.lee.aiguigu.netty.pipeline;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ChannelInitializerDemo extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("InHandler1",new InHandler1());
        ch.pipeline().addLast("InHandler2",new InHandler2());
        ch.pipeline().addLast("InHandler3",new InHandler3());
        ch.pipeline().addLast("OutHandler1",new OutHandler1());
        ch.pipeline().addLast("OutHandler2",new OutHandler2());
        ch.pipeline().addLast("OutHandler3",new OutHandler3());
    }
}
