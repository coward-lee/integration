package org.lee.raft.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Set;

public class InitialHandler extends ChannelInitializer<DatagramChannel>{
    Set<Integer> ports;

    public InitialHandler(Set<Integer> ports) {
        this.ports = ports;
    }

    @Override
    protected void initChannel(DatagramChannel ch)  {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new AddPortHandler(ports));
        pipeline.addLast(new DataToObjectHandler());
        pipeline.addLast(new MessageDecoderOfRaftHandler());
    }
}
