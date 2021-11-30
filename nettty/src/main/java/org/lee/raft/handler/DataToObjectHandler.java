package org.lee.raft.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import org.lee.raft.domain.MessageProto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataToObjectHandler extends ChannelInboundHandlerAdapter {
    private final Logger log = LoggerFactory.getLogger(DataToObjectHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof DatagramPacket)){
            log.warn("message error");
            return;
        }
        DatagramPacket datagramPacket = (DatagramPacket)msg;
        ByteBuf in = datagramPacket.content();
        int len = in.readableBytes();
        byte[] content = new byte[len];
        in.readBytes(content, 0, len);
        Message message = Message.parseFrom(content);
        super.channelRead(ctx, message);
    }
}
