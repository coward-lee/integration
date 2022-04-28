package org.lee.event;

import io.netty.channel.Channel;
import org.lee.core.NodesContainer;
import org.lee.core.ServerContainer;
import org.lee.domain.MessageProto.Message;
import org.lee.zk.ZkClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Exchanger {
    ZkClient zkClient = ZkClient.getZkClient();

    public void doExchange(Message msg){
        String from = msg.getFrom();
        int key = from.hashCode() % 4;
        if (key == ServerContainer.getServerSeq()){
            Channel channel = ServerContainer.queryClient(msg);
            if (channel != null){
                channel.writeAndFlush(msg);
                return;
            }
        }else{
            List<String> allNode = zkClient.getAllNode();
            Map<Integer, Node> nodes = allNode.stream().map(node -> {
                String data = zkClient.getData(node);
                return new Node(node, Integer.valueOf(data));
            }).collect(Collectors.toMap(Node::getNodeKey, Function.identity()));

            NodesContainer.nodes.putAll(nodes);
            Node node = NodesContainer.getNode(key);
            Channel channel = ServerContainer.queryServerNode(node.getNodeName());
            channel.writeAndFlush(msg);
        }


    }
}
