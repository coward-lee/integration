package org.lee;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.lee.core.ServerContainer;
import org.lee.core.Startup;
import org.lee.decoder.IMProtoDecoder;
import org.lee.decoder.IMServerExchangeHandler;
import org.lee.decoder.MessageInHandler;
import org.lee.decoder.RegisterHandler;
import org.lee.encoder.IMProtoEncoder;
import org.lee.event.RegisterToOtherServer;
import org.lee.util.CustomConfigurationFactory;

import static org.lee.core.Config.port0;
import static org.lee.core.Config.port1;


public class Server {


    private static final Logger log = LogManager.getLogger(Server.class);

    private Integer port;

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

            // 关闭监听回调
            ChannelFuture close = listen.channel().closeFuture();
            close.sync();
            log.warn("服务器关闭");
        } catch (Exception e) {
            log.error(" 服务端出现错误", e);
            Thread.currentThread().interrupt();
        }
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    public static void run(Integer port) {
        CustomConfigurationFactory customConfigurationFactory = new CustomConfigurationFactory();
        Configurator.initialize(customConfigurationFactory.getConfiguration());
        new Server(port).runServer();
    }


    public static void main(String[] args) {
        CustomConfigurationFactory customConfigurationFactory = new CustomConfigurationFactory();
        Configurator.initialize(customConfigurationFactory.getConfiguration());

        Server server = new Server(0);
        Integer port = server.parse(args);
        server.setPort(port);

        Startup.start(port + "", port);
        new Thread(() -> {
            // 服务器注册到服务器
            RegisterToOtherServer register = new RegisterToOtherServer("localhost", port.equals(port1) ? port0 : port1);
            try {
                register.runClientForServer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        log.info("server seq:{}", ServerContainer.getServerSeq());
        server.runServer();
    }

    public Integer parse(String[] args) {
        return Integer.valueOf(args[0]);
    }

}
