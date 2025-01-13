package org.lee.im.single.manual;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.lee.im.util.ScannerUtil;

import java.util.List;

public class ManualClient {

    int port;
    String ip;

    public ManualClient(String ip, int port) {
        this.port = port;
        this.ip = ip;
    }


    public void runClient() throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(worker);
        b.channel(NioSocketChannel.class);
        b.remoteAddress(ip, port);
//        b.option(ChannelOption.SO_KEEPALIVE, true);
//        b.option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ClientOutChannel());
                ch.pipeline().addLast(new ClientInChannel());
                ch.pipeline().addLast(new ClientDecoderChannel());

            }
        });

        ChannelFuture f = b.connect();
        f.addListener((ChannelFuture future)->{
            if (future.isSuccess()){
                System.out.println("客户端连接成功");
            }else{
                System.out.println("客户端连接失败");
            }
        });
        f = f.sync();
        Channel channel = f.channel();
        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000);
            ByteBuf buf = Unpooled.copiedBuffer("aaaaaaaaaa".getBytes());
            System.out.println("发送了   aaaaaaaaaa");
            channel.writeAndFlush(buf);
        }
        startInputListening(channel);
        channel.flush();
        ChannelFuture closeFuture = channel.closeFuture();
        // // 服务监听通道会一直等待关闭的异步任结束 服务监听通道会一直等待关闭的异步任结束
        closeFuture.sync();

    }
    static class ClientInChannel extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf but = ((ByteBuf)msg);
            System.out.println("客户端 收到了：【"+ but.toString(CharsetUtil.UTF_8)+"】");
            super.channelRead(ctx, msg);
        }
    }

    static class ClientDecoderChannel extends MessageToMessageDecoder<String> {

        @Override
        protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
            System.out.println("ClientDecoderChannel 客户端 收到了：【"+msg+"】");
            out.add(msg);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            super.channelRead(ctx, msg);
        }
    }


    static class ClientOutChannel extends MessageToMessageEncoder<String> {
        @Override
        protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
            System.out.println("ClientOutChannel客户端 发送了：【"+msg+"】");
            out.add(msg);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ManualClient("127.0.0.1",80).runClient();
    }

    public void startInputListening(Channel channel){
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

}
