package org.lee.aiguigu.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        // 1.创建线程，accept 和 worker
        // 2.accept（master）只处理accept 请求
        // 3. 两个都是无限循环
        // 4. masterGroup 和 workerGroup 含有子线程（NioEventLoop）的个数 * 2
        //   两个的默认的数量都是 cpu 核心数
        NioEventLoopGroup master = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try{

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(master, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {// 创建一个管道初始化对象
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }); // 给我们的workerGroup的 event loop 对应的管道设置处理器
            System.out.println("server started");
            ChannelFuture future = bootstrap.bind(6668).sync();

            // 对关闭通道进行监听
            future.channel().closeFuture().sync();
        }finally {
            master.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
