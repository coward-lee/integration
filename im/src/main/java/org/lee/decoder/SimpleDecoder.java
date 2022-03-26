package org.lee.client.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SimpleDecoder extends ByteToMessageDecoder {
    private final Logger log = LoggerFactory.getLogger(SimpleDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        System.out.println("有消息来了");
        log.info("有消息来了");
        int len = in.readableBytes();
        byte[] buf = new byte[len];
        in.readBytes(buf, 0, len);
        String s = new String(buf, 0, len, CharsetUtil.UTF_8);
        log.info("receive:{}", s);
        out.add(s);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("有信息连接来了");
        super.channelActive(ctx);
    }
}