package org.lee.p2p;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.lee.im.multi.config.AddressConfig;
import org.lee.im.util.ScannerUtil;
import org.lee.p2p.domain.MessageProto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

public class P2PClient {

    String ip;
    int port;
    String name;
    private static final Logger log = LoggerFactory.getLogger(P2PClient.class);

    public P2PClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void runClient(){
        Bootstrap b = new Bootstrap();
        try{
            EventLoopGroup loop = new NioEventLoopGroup();
            b.group(loop)
                    .channel(NioDatagramChannel.class);
            b.handler(new ChannelInitializer<DatagramChannel>() {
                @Override
                protected void initChannel(DatagramChannel ch){
                    ch.pipeline()
                            .addLast(new LoggingHandler(LogLevel.INFO))
                            .addLast(new P2PBusinessDecoder())
                            ;
//                            .addLast(new OutPrint());
                }
            });
            ChannelFuture sync = b.bind(port).sync();
            log.info("绑定端口成功："+port);
            test(sync.channel());

            ChannelFuture close = sync.channel().closeFuture();
            close.sync();
            log.info("连接关闭   ==== === = = == = = = =");
        }catch (Exception e){
            log.warn("出现异常");
            log.error(e.getMessage(), e);
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

    static class P2PBusinessDecoder extends SimpleChannelInboundHandler<DatagramPacket> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg){
            ByteBuf m =  msg.content();
            int len = m.readableBytes();
            byte[] bytes = new byte[len];
            m.readBytes(bytes,0, len);
            log.info("收到了消息：" + new String(bytes, 0, len, CharsetUtil.UTF_8));
    }
    }

    static class OutPrint extends MessageToByteEncoder<DatagramPacket> {

        @Override
        protected void encode(ChannelHandlerContext ctx, DatagramPacket msg, ByteBuf out) throws Exception {
            ByteBuf content =msg.content();
            int len = content.readableBytes();
            byte[] bytes = new byte[len];
            content.readBytes(bytes,0, len);
            log.info("客户端发送了 ： "+new String(bytes, 0, len, CharsetUtil.UTF_8));
//            out.writeByte(ms)
        }
    }
    public void test( Channel channel){
        try {
            Thread.sleep(1000);
            for (int i = 0; i < 10; i++) {
                ByteBuf buf = Unpooled.copiedBuffer("发送消息".getBytes());
                channel.writeAndFlush(buildData(buf));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


//    public void startInputListening(Channel channel){
//        new Thread(()->{
//            try {
//                // 登录请求
//                DatagramChannel udp = (DatagramChannel) channel;
//                udp.config().ad
//                Message loginInfo = loginInfo();
//                channel.writeAndFlush(loginInfo);
//
//                while (true) {
//                    Message message = sendMessage();
//                    if (ScannerUtil.quit(message.getContent())){
//                        return;
//                    }
//                    channel.writeAndFlush(message);
//                }
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }

//    // 登录操作
//    public Message loginInfo(){
//        log.info("开始登录");
//        Message.Builder m = Message.newBuilder();
//        System.out.print("请输入用户名：");
//        String line = ScannerUtil.getLine();
//        m.setFrom(line);
//        this.name = line;
//        m.setContent("登录操作");
//        m.setHeader(ClientAction.LOGIN.name());
//        m.setId(UUID.randomUUID().toString());
//        m.setSendTime(System.currentTimeMillis());
//        m.setTo("server");
//        return m.build();
//    }
//    public Message sendMessage(){
//        log.info("发送消息");
//        Message.Builder m = Message.newBuilder();
//        System.out.print("输入消息：");
//        String line = ScannerUtil.getLine();
//        m.setContent(line);
//        System.out.print("输入发送的用户:");
//        line = ScannerUtil.getLine();
//        m.setTo(line);
//        m.setFrom(this.name);
//        m.setHeader(ClientAction.SEND_MESSAGE.name());
//        m.setId(UUID.randomUUID().toString());
//        m.setSendTime(System.currentTimeMillis());
//        return m.build();
//    }

    public DatagramPacket buildData(ByteBuf buf){
        System.out.print("\n请输入发送者的端口：");
        String line = ScannerUtil.getLine();
        int itemPort = Integer.parseInt(line);
        InetSocketAddress address = new InetSocketAddress(AddressConfig.ADDRESS,itemPort);
        return new DatagramPacket(buf, address);
    }



    public static void main(String[] args) {
        new P2PClient(AddressConfig.ADDRESS, Integer.parseInt(args[0])).runClient();
    }
}
