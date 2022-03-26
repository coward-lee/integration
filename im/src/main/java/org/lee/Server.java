package org.lee;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.lee.decoder.IMProtoDecoder;
import org.lee.decoder.MessageInHandler;
import org.lee.encoder.IMProtoEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private final static Map<String, Channel> clientMap = new ConcurrentHashMap<>(16);

    private final Logger log = LoggerFactory.getLogger(Server.class);

    private final Integer port;

    public Server(Integer port) {
        this.port = port;
    }

    public void runServer() {
        ServerBootstrap server = new ServerBootstrap();
        try {
            EventLoopGroup connectPool = new NioEventLoopGroup(1);
            EventLoopGroup worker = new NioEventLoopGroup();
            server.group(connectPool,worker)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            server.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new IMProtoDecoder());
                    ch.pipeline().addLast(new MessageInHandler());

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
        }
    }

    public static void main(String[] args) {
        new Server(80).runServer();
    }
}
