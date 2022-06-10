package org.lee.im.multi.im;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.lee.im.multi.config.AddressConfig;
import org.lee.im.multi.im.coder.IMProtoDecoder;
import org.lee.im.multi.im.coder.IMProtoEncoder;
import org.lee.im.multi.im.domain.MessageProto;
import org.lee.im.multi.im.domain.MessageProto.*;
import org.lee.im.multi.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class NormalServer {

    int port;
    public static Map<String, Channel> clients = null;
    public static final Logger log = LoggerFactory.getLogger(NormalServer.class);

    public NormalServer(int port) {
        this.port = port;
        clients = new ConcurrentHashMap<>();
    }


    public void runServer(){
        ServerBootstrap server = new ServerBootstrap();
        try{
            EventLoopGroup accept = new NioEventLoopGroup(1);
            EventLoopGroup work = new NioEventLoopGroup();
            server.group(accept, work)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    ;
            server.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch){
                    // out bound
                    ch.pipeline().addLast(new IMProtoEncoder());
                    ch.pipeline().addLast(new ProtobufBusinessEncoder());

                    // in bound
                    ch.pipeline().addLast(new IMProtoDecoder());
                    ch.pipeline().addLast(new ProtobufBusinessDecoder());
                }
            });
            // 等待启动成功
            ChannelFuture future = server.bind().sync();
            log.info("启动成功，服务器端口为：{}", port);
            // 关闭监听回调
            ChannelFuture close = future.channel().closeFuture();
            close.sync();
            log.warn("服务器关闭");
        }catch (Exception e){
            log.warn("出现异常");
            log.error(e.getMessage(), e);
        }
    }

    public static Message build(){
        MessageProto.Message.Builder message = MessageProto.Message.newBuilder();
        message.setContent("服务器消息");
        message.setFrom("name");
        message.setHeader("消息header");
        message.setId(UUID.randomUUID().toString());
        message.setSendTime(System.currentTimeMillis());
        return message.build();
    }

    static class ProtobufBusinessEncoder extends MessageToMessageEncoder<MessageProto.Message> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out){
           log.info("exchange  from : "+  msg.getFrom()+"，to : {}"+ msg.getTo());
            out.add(msg);
        }
    }

    static class ProtobufBusinessDecoder extends ChannelInboundHandlerAdapter {
        private final Logger log = LoggerFactory.getLogger(ProtobufBusinessDecoder.class);

        @Override
        public void channelActive(ChannelHandlerContext ctx){
            log.info("连接进来了，现在回复一个消息回去");
            ctx.channel().writeAndFlush(build());
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg){
            Message m = (Message) msg;
            printMessage(m);
            if (HeaderUtil.isLogin(m.getHeader())){
                clients.put(m.getFrom(), ctx.channel());
                return;
            }
            sendMessage(m);

        }

        private void sendMessage(Message m) {
            SocketChannel channel = (SocketChannel)clients.get(m.getTo());
            if (channel == null){
                log.warn("该客户端没有登录");
                return;
            }
//            log.info("exchange message:{} from : {}，to : {}", m.getContent(), m.getFrom(), m.getTo());
            channel.writeAndFlush(m);
        }
    }
    public static void printMessage(Message message){
       log.info(
                "收到了消息：header:"+message.getHeader()
                        +",content:"+message.getContent()
                        +",from:"+message.getFrom()
                        +",to:"+message.getTo()
                        +",id:"+message.getId()
        );
    }



    public static void main(String[] args) {
        new NormalServer(AddressConfig.SERVER_PORT).runServer();
    }

}
