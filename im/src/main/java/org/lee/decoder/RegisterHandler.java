package org.lee.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.lee.core.ServerContainer;
import org.lee.domain.MessageProto.Message;
import org.lee.domain.MessageType;
import org.lee.event.Register;
import org.lee.event.RegisterServer;

import java.util.List;

public class RegisterHandler extends MessageToMessageDecoder<Message> {
    Register register = new Register();
    RegisterServer registerServer = new RegisterServer();
    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        if (MessageType.isRegister(msg.getHeader())){
            register.doRegister(msg,ctx.channel());
            return;
        }
        if (MessageType.JOIN.getVal().equalsIgnoreCase(msg.getHeader())){
            registerServer.doRegister(msg, ctx.channel());
        }
        out.add(msg);
    }

}
