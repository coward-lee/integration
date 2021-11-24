package org.lee.netty.netty.cahnnel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;

public class NettyDiscardServer {

    public static void main(String[] args) {
        int port = 80;
        new NettyDiscardServer(port).runServer();
    }



    private final int serverPort;
    ServerBootstrap b = new ServerBootstrap();
    public NettyDiscardServer(int port){
        this.serverPort = port;
    }

    public void runServer(){
        // 通常用于负责，新连接的建立
        EventLoopGroup loopGroup = new NioEventLoopGroup(1);
        // 负责网络I/O
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            // 这里决定的
            b.group(loopGroup, worker);
            b.channel(NioServerSocketChannel.class);
            b.localAddress(serverPort);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new NettyDiscardHandler());
                }
            });

            ChannelFuture future = b.bind().sync();
            System.out.println("服务器启动成功");
            ChannelFuture closeFuture =future.channel().closeFuture();
            closeFuture.sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
            loopGroup.shutdownGracefully();
        }
    }


    static class NettyDiscardHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf in = (ByteBuf) msg;
            try{
                System.out.println("收到消息：");
                while (in.isReadable()){
                    System.out.print(in.readableBytes());
                }
                System.out.println();
            }finally {
                {
                    ReferenceCountUtil.release(msg);
                }
            }
        }
    }
}
