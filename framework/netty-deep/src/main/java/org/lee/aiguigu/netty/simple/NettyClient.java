
package org.lee.aiguigu.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {// 创建一个管道初始化对象
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new NettyClientHandler()); // 加入自己的处理器
                        }
                    }); // 给我们的workerGroup的 event loop 对应的管道设置处理器
            System.out.println("client started");
            // 链接服务端
            ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 6668)).sync();
            future.channel().writeAndFlush("你好");

            // 对关闭通道进行监听
            future.channel().closeFuture().sync();
        }finally {
            worker.shutdownGracefully();
        }
    }
}
