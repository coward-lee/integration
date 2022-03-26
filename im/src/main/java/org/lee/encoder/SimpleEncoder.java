package org.lee.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class SimpleEncoder extends MessageToByteEncoder<String> {
    private final Logger log = LoggerFactory.getLogger(SimpleEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        System.out.println("发送消息？？？");
        log.info("send：{}", msg);
        out.writeBytes(msg.getBytes(StandardCharsets.UTF_8));
    }

}