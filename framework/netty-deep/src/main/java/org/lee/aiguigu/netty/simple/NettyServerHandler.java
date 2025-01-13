package org.lee.aiguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private final Logger log = LoggerFactory.getLogger(NettyServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx:" + ctx);


        ByteBuf buf = (ByteBuf) msg;
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(1000 * 10);
                log.info("executed finished");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(1000 * 1);
                ctx.writeAndFlush(Unpooled.copiedBuffer("server message to client for scheduled task", CharsetUtil.UTF_8));
                log.info("scheduled task executed finished");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 5, TimeUnit.SECONDS);

        log.info("receive:" + buf.toString(StandardCharsets.UTF_8));
        log.info("client address:" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("server message to client", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
