package org.lee.im.single;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;

public class EmbededChannelTest {
//    public static void main(String[] args) {
//
//        ChannelInitializer initializer = new ChannelInitializer() {
//            @Override
//            protected void initChannel(Channel ch) throws Exception {
//                ch.pipeline().addLast(new Client.ClientOutChannel());
//            }
//        };
//        EmbeddedChannel channel = new EmbeddedChannel(initializer);
//        channel.write("aaaaaaaaaaaaaa");
//
//
//        ChannelInitializer initializerIn = new ChannelInitializer() {
//            @Override
//            protected void initChannel(Channel ch) throws Exception {
//                ch.pipeline().addLast(new Server.ServerDecoder());
////                ch.pipeline().addLast(new Server.ClientOutChannel());
//            }
//        };
//        EmbeddedChannel serverChannel = new EmbeddedChannel(initializerIn);
//        serverChannel.writeInbound("aaaaaaaaaaaaaaa");
//        try{
//            Thread.sleep(Integer.MAX_VALUE);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
