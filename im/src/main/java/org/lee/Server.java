package org.lee;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.lee.decoder.IMProtoDecoder;
import org.lee.decoder.IMServerExchangeHandler;
import org.lee.decoder.MessageInHandler;
import org.lee.decoder.RegisterHandler;
import org.lee.encoder.IMProtoEncoder;
import org.lee.util.CustomConfigurationFactory;

import java.io.*;
import java.net.URI;

public class Server {


    private final Logger log = LogManager.getLogger(Server.class);

    private final Integer port;

    public Server(Integer port) {
        this.port = port;
    }

    public void runServer() {
        ServerBootstrap server = new ServerBootstrap();
        try {
            EventLoopGroup connectPool = new NioEventLoopGroup(1);
            EventLoopGroup worker = new NioEventLoopGroup();
            server.group(connectPool, worker)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            server.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new IMProtoDecoder());
                    ch.pipeline().addLast(new RegisterHandler());
                    ch.pipeline().addLast(new MessageInHandler());
                    ch.pipeline().addLast(new IMServerExchangeHandler());

                    ch.pipeline().addLast(new IMProtoEncoder());
                }
            });

            ChannelFuture listen = server.bind().sync();
            log.info("启动成功，服务器端口为：{}", port);
            System.out.println("启动成功");

            // 关闭监听回调
            ChannelFuture close = listen.channel().closeFuture();
            close.sync();
            log.warn("服务器关闭");
        } catch (Exception e) {
            log.error(" 服务端出现错误", e);
        }
    }


    public static void run(Integer port) {
        CustomConfigurationFactory customConfigurationFactory = new CustomConfigurationFactory();
        Configurator.initialize(customConfigurationFactory.getConfiguration());
        new Server(port).runServer();
    }


}
