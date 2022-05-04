package org.lee.event;

import io.netty.channel.Channel;
import org.lee.core.ServerContainer;
import org.lee.domain.MessageProto.Message;
import org.lee.domain.MessageType;
import org.lee.util.PrintUtil;

public class Exchanger {

    public void doExchange(Message msg) {
        try {
            String to = msg.getTo();
            int key = ServerContainer.getCode(to);
            if (key == ServerContainer.getServerSeq()) {
                Channel channel = ServerContainer.queryClient(msg);
                if (channel != null) {
                    channel.writeAndFlush(msg);
                    return;
                }
            } else {
                System.out.print("消息转发:  ");
                Channel otherServerChannel = ServerContainer.serverMap.values().stream().toList().get(0);
                Message newMsg = Message.newBuilder()
                        .setHeader(MessageType.EXCHANGE_SEND.getVal())
                        .setFrom(msg.getFrom())
                        .setContent(msg.getContent())
                        .setId(msg.getId())
                        .setTo(msg.getTo())
                        .build();

                PrintUtil.printProtoObject(newMsg);
                otherServerChannel.writeAndFlush(newMsg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
