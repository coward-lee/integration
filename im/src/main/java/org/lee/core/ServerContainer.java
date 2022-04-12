package org.lee.core;

import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lee.domain.MessageProto;
import org.lee.domain.MessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerContainer {
    public final static Map<String, Channel> clientMap = new ConcurrentHashMap<>(16);
    public final static Map<String, Channel> serverMap = new ConcurrentHashMap<>(16);
    private final static Logger log = LogManager.getLogger(ServerContainer.class);

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
        log.info("【{}】登录进来了",message.getFrom());
    }
}
