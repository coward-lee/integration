package org.lee.event;

import io.netty.channel.Channel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.lee.core.NodesContainer;
import org.lee.core.ServerContainer;
import org.lee.domain.MessageProto;
import org.lee.zk.ZkClient;


public class Register {
    private final Logger log = LogManager.getLogger(Register.class);
    ZkClient zkClient = ZkClient.getZkClient();

    public void doRegister(MessageProto.Message message, Channel channel){
        System.out.println("hash 出来的 seq"+ServerContainer.getCode(message.getFrom()));
        if (ServerContainer.getCode(message.getFrom()) == ServerContainer.getServerSeq()){
            System.out.println("注册到了自己的服务器");
            ServerContainer.registerClient(message, channel);
        }else{
            System.out.println("注册到了其他的服务器");
            Channel otherChannel = ServerContainer.serverMap.values().stream().toList().get(0);
            otherChannel.writeAndFlush(message);
        }
    }
}
