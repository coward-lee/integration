package org.lee.im.multi.im;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.lee.im.multi.config.AddressConfig;
import org.lee.im.multi.config.ClientAction;
import org.lee.im.multi.im.coder.IMProtoDecoder;
import org.lee.im.multi.im.coder.IMProtoEncoder;
import org.lee.im.multi.im.domain.MessageProto;
import org.lee.im.multi.im.domain.MessageProto.*;
import org.lee.im.multi.util.MessageUtil;
import org.lee.im.util.ScannerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class NormalClient {

    String ip;
    int port;
    String name;
    private final Logger log = LoggerFactory.getLogger(NormalServer.class);

    public NormalClient(String ip, int port, String name) {
        this.ip = ip;
        this.port = port;
        this.name = name;
    }

    public NormalClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void runClient(){
        Bootstrap b = new Bootstrap();
        try{
            EventLoopGroup loop = new NioEventLoopGroup();
            b.group(loop)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(ip, port)
            ;
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // out bound
                    ch.pipeline().addLast(new IMProtoEncoder());
                    ch.pipeline().addLast(new OutPrint());

                    // in bound
                    ch.pipeline().addLast(new IMProtoDecoder());
                    ch.pipeline().addLast(new ProtobufBusinessDecoder());
                }
            });
            ChannelFuture future = b.connect();
            log.info("连接 {address:{}, port:{}} 成功，", ip, port);
            future.addListener(f ->{
                if (f.isSuccess()){
                    log.info("连接成功");
                    MessageUtil.startInputListening(future.channel(),NormalClient.class);
                }else{
                    log.info("连接失败");
                }
            });
            future.sync();
            Channel channel =  future.channel();

//            login(channel);

//            test(channel);
            startInputListening((SocketChannel) channel);

            ChannelFuture close = future.channel().closeFuture();
            close.sync();
            log.info("连接关闭   ==== === = = == = = = =");
        }catch (Exception e){
            log.warn("出现异常");
            log.error(e.getMessage(), e);
        }
    }
    public void test(Channel channel) throws InterruptedException {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(2000);
                Message message = build(i);
                log.info("发送了消息：{}", message);
                channel.writeAndFlush(message);
            }
            channel.flush();
    }

    // 登录操作
    public void login(Channel channel){
        log.info("开始登录");
        MessageProto.Message.Builder m = MessageProto.Message.newBuilder();
        m.setContent("登录操作");
        m.setFrom(this.name);
        m.setHeader(ClientAction.LOGIN.name());
        m.setId(UUID.randomUUID().toString());
        m.setSendTime(System.currentTimeMillis());
        channel.writeAndFlush(m);
    }

    // 构建测试对象
    public Message build(int i){
        MessageProto.Message.Builder message = MessageProto.Message.newBuilder();
        message.setContent("一个发送的内容");
        message.setFrom(name);
        for (String s : names) {
            if (!s.equalsIgnoreCase(name)){
                message.setTo(s);
                break;
            }
        }
        message.setHeader("消息header");
        message.setId(UUID.randomUUID().toString());
        message.setSendTime(System.currentTimeMillis());
        return message.build();
    }

    static class ProtobufBusinessDecoder extends ChannelInboundHandlerAdapter {
        private final Logger log = LoggerFactory.getLogger(ProtobufBusinessDecoder.class);
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            MessageProto.Message m = (MessageProto.Message) msg;

            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            log.info("id : {}", m.getContent());
            log.info("sendTime : {}", m.getSendTime());
            log.info("header : {}", m.getHeader());
            log.info("content : {}", m.getContent());
            log.info("from : {}", m.getFrom());
            log.info("to : {}", m.getTo());
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        }
    }
    static class OutPrint extends MessageToMessageEncoder<Message> {
        private final Logger log = LoggerFactory.getLogger(NormalServer.ProtobufBusinessEncoder.class);

        @Override
        protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
            System.out.println("客户端发送了消息：" +msg);
            out.add(msg);
        }
    }


    public void startInputListening(SocketChannel channel){
        new Thread(()->{
            try {
                // 登录请求
                Message loginInfo = loginInfo();
                channel.writeAndFlush(loginInfo);

                while (true) {
                    Message message = sendMessage();
                    if (ScannerUtil.quit(message.getContent())){
                        return;
                    }
                    channel.writeAndFlush(message);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 登录操作
    public Message loginInfo(){
        System.out.println("开始登录");
        Message.Builder m = Message.newBuilder();
        System.out.print("请输入用户名：");
        String line = ScannerUtil.getLine();
        m.setFrom(line);
        this.name = line;
        m.setContent("登录操作");
        m.setHeader(ClientAction.LOGIN.name());
        m.setId(UUID.randomUUID().toString());
        m.setSendTime(System.currentTimeMillis());
        m.setTo("server");
        return m.build();
    }
    public Message sendMessage(){

        System.out.println("发送消息");
        Message.Builder m = Message.newBuilder();

        System.out.print("输入消息：");
        String line = ScannerUtil.getLine();
        m.setContent(line);

        System.out.print("输入发送的用户:");
        line = ScannerUtil.getLine();
        m.setTo(line);

        m.setFrom(this.name);
        m.setHeader(ClientAction.SEND_MESSAGE.name());
        m.setId(UUID.randomUUID().toString());
        m.setSendTime(System.currentTimeMillis());
        return m.build();
    }



    public static String[] names = {"name1", "name2"};
    public static void main(String[] args) {
        new NormalClient(AddressConfig.ADDRESS, AddressConfig.SERVER_PORT).runClient();
    }
}
