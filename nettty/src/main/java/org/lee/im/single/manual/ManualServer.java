package org.lee.im.single.manual;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.lee.im.util.ScannerUtil;

import java.util.List;

public class ManualServer {

    int port;
    static volatile Channel channel = null;

    public ManualServer(int port) {
        this.port = port;
    }


    public void runServer() {
        EventLoopGroup parent =new  NioEventLoopGroup(1);
        EventLoopGroup worker =new  NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(parent, worker);
            b.channel(NioServerSocketChannel.class);
            b.localAddress(port);
//            b.option(ChannelOption.SO_KEEPALIVE, true);
//            b.option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);

            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ServerInChannel());
                    ch.pipeline().addLast(new ServerOutChannel());
                }
            });
            ChannelFuture future = b.bind().sync();
            System.out.println("服务器启动成功");
            ChannelFuture close = future.channel().closeFuture();
            startInputListening();
            close.sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            parent.shutdownGracefully();
            worker.shutdownGracefully();

        }
    }

//    static class ServerDecoder extends MessageToMessageDecoder<String> {
//        @Override
//        protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
//            System.out.println("服务端 收到了：【"+msg+"】");
//            out.add(msg);
//        }
//    }
    static class ServerInChannel extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf but = ((ByteBuf)msg);
            System.out.println("服务端 收到了：【"+ but.toString(CharsetUtil.UTF_8)+"】");
//            ctx.channel().writeAndFlush("replay a message");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ManualServer.channel = ctx.channel();
        }
    }
    static class ServerOutChannel extends MessageToMessageEncoder<String> {
        @Override
        protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
            System.out.println("服务端 发送了：【" + msg + "】");
            out.add(msg);
        }
    }
    public void startInputListening(){
        new Thread(()->{
            try {
                while (channel == null){
                    System.out.println("等待中");
                    Thread.sleep(1000);
                }
                while (true) {
                    String line = ScannerUtil.getLine();
                    if (ScannerUtil.quit(line)){
                        return;
                    }
                    System.out.println("服务端发送了     "+ line);
                    channel.writeAndFlush(Unpooled.copiedBuffer(line.getBytes()));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        new ManualServer(80).runServer();
    }



}
