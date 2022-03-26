package org.lee;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.lee.decoder.IMProtoDecoder;
import org.lee.decoder.MessageInHandler;
import org.lee.encoder.IMProtoEncoder;
import org.lee.util.MessageUtil;
import org.lee.util.SendMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private final Logger log = LoggerFactory.getLogger(Client.class);

    private final Integer port;
    private final String addr;

    public Client(Integer port, String addr) {
        this.port = port;
        this.addr = addr;
    }

    public void runClient() {
        Bootstrap b = new Bootstrap();
        try {
            EventLoopGroup loop = new NioEventLoopGroup();
            b.group(loop)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(addr, port)
                    .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ch.pipeline().addLast(new IMProtoDecoder());
                    ch.pipeline().addLast(new MessageInHandler());

                    ch.pipeline().addLast(new IMProtoEncoder());
                }
            });
            ChannelFuture connect = b.connect();
            connect.addListener(f->{
                if (f.isSuccess()){
                    Channel channel = connect.channel();
                    channel.writeAndFlush("connect success");
                    SendMessageUtil.startInputListening(channel);
                    log.info("连接成功");
                }else{
                    log.info("连接失败");
                }
            });

            ChannelFuture close = connect.channel().closeFuture();
            close.sync();
            log.info("连接关闭   ==== === = = == = = = =");
        } catch (Exception e) {
            log.error(" 客户端出现错误", e);
            Thread.currentThread().interrupt();
        }
    }


    public static void main(String[] args) {
        new Client(80,"127.0.0.1").runClient();
    }
}
