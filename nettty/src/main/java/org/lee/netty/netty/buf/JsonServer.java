package org.lee.netty.netty.buf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

public class JsonServer {

    int port;

    public JsonServer(int port) {
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
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,4,0,4));
                    ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                    ch.pipeline().addLast(new JsonMsgDecoder());
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
    static class JsonMsgDecoder extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String json = (String)msg;
            System.out.println("拆包前：【"+msg+"】");
            JsonMsg jsonMsg = JsonMsg.parseFromJson(json);
            System.out.println("收到一个Json数据包=》"+jsonMsg);
        }
    }


    public static void main(String[] args) {
        int port = 80;
        new JsonServer(port).runServer();

    }

}
