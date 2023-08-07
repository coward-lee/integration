package org.lee.aiguigu.netty.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdleStateEventHandler extends ChannelInboundHandlerAdapter {
    private final Logger log = LoggerFactory.getLogger(IdleStateEventHandler.class);

    /**
     * @param ctx
     * @param evt 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE: {
                    log.info("reader idle");
                    eventType = "reader idle";
                }
                break;
                case WRITER_IDLE: {
                    log.info("writer idle");
                    eventType = "writer idle";
                }
                break;
                case ALL_IDLE: {
                    log.info("all idle");
                    eventType = "all idle";
                }
                break;
                default:
                    ctx.fireUserEventTriggered(evt);
                    return;
            }
            log.info("{} time out in {}", ctx.channel().remoteAddress(), eventType);
            CharCenter.remove(ctx.channel());
        }
    }
}
