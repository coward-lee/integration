package org.lee.aiguigu.netty.pipeline;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyPipelineServer {

    private final static Logger log = LoggerFactory.getLogger(NettyPipelineServer.class);
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
                    .childHandler(new ChannelInitializerDemo()); // 给我们的workerGroup的 event loop 对应的管道设置处理器
            ChannelFuture future = bootstrap.bind(6668);
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()){
                        log.info("listen ok");
                    }else{
                        log.info("listen failed");
                    }
                }
            });
            log.info("server started");
            // 对关闭通道进行监听
            future.channel().closeFuture().sync();
        }finally {
            master.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
