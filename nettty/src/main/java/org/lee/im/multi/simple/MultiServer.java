package org.lee.im.multi.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.lee.im.multi.config.AddressConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MultiServer {
    private final static Logger log = LoggerFactory.getLogger(MultiServer.class);
    public static final List<SocketChannel>  channels = new ArrayList<>();
    public void runServer()  {
        ServerBootstrap b = new ServerBootstrap();
        EventLoopGroup parent = new NioEventLoopGroup(1);
        EventLoopGroup work = new NioEventLoopGroup();
        try{
            b.group(parent, work)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(AddressConfig.PORT);

            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new RegisterAdapter());
                    ch.pipeline().addLast(new ExchangeAdapter());
                }
            });
            ChannelFuture future = b.bind().sync();
            System.out.println("服务器启动成功");
            ChannelFuture close = future.channel().closeFuture();
            close.sync();
        }catch (Exception e){e.printStackTrace();}
        finally {
            work.shutdownGracefully();
            parent.shutdownGracefully();
        }

    }


    static class ExchangeAdapter extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf)msg;
            int len = buf.readableBytes();
            byte[] bytes = new byte[len];
            buf.getBytes(0,bytes);
            String content =  new String(bytes,0, len);
            System.out.println("客户端收到了：【 " +content+" 】");

            SocketChannel ch = (SocketChannel) ctx.channel();
            channels.forEach(channel->{
                if (ch!=channel) {
                    if (content.contains("a")){
                        channel.writeAndFlush(Unpooled.copiedBuffer(("发送到另外一个客户端的消息aaaaaaa").getBytes()));
                    }
                    if (content.contains("b")){
                        channel.writeAndFlush(Unpooled.copiedBuffer(("发送到另外一个客户端的消息bbb").getBytes()));
                    }
                }
            });
            super.channelRead(ctx, msg);
        }
    }

    static class RegisterAdapter extends ChannelInboundHandlerAdapter{
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            SocketChannel ch = (SocketChannel) ctx.channel();
            log.info("===================  a channel connected :【ip:{},port:{}】   =============", ch.remoteAddress().getAddress(),ch.remoteAddress().getPort());
            channels.add(ch);
            super.channelActive(ctx);
        }
    }
    static class ExchangeMessage extends MessageToMessageEncoder<String>{
        @Override
        protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
            SocketChannel ch = (SocketChannel) ctx.channel();
            System.out.println("服务器发送到客户端："+ch.remoteAddress().getAddress()+":"+ch.remoteAddress().getPort());
            out.add(msg);
        }
    }

    public static void main(String[] args) {
        try {
            new MultiServer().runServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
