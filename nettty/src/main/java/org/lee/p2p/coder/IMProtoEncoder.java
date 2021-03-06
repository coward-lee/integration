package org.lee.p2p.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.lee.im.multi.im.domain.MessageProto.Message;

public class IMProtoEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        byte[] bytes = msg.toByteArray();
        int length = bytes.length;
        out.writeInt(length);
        out.writeBytes(bytes);
    }
}
