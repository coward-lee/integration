package org.lee.event;

import io.netty.channel.Channel;
import org.lee.core.NodesContainer;
import org.lee.core.ServerContainer;
import org.lee.domain.MessageProto;
import org.lee.zk.ZkClient;


public class Register {
    ZkClient zkClient = ZkClient.getZkClient();

    public void doRegister(MessageProto.Message message, Channel channel){
        int serverSeq = ServerContainer.getServerSeq();
        if (ServerContainer.getCode(message.getFrom()) == ServerContainer.getServerSeq()){
            ServerContainer.registerClient(message, channel);
        }else{
            String nodeName = NodesContainer.getNode(serverSeq).getNodeName();
            Channel otherChannel = ServerContainer.queryServerNode(nodeName);
            otherChannel.writeAndFlush(message);
        }
    }
}
