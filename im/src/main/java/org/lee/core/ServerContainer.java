package org.lee.core;

import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lee.domain.MessageProto;
import org.lee.domain.MessageType;
import org.lee.event.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerContainer {
    public final static Map<String, Channel> clientMap = new ConcurrentHashMap<>(16);
    public final static Map<String, Channel> serverMap = new ConcurrentHashMap<>(16);


    private final static Logger log = LogManager.getLogger(ServerContainer.class);
    private static int serverSeq = 0;


    public static Channel queryClient(MessageProto.Message message) {
        if (message == null || StringUtil.isNullOrEmpty(message.getTo())) {
            return null;
        }
        String to = message.getTo();
        return clientMap.get(to);
    }

    public static void registerClient(MessageProto.Message message, Channel channel) {
        if (message == null || StringUtil.isNullOrEmpty(message.getFrom()) || !MessageType.isRegister(message.getHeader())) {
            throw new IllegalArgumentException("register error," + message.getHeader());
        }
        clientMap.put(message.getFrom(), channel);
        log.info("【{}】登录进来了", message.getFrom());
    }

    public static Channel queryServerNode(String nodeName) {
        return serverMap.get(nodeName);
    }

    public static void registerServer(String nodeName, Channel channel) {
        serverMap.put(nodeName, channel);
    }

    public static void setServerSeq(int serverSeq1) {
        serverSeq = serverSeq1;
    }

    public static int getServerSeq() {
        return serverSeq;
    }

    public static int getCode(String nodeName) {
        if (nodeName.equalsIgnoreCase("c1")){
            return 1;
        }
        if (nodeName.equalsIgnoreCase("c0")){
            return 0;
        }
        return Math.abs(nodeName.hashCode() % 2);
    }

}
