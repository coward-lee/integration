package org.lee.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

public class Server {
    public static void main(String[] args) {
        new ServerBootstrap().group(new NioEventLoopGroup(1))
                .channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) {
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                System.out.println(msg.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                }).bind(8080);
    }
}
