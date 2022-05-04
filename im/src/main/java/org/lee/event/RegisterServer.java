package org.lee.event;

import io.netty.channel.Channel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.lee.core.NodesContainer;
import org.lee.core.ServerContainer;
import org.lee.domain.MessageProto;
import org.lee.domain.MessageType;
import org.lee.zk.ZkClient;


public class RegisterServer {
    private final Logger log = LogManager.getLogger(RegisterServer.class);


    public void doRegister(MessageProto.Message message, Channel channel){

        ServerContainer.registerServer(message.getFrom(), channel);
//        MessageProto.Message msg = MessageProto.Message.newBuilder()
//                .setContent("注册到服务器成功")
//                .setHeader(MessageType.ACCEPT.getVal())
//                .setFrom("server")
//                .setTo("server")
//                .build();
        System.out.println("服务器注册到服务器");
//        channel.writeAndFlush(msg);
    }
}
