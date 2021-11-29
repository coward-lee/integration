package org.lee.raft.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.lee.raft.domain.MessageProto.Message;

import java.util.List;
import java.util.Set;

class  AddPortHandler extends MessageToMessageDecoder<Message> {
    Set<Integer> ports;

    public AddPortHandler(Set<Integer> ports) {
        this.ports = ports;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        ports.add(Integer.parseInt(msg.getFrom()));
        System.out.println("收到了来自:"+msg.getFrom()+"，消息："+msg.getContent());
    }
}
