package org.lee.util;

import io.netty.channel.Channel;
import org.lee.domain.MessageProto.Message;
import org.lee.domain.MessageType;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.util.UUID;

public class SendMessageUtil {

    public static void startInputListening(Channel channel) {
        new Thread(() -> {
//            final Logger log = LoggerFactory.getLogger(MessageUtil.class);

            try {
                while (channel == null) {
//                    log.info("等待中， 连接中");
                    Thread.sleep(1000);
                }
//                log.info("input q or quit exit the input listen");
                while (true) {
                    String line = ScannerUtil.getLine();
                    if (ScannerUtil.quit(line)) {
                        return;
                    }
                    String[] split = line.split(":");
                    if (split.length != 2) {
                        continue;
                    }
                    channel.writeAndFlush(
                            Message.newBuilder()
                                    .setId(UUID.randomUUID().toString())
                                    .setTo(split[0])
                                    .setContent(split[1])
                                    .setFrom(channel.toString())
                                    .setHeader("message")
                                    .build()
                    );
                }
            } catch (Exception e) {
//                log.error("输入出现错误，", e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public static Message generateRegisterMessage(String client) {
        return Message.newBuilder()
                .setHeader(MessageType.REGISTER.getVal())
                .setFrom(client)
                .build();
    }
}
