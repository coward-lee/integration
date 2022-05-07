package org.lee;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.lee.core.Config.port1;

public class ServerTest {
    private final Logger log = LogManager.getLogger(Server.class);

    public void runServer(Integer port) throws InterruptedException {
        ServerBootstrap server = new ServerBootstrap();

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


                ch.pipeline().addLast(new ConnectionInHandler());
            }
        });

        ChannelFuture listen = server.bind().sync();
        log.info("启动成功，服务器端口为：{}", port);

        // 关闭监听回调
        ChannelFuture close = listen.channel().closeFuture();
        close.sync();
        log.warn("服务器关闭");
    }

    public void runClient(Integer port) throws InterruptedException {
        AtomicBoolean connect = new AtomicBoolean(false);

        ChannelFuture channelFuture = null;
        Bootstrap b = newBoot(port);

        while (true){
            try {
                channelFuture = b.connect().sync();
                System.out.println("连接成功");
                break;
            } catch (Throwable e) {
                Thread.sleep(1000);
                System.out.println("连接失败正在重连。。。  " + "localhost" + ":" + port);
            }
        }

        ChannelFuture close = channelFuture.channel().closeFuture();
        close.sync();
        log.info("连接关闭   ==== === = = == = = = =");

    }
    private Bootstrap newBoot(Integer port){
        Bootstrap b = new Bootstrap();
        b.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress("localhost", port)
                .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
            }
        });
        return b;
    }

    class ConnectionInHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("有链接来了");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ServerTest serverTest = new ServerTest();
        Integer port = serverTest.parse(args);
//        new Thread(() -> {
//            try {
//                serverTest.runClient(port == 80 ? 81 : 80);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();

        serverTest.runClient(port1);
//        serverTest.runServer(port);
    }

    public Integer parse(String[] args) {
        return Integer.valueOf(args[0]);
    }

}
