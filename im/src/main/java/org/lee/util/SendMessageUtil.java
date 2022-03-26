package org.lee.util;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.lee.domain.MessageProto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class SendMessageUtil {

    public static void startInputListening(Channel channel){
        new Thread(()->{
            final Logger log = LoggerFactory.getLogger(MessageUtil.class);

            try {
                while (channel == null){
                    log.info("等待中， 连接中");
                    Thread.sleep(1000);
                }
                log.info("input q or quit exit the input listen");
                while (true) {
                    String line = ScannerUtil.getLine();
                    if (ScannerUtil.quit(line)){
                        return;
                    }
                    channel.writeAndFlush(
                            Message.newBuilder()
                                    .setId(UUID.randomUUID().toString())
                                    .setContent(line)
                                    .setFrom(channel.toString())
                                    .setHeader("message")
                                    .build()
                    );
                }
            }
            catch (Exception e) {
                log.error("输入出现错误，",e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
