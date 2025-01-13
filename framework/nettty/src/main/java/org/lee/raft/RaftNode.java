package org.lee.raft;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.lee.im.multi.config.AddressConfig;
import org.lee.im.util.ScannerUtil;
import org.lee.raft.enums.NodeAction;
import org.lee.raft.handler.AddPortHandler;
import org.lee.raft.handler.DataToObjectHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.lee.raft.domain.MessageProto.Message;
import org.lee.raft.domain.MessageProto.ElectionMessage;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RaftNode {
    private final Logger log = LoggerFactory.getLogger(RaftNode.class);
    // 所以的节点端口
    private static final Set<Integer> ports = new HashSet<>();
    // 选举轮次，逻辑时钟
    private final AtomicInteger epoch = new AtomicInteger(0);
    // 是否选举结束，用于选举最后的一次 accepted  by majorities 的消息通知的置为
    private final AtomicBoolean isDOne = new AtomicBoolean(false);
    // node的提议
    private String acceptPropose;

    private Set<Integer> acceptors;


    String propose;
    int port;

    public RaftNode(String propose, int port) {
        ports.add(10001);
        ports.add(10002);
        ports.add(10003);
        this.port = port;
        this.propose = propose;
        acceptors = new HashSet<>();
        acceptPropose = "propose:"+port;
    }

    public void runNode(){
        NioEventLoopGroup eventExecutors = null;
        try {

            eventExecutors = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(eventExecutors)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        protected void initChannel(DatagramChannel ch){
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new DataToObjectHandler());
                            pipeline.addLast(new AddPortHandler(ports));
                            pipeline.addLast(new RaftAcceptorHandler());
                        }
                    });
            ChannelFuture future = b.bind(port).sync();

            future.addListener((ChannelFuture f) ->{
                if (f.isSuccess()){
                    log.info("绑定端口成功：{}", port);
                }else{
                    log.info("绑定失败：{}", port);
                }
            } );

            log.info("enter 开始选举过程");
            ScannerUtil.getLine();

            // 选举操作
            election(future.channel());

            waitAcceptor(1);
            // 关闭连接的监听
            ChannelFuture closeFuture = future.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            log.warn("似乎失败了");
        }finally {
            if (eventExecutors !=null){
                eventExecutors.shutdownGracefully();
            }
        }
    }

    private void election(Channel channel) {
        while (true) {
            // 发送广播，但是由于是端口没法广播端口。

            ports.forEach(p -> {
                DatagramPacket datagramPacket = buildPropose(p);
                channel.writeAndFlush(datagramPacket);
            });
            log.info("send propose completely");

            waitAcceptor(20);

            if (isDOne.get()){
                log.info("selection is done, terminal propose is {}", acceptPropose);
                return;
            }
            // 这一轮选举失败，开始新一轮
            startNewEpoch();
        }
    }

    private void waitAcceptor(int second){
        try {
            Thread.sleep(second* 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public DatagramPacket buildPropose(Integer targetPort){

        ElectionMessage content = ElectionMessage.newBuilder()
                .setEpoch(String.valueOf(epoch.get()))
                .setValue(propose)
                .build();

        Message build = Message.newBuilder()
                .setContent(content)
                .setFrom(String.valueOf(port))
                .setHeader(NodeAction.PROPOSE.name())
                .build();

        return new DatagramPacket(
                Unpooled.copiedBuffer(build.toByteArray()),
                new InetSocketAddress(AddressConfig.BROADCAST_ADDRESS,targetPort)
        );
    }

    public void responsePropose(Channel channel, Message message){
        ElectionMessage content = message.getContent();
        int proposeEpoch = Integer.parseInt(content.getEpoch());
        // 以及选举出新的leader
        if (isDOne.get()){
            log.info("拒绝了：from:{}，propose:{}",message.getFrom(), message.getContent().getValue());
            refuse(channel, message);
            return;
        }

        // 为选举出新leader
        if (proposeEpoch > epoch.get()){
            log.info("接收了：from:{}，propose:{}",message.getFrom(), message.getContent().getValue());
            accept(channel, message);
        }else{
            log.info("拒绝了：from:{}，propose:{}",message.getFrom(), message.getContent().getValue());
            refuse(channel, message);
        }
    }
    // 接收一个提议,见自己的value和epoch设置为对应的值
    public void accept(Channel channel,Message message){
        epoch.set(Integer.parseInt(message.getContent().getEpoch()));
        acceptPropose = message.getContent().getValue();
        channel.writeAndFlush(buildAccepting(message));
    }

    public DatagramPacket buildAccepting(Message message){

        Message build = Message.newBuilder()
                .setContent(message.getContent())
                .setFrom(String.valueOf(port))
                .setHeader(NodeAction.ACCEPT.name())
                .build();
        return new DatagramPacket(
                Unpooled.copiedBuffer(build.toByteArray()),
                new InetSocketAddress(AddressConfig.ADDRESS,Integer.parseInt(message.getFrom()))
        );
    }

    // 拒绝一个提议
    public void refuse(Channel channel, Message message){
        channel.writeAndFlush(buildRefuse(message));
    }
    public DatagramPacket buildRefuse(Message message){

        ElectionMessage content = ElectionMessage.newBuilder()
                .setEpoch(String.valueOf(epoch))
                .setValue(propose)
                .build();

        Message build = Message.newBuilder()
                .setContent(content)
                .setFrom(String.valueOf(port))
                .setHeader(NodeAction.ACCEPT.name())
                .build();

        return new DatagramPacket(
                Unpooled.copiedBuffer(build.toByteArray()),
                new InetSocketAddress(AddressConfig.ADDRESS,Integer.parseInt(message.getFrom()))
        );
    }

    // 处理接受请求
    public void dealAcceptResp(Channel channel, Message message) {
        acceptors.add(Integer.parseInt(message.getFrom()));
        int acceptorNum = acceptors.size();
        // major
        if (acceptorNum > ports.size()/2) {
            doElectionDone();
            for (Integer p : ports) {
                sendSelectionDone(channel, p);
            }
        }
    }

    // 开始新纪元
    public void startNewEpoch(){
        log.info("当前的被接受的人数{}", acceptors.size());
        acceptors = new HashSet<>();
        int i = epoch.incrementAndGet();
        log.info("Elect fail in epoch {}, starting new epoch",i);
    }

    // 选举结束的通知
    private void sendSelectionDone(Channel channel, int port) {

        ElectionMessage content = ElectionMessage.newBuilder()
                .setValue(acceptPropose)
                .setEpoch(String.valueOf(epoch.get()))
                .build();

        Message build = Message.newBuilder()
                .setContent(content)
                .setFrom(String.valueOf(this.port))
                .setHeader(NodeAction.ELECTION_DONE.name())
                .build();
        DatagramPacket datagramPacket = new DatagramPacket(
                Unpooled.copiedBuffer(build.toByteArray()),
                new InetSocketAddress(AddressConfig.BROADCAST_ADDRESS, Integer.parseInt(String.valueOf(port)))
        );

        channel.writeAndFlush(datagramPacket);
    }


    // 设置选举状态
    private void doElectionDone() {
        isDOne.getAndSet( true);
    }



    public class RaftAcceptorHandler extends MessageToMessageDecoder<Message>{

        private final Logger log = LoggerFactory.getLogger(RaftAcceptorHandler.class);

        @Override
        protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) {
            log.info("收到消息：header:{},from：{}，propose:{},epoch:{}",msg.getHeader(),msg.getFrom(),msg.getContent().getValue(),msg.getContent().getEpoch());
            dealRaftMessage(ctx.channel(), msg);
        }

        public void dealRaftMessage(Channel channel,Message message){
            if       (NodeAction.ELECTION_DONE.name().equalsIgnoreCase(message.getHeader())){
                log.info("{}，ELECTION_DONE ",message.getFrom());
                doElectionDone();
            }else if (NodeAction.HEART_BEAT.name().equalsIgnoreCase(message.getHeader())){
                log.info("{}，心跳检测",message.getFrom());
            }else if (NodeAction.QUESTION.name().equalsIgnoreCase(message.getHeader())){
                log.info("{}，question",message.getFrom());
            }else if (NodeAction.PROPOSE.name().equalsIgnoreCase(message.getHeader())){
                log.info("{}， 来了propose", message.getFrom());
                responsePropose(channel, message);
            }else if (NodeAction.ACCEPT.name().equalsIgnoreCase(message.getHeader())){
                log.info("{}， 来了 accept", message.getFrom());
                dealAcceptResp(channel, message);
            }else if (NodeAction.JOIN.name().equalsIgnoreCase(message.getHeader())){
                log.info("{}，服务加入",message.getFrom());
            }
        }
    }


    public static void main(String[] args) {
        new RaftNode(args[0],Integer.parseInt(args[1])).runNode();
    }
}
