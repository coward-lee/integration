package org.lee.raft;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.lee.im.multi.config.AddressConfig;
import org.lee.im.util.ScannerUtil;
import org.lee.raft.domain.MessageProto.Message;
import org.lee.raft.handler.InitialHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class RaftNode {
    private final Logger log = LoggerFactory.getLogger(RaftNode.class);
    private static final Set<Integer> ports = new HashSet<>();

    String name;
    int port;

    public RaftNode(int port) {
        this.port = port;
    }

    public RaftNode(int port, String name) {
        this.port = port;
        this.name = name;
    }

    public RaftNode(String name, int port) {
        this.port = port;
        this.name = name;
    }

    public void runNode(){

        NioEventLoopGroup eventExecutors = null;
        try {

            eventExecutors = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(eventExecutors)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new InitialHandler(ports));
            ChannelFuture future = b.bind(port).sync();

            future.addListener((ChannelFuture f) ->{
                if (f.isSuccess()){
                    log.info("绑定端口成功：{}", port);
                }else{
                    log.info("绑定失败：{}", port);
                }
            } );
            test(future.channel());
            // 关闭连接的监听
            ChannelFuture closeFuture = future.channel().closeFuture();
            closeFuture.sync();
        } catch (InterruptedException e) {
            log.warn("似乎失败了");
        }finally {
            if (eventExecutors !=null){
                eventExecutors.shutdownGracefully();
            }
        }
    }
    public DatagramPacket buildData( ){
        System.out.print("\n输入发送者的端口：");
        String line = ScannerUtil.getLine();
        Message build = Message.newBuilder().setContent(name).build();

        return new DatagramPacket(Unpooled.copiedBuffer(build.toByteArray()),
                new InetSocketAddress(AddressConfig.BROADCAST_ADDRESS,Integer.parseInt(line)));
    }
    public void test(Channel channel){
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                channel.writeAndFlush(buildData());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new RaftNode(args[0],Integer.parseInt(args[1])).runNode();
    }
}
