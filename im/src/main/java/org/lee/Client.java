package org.lee;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.lee.decoder.IMProtoDecoder;
import org.lee.decoder.MessageInHandler;
import org.lee.domain.MessageProto;
import org.lee.domain.MessageType;
import org.lee.encoder.IMProtoEncoder;
import org.lee.util.CustomConfigurationFactory;
import org.lee.util.SendMessageUtil;

import java.io.IOException;


public class Client {
    private final Logger log = LogManager.getLogger(Client.class);

    private final Integer port;
    private final String addr;
    private final String clientId;

    public Client(Integer port, String addr, String clientId) {
        this.port = port;
        this.addr = addr;
        this.clientId = clientId;
    }

    private Bootstrap b = new Bootstrap();
    private Channel channel = null;

    public void runClient() {
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
            ChannelFuture connect = b.connect().sync();
            connect.addListener(f->{
                if (f.isSuccess()){
                    Channel channel = connect.channel();
                    this.channel = channel;
                    channel.writeAndFlush("connect success");
                    // 注册自己的channel到服务器
                    register(channel);
                    // 开启输入监听
                    SendMessageUtil.startInputListening(channel, clientId);
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
    public void sendMessage(MessageProto.Message message){
        if (channel!=null){
            channel.writeAndFlush(message);
        }
    }

    public void close(){
        if (channel != null) {
            channel.close();
        }
    }

    public void register(Channel channel){
        MessageProto.Message message = SendMessageUtil.generateRegisterMessage(clientId);
        channel.writeAndFlush(message);
    }

    public static void run(String ip, Integer port, String clientId) throws IOException {
        for (int i = 0; i < 100; i++) {
            String from = "args[0]"+i;
            Client client = new Client(port, ip, clientId);
            client.runClient();
            client.sendMessage(MessageProto.Message.newBuilder()
                            .setTo("args[0]"+(i-1))
                            .setFrom(from)
                            .setHeader(MessageType.SEND.getVal())
                            .setContent("发送一个消息"+i)
                    .build());
            client.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Main.parseArgs(args);

        CustomConfigurationFactory customConfigurationFactory = new CustomConfigurationFactory();
        Configurator.initialize(customConfigurationFactory.getConfiguration());

        Integer port = Integer.valueOf(Main.port);
        String ip = "localhost";
        for (int i = 0; i < 100; i++) {
            String from = "args[0]"+i;
            int finalI = i;
            new Thread(()->{

                Client client = new Client(port, ip, from);
                client.runClient();
                client.sendMessage(MessageProto.Message.newBuilder()
                        .setTo("args[0]"+(finalI -1))
                        .setFrom(from)
                        .setHeader(MessageType.SEND.getVal())
                        .setContent("发送一个消息"+ finalI)
                        .build());
                client.close();
            }, "线程【"+i+"】").start();
        }
    }
}
