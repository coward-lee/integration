package org.lee.question;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class StickyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                    System.out.println("Received from server: " + msg);
                                }
                            });
                        }
                    });

            ChannelFuture future = bootstrap.connect("localhost", 8080).sync();

            // 连续发送 10 条消息，模拟粘包现象
            for (int i = 0; i < 10; i++) {
                String message = "Message " + i + "\n";
                future.channel().writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
            }

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
