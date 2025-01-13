package org.lee.aiguigu.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.ReflectiveChannelFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatServer {

    private static final Logger log = LoggerFactory.getLogger(ChatServer.class);

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup master = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup(1);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            master.shutdownGracefully();
            worker.shutdownGracefully();
        }));

        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture listen = bootstrap.group(master, worker)
//                .channel()
                .channelFactory(new ReflectiveChannelFactory<>(NioServerSocketChannel.class))
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChatServerInitializer())
                .handler(new LoggingHandler(LogLevel.INFO))
                .bind(CharCenter.PORT)
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        log.info("listen success");
                    }else{
                        Throwable cause = future.cause();
                        log.info("listen failed");
                        try {
                            throw cause;
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .channel()
                .closeFuture();
        log.info("started");
        listen.sync();
    }
}
