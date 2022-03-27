package org.lee.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.lee.core.ServerContainer;
import org.lee.domain.MessageProto.Message;
import org.lee.domain.MessageType;

import java.util.List;

public class RegisterHandler extends MessageToMessageDecoder<Message> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        if (MessageType.isRegister(msg.getHeader())){
            ServerContainer.registerClient(msg, ctx.channel());
            return;
        }
        out.add(msg);
    }

}
