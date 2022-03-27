package org.lee.core;

import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import org.lee.domain.MessageProto;
import org.lee.domain.MessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerContainer {
    public final static Map<String, Channel> clientMap = new ConcurrentHashMap<>(16);

    public static Channel queryClient(MessageProto.Message message){
        if (message == null || StringUtil.isNullOrEmpty(message.getTo())){
            return null;
        }
        String to = message.getTo();
        return clientMap.get(to);
    }

    public static void registerClient(MessageProto.Message message,Channel channel){
        if (message == null || StringUtil.isNullOrEmpty(message.getFrom()) || !MessageType.isRegister(message.getHeader())){
            throw new IllegalArgumentException("register error,"+message.getHeader());
        }
        clientMap.put(message.getFrom(), channel);
        System.out.println("register successfullly");
    }
}
