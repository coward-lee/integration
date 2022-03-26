package org.lee.util;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageUtil {
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
                    log.info("发送了消息:【{}】", line);
                    channel.writeAndFlush(Unpooled.copiedBuffer(line.getBytes()));
                }
            }
            catch (Exception e) {
                log.error("输入出现错误，",e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }
//    public static void startInputListening()
}
