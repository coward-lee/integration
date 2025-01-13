package org.lee.netty.netty.cahnnel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;

public class InPipeline {
    static class SimpleInHandlerA extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("in in in in in in  AAAAAAAAAAA");
            super.channelRead(ctx, msg);
        }
    }
    static class SimpleInHandlerZ extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("in in in in in in  ZZZZZZZZZZ");
            // pipeline是可以热插拔的，因为 ChannelHandlerContext 时一个双向链表
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }
    static class SimpleInHandlerB extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(" in BBBBBBBBBBBBB");
            // 这个可以截断c后续的handler
//            super.channelRead(ctx, msg);
        }
    }
    static class SimpleInHandlerC extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("in CCCCCCCCCCCCCCCCCCCCC");
            super.channelRead(ctx, msg);
        }
    }

    public static void main(String[] args) {
        ChannelInitializer initializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new SimpleInHandlerA());
                ch.pipeline().addLast(new SimpleInHandlerZ());
                ch.pipeline().addLast(new SimpleInHandlerB());
                ch.pipeline().addLast(new SimpleInHandlerC());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(initializer);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        channel.writeInbound(buf);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        channel.writeInbound(buf);
        System.out.println();
        System.out.println();
        System.out.println();
        channel.writeInbound(buf);
        try{
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
