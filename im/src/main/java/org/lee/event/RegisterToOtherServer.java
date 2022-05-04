package org.lee.event;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.lee.decoder.IMProtoDecoder;
import org.lee.decoder.IMServerExchangeHandler;
import org.lee.decoder.MessageInHandler;
import org.lee.decoder.RegisterHandler;
import org.lee.domain.MessageProto;
import org.lee.domain.MessageType;
import org.lee.encoder.IMProtoEncoder;

public class RegisterToOtherServer {

    Bootstrap b;
    String addr;
    Integer port;

    public RegisterToOtherServer(String addr, Integer port) {
        b = new Bootstrap();
        b.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress(addr, port)
                .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

                ch.pipeline().addLast(new IMProtoDecoder());
                ch.pipeline().addLast(new MessageInHandler());
                ch.pipeline().addLast(new RegisterHandler());
                ch.pipeline().addLast(new IMServerExchangeHandler());

                ch.pipeline().addLast(new IMProtoEncoder());
            }
        });
        this.addr = addr;
        this.port = port;
    }

    /**
     * 用于服务端之间的相互连接
     */
    public void runClientForServer() throws InterruptedException {
        while (true) {
            try {
                System.out.println("准备注册到服务器  " + addr + ":" + port);
                ChannelFuture connect = b.connect().sync();
                MessageProto.Message message = MessageProto.Message.newBuilder()
                        .setTo("server")
                        .setContent("register to server")
                        .setHeader(MessageType.JOIN.getVal())
                        .setFrom(port+"")
                        .build();
                connect.channel().writeAndFlush(message);

                System.out.println("已经注册到服务器");
                break;
            } catch (Throwable e) {
                Thread.sleep(1000);
                System.out.println("连接失败正在重连。。。  " + addr + ":" + port);
            }
        }
    }
}
