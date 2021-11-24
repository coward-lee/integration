package org.lee.netty.netty.cahnnel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;

public class OutPipeline {
    static class SimpleOutputHandlerA extends ChannelOutboundHandlerAdapter{
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("AAAAAAAAAAAAAAAAA    out");
            super.write(ctx, msg, promise);
        }
    }
    static class SimpleOutputHandlerZ extends ChannelOutboundHandlerAdapter{
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("ZZZZZZZZZZZ    out");
            super.write(ctx, msg, promise);
        }
    }
    static class SimpleOutputHandlerB extends ChannelOutboundHandlerAdapter{
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("CCCCCCCCCCC    out");
            // out put 不能被截断
//            super.write(ctx, msg, promise);
        }
    }
    static class SimpleOutputHandlerC extends ChannelOutboundHandlerAdapter{
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("CCCCCCCCCCCCCCCC    out");
            super.write(ctx, msg, promise);
        }
    }

    public static void main(String[] args) {
        ChannelInitializer initializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new SimpleOutputHandlerA());
                ch.pipeline().addLast(new SimpleOutputHandlerB());
                ch.pipeline().addLast(new SimpleOutputHandlerC());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(initializer);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        channel.writeOutbound(buf);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
