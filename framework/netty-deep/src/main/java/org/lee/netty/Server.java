package org.lee.netty;

import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    public static void main(String[] args) {
//        new ServerBootstrap().group(new NioEventLoopGroup(1))
//                .channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<NioServerSocketChannel>() {
//                    @Override
//                    protected void initChannel(NioServerSocketChannel ch) {
//                        ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
//                            @Override
//                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
//                                System.out.println(msg.toString(Charset.defaultCharset()));
//                            }
//                        });
//                    }
//                }).bind(8080);
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MAX_VALUE);
        int andIncrement = atomicInteger.incrementAndGet();
        andIncrement = atomicInteger.incrementAndGet();
        System.out.println(andIncrement);
        System.out.println(andIncrement & 512 - 1);
        System.out.println(Math.abs(andIncrement % 1023));
        System.out.println(Integer.MAX_VALUE);
    }
}
