package org.lee.aiguigu.netty.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatServerChannelHandler extends SimpleChannelInboundHandler<String> {

    private final Logger log = LoggerFactory.getLogger(ChatServerChannelHandler.class);
    protected ChatServerChannelHandler() {
        super();
    }

    protected ChatServerChannelHandler(boolean autoRelease) {
        super(autoRelease);
    }

    protected ChatServerChannelHandler(Class<? extends String> inboundMessageType) {
        super(inboundMessageType);
    }

    protected ChatServerChannelHandler(Class<? extends String> inboundMessageType, boolean autoRelease) {
        super(inboundMessageType, autoRelease);
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("{}, online", ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("{}, offline", ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void ensureNotSharable() {
        super.ensureNotSharable();
    }

    @Override
    public boolean isSharable() {
        return super.isSharable();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        CharCenter.add(ctx.channel());
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        CharCenter.remove(ctx.channel());
        super.handlerRemoved(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("message from :{} --- {}",ctx.channel().remoteAddress(),msg);
        CharCenter.foreach(channel -> {
            if (channel == ctx.channel()){
                return;
            }
            try{
                channel.writeAndFlush(msg);
            }catch (Throwable e){
                log.info(e.getMessage(), e);
            }
        });
    }
}
