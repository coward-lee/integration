package org.lee.netty.netty.pipeline;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import org.lee.netty.netty.buf.JsonMsg;
import org.lee.netty.netty.buf.JsonServer;

public class PipelineServer {

    int port;

    public PipelineServer(int port) {
        this.port = port;
    }

    public void runServer(){
        EventLoopGroup loopGroup = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(loopGroup, worker);
            b.channel(NioServerSocketChannel.class);
            b.localAddress(port);

            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

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
    static class In1 extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("in1");
            super.channelRead(ctx, msg);
        }
    }

    static class In2 extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("in2");
            super.channelRead(ctx, msg);
        }
    }
    static class Out1 extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            super.write(ctx, msg, promise);
        }

    }

    static class Out2 extends ChannelOutboundHandlerAdapter {

    }


    public static void main(String[] args) {
        int port = 80;
        new JsonServer(port).runServer();

    }

}
