package org.lee.util;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lee.domain.MessageProto.Message;
import org.lee.domain.MessageType;

import java.util.UUID;

public class SendMessageUtil {

    Channel channel;
    String from;

    public SendMessageUtil(String from) {
        this.from = from;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void startInputListening() {
        this.channel = channel;
        new Thread(() -> {
            final Logger log = LogManager.getLogger(MessageUtil.class);

            try {
                while (channel == null) {
                    log.info("等待中， 连接中");
                    Thread.sleep(1000);
                }
                log.info("input q or quit exit the input listen");
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
                                    .setFrom(from)
                                    .setHeader("message")
                                    .build()
                    );
                }
            } catch (Exception e) {
                log.error("输入出现错误，", e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public void sendMessage(String to, String content){
        channel.writeAndFlush(
                Message.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setTo(to)
                        .setContent(content)
                        .setFrom(from)
                        .setHeader("message")
                        .build()
        );
    }

    public static Message generateRegisterMessage(String client) {
        return Message.newBuilder()
                .setContent("")
                .setTo("server")
                .setHeader(MessageType.REGISTER.getVal())
                .setFrom(client)
                .build();
    }
}
