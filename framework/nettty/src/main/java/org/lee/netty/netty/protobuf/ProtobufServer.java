package org.lee.netty.netty.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

public class ProtobufServer {


    public void runServer(){
        EventLoopGroup parent = new NioEventLoopGroup(1);
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        try{
            b.group(parent, work);
            b.channel(NioServerSocketChannel.class);
            b.localAddress(80);

            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch){
                    // 添加指定的handler
                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    ch.pipeline().addLast(new ProtobufDecoder(MsgProtos.Msg.getDefaultInstance()));
                    ch.pipeline().addLast(new ProtobufBusinessDecoder());
                }
            });
            ChannelFuture future = b.bind().sync();
            System.out.println("服务器启动成功");
            ChannelFuture closeFuture = future.channel().closeFuture();
            closeFuture.sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            work.shutdownGracefully();
            parent.shutdownGracefully();
        }
    }
    static class ProtobufBusinessDecoder extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg){
            MsgProtos.Msg m = (MsgProtos.Msg) msg;
            //
            System.out.println("收到了消息："  + m.getContent());
            System.out.print("      收到了消息："  + m.getId());
        }
    }
    public static void main(String[] args) {
        new ProtobufServer().runServer();
    }


}
