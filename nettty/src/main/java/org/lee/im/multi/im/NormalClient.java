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
                protected void initChannel(SocketChannel ch){
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
                    startInputListening(future.channel());
                }else{
                    log.info("连接失败");
                }
            });
            ChannelFuture close = future.channel().closeFuture();
            close.sync();
            log.info("连接关闭   ==== === = = == = = = =");
        }catch (Exception e){
            log.warn("出现异常");
            log.error(e.getMessage(), e);
        }
    }

    public static void printMessage(Message message){
        System.out.println(
                "收到了消息：header:"+message.getHeader()
                        +",content:"+message.getContent()
                        +",from:"+message.getFrom()
                        +",to:"+message.getTo()
                        +",id:"+message.getId()
        );
    }

    static class ProtobufBusinessDecoder extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            MessageProto.Message m = (MessageProto.Message) msg;
            printMessage(m);
        }
    }

    static class OutPrint extends MessageToMessageEncoder<Message> {
        @Override
        protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) {
            System.out.println("客户端发送了消息：" +msg);
            out.add(msg);
        }
    }


    public void startInputListening(Channel channel){
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



    public static void main(String[] args) {
        new NormalClient(AddressConfig.ADDRESS, AddressConfig.SERVER_PORT).runClient();
    }
}
