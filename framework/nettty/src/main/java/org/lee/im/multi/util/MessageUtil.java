package org.lee.im.multi.util;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.lee.im.multi.im.NormalServer;
import org.lee.im.util.ScannerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageUtil {

    public static void startInputListening(Channel channel, Class cls){
        new Thread(()->{
            final Logger log = LoggerFactory.getLogger(cls);

            try {
                while (channel == null){
                    System.out.println("等待中， 连接中");
                    Thread.sleep(1000);
                }
                System.out.println("input q or quit exit the input listen");
                while (true) {
                    String line = ScannerUtil.getLine();
                    if (ScannerUtil.quit(line)){
                        return;
                    }
                    System.out.println("服务端发送了     "+ line);
                    channel.writeAndFlush(Unpooled.copiedBuffer(line.getBytes()));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
//    public static void startInputListening()
}
