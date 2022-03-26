package org.lee.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.lee.domain.MessageProto.Message;

import java.util.List;

public class MessageInHandler extends MessageToMessageDecoder<Message> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        System.out.println("收到了消息：【"+msg.getContent()+"");
    }
}
