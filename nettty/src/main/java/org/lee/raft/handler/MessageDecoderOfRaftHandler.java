package org.lee.raft.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MessageDecoderOfRaftHandler extends ByteToMessageDecoder {
    private final Logger log = LoggerFactory.getLogger(MessageDecoderOfRaftHandler.class);
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int len = in.readableBytes();
        byte[] content = new byte[len];
        in.readBytes(content, 0, len);
        log.info("接收到消息 : {}", content);
        out.add(in);
    }
}
