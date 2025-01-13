package org.lee.im.multi.util;

import io.netty.channel.Channel;
import org.lee.im.multi.im.domain.MessageProto;

import java.util.UUID;

public class TestUtil {
    // 小消息发送测试
    public void test(Channel channel) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(2000);
            MessageProto.Message message = build("from", "to");
            channel.writeAndFlush(message);
        }
        channel.flush();
    }


    // 构建测试对象
    public MessageProto.Message build(String from, String to){
        MessageProto.Message.Builder message = MessageProto.Message.newBuilder();
        message.setContent("一个发送的内容");
        message.setFrom(from);
        message.setTo(to);
        message.setHeader("消息header");
        message.setId(UUID.randomUUID().toString());
        message.setSendTime(System.currentTimeMillis());
        return message.build();
    }
}
