package org.lee.aiguigu.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class ChatClient {
    private static final Logger log = LoggerFactory.getLogger(ChatClient.class);
    private static Channel channel;

    public static void main(String[] args) throws InterruptedException {
        startListen();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                        log.info("receive from server:{}", msg);
                                    }
                                });
                    }
                })
                .connect(new InetSocketAddress("127.0.0.1", CharCenter.PORT))
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        log.info("connected");
                        channel = future.channel();

                    } else {
                        log.info("connected fail");
                        try {
                            throw future.cause();
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).sync();

    }

    private static void startListen() {
        new Thread(() -> {
            try {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String text = scanner.nextLine().trim();
                    if ("quit".equals(text)) {
                        break;
                    }
                    getChannel().writeAndFlush(text+"\r\n");
                }
            } catch (Throwable t) {
                log.info(t.getMessage(), t);
            }
        }).start();
        ;
    }

    private static Channel getChannel() {
        return channel;
    }
}
