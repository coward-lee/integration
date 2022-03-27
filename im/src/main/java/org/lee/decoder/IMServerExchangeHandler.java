package org.lee.decoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.lee.core.ServerContainer;
import org.lee.domain.MessageProto.Message;

import java.util.List;

public class IMServerExchangeHandler extends MessageToMessageDecoder<Message> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) {
        Channel channel = ServerContainer.queryClient(msg);
        channel.writeAndFlush(msg);
    }
}
