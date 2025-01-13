package org.lee.raft.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.lee.raft.domain.MessageProto.Message;
import org.lee.raft.enums.NodeAction;

import java.util.List;
import java.util.Set;

public class  AddPortHandler extends MessageToMessageDecoder<Message> {
    Set<Integer> ports;

    public AddPortHandler(Set<Integer> ports) {
        this.ports = ports;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        if (NodeAction.JOIN.name().equalsIgnoreCase(msg.getHeader())){
            ports.add(Integer.parseInt(msg.getFrom()));
            System.out.println("header加入服务，from："+msg.getFrom()+"，消息："+msg.getContent());
        }
        out.add(msg);
    }
}
