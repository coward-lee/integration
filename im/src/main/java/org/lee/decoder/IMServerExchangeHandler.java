package org.lee.decoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lee.Server;
import org.lee.core.ServerContainer;
import org.lee.domain.MessageProto.Message;

import java.util.List;

public class IMServerExchangeHandler extends MessageToMessageDecoder<Message> {
    private final Logger log = LogManager.getLogger(IMServerExchangeHandler.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) {
        Channel channel = ServerContainer.queryClient(msg);
        if (channel != null){
            channel.writeAndFlush(msg);
        }
        String to = msg.getTo();
        log.info("目标客户端：{}不在此服务器上：",to);

    }
}
