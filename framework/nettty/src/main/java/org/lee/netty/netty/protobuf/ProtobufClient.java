package org.lee.netty.netty.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.util.CharsetUtil;

public class ProtobufClient {


    public void runClient(){
        EventLoopGroup work = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(work);
            b.channel(NioSocketChannel.class);
            b.remoteAddress("localhost",80);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast(new ProtobufEncoderHandler());
                    ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                    ch.pipeline().addLast(new ProtobufEncoder());
//                    ch.pipeline().addLast(new ());
                }
            });
            ChannelFuture f  = b.connect();
            f.sync();
            Channel c = f.channel();
            for (int i = 0; i < 199; i++) {
                MsgProtos.Msg user = build("content___[啊啊啊"+i+"]",i);
                System.out.println("客户端发送了："+user.getContent());
                Thread.sleep(100);
                c.writeAndFlush(user);
            }
            c.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public MsgProtos.Msg build(String content,int id) throws InterruptedException {
        MsgProtos.Msg.Builder msg = MsgProtos.Msg.newBuilder();
        msg.setContent(content);
        msg.setId(id);

        return msg.build();
    }

//    static class ProtobufEncoderHandler extends MessageToMessageEncoder<MsgProtos.Msg> {
//        @Override
//        protected void encode(ChannelHandlerContext ctx, MsgProtos.Msg msg, List<Object> out) throws Exception {
//            MsgProtos.Msg.Builder builder = MsgProtos.Msg.newBuilder();
//            builder.setId(msg.getId())
//            .setContent(Arrays.toString(msg.getContent().getBytes(CharsetUtil.UTF_8)));
//            out.add(builder.build());
//        }
//    }

    static class ProtobufEncoderHandler extends MessageToByteEncoder<MsgProtos.Msg> {
        @Override
        protected void encode(ChannelHandlerContext ctx, MsgProtos.Msg msg, ByteBuf buf) throws Exception {
            byte[] bytes = msg.toString().getBytes(CharsetUtil.UTF_8);

            System.out.println("发送了消息：" + new String(bytes, 0, bytes.length,CharsetUtil.UTF_8));
            buf.writeBytes(bytes);
        }
    }

    public static void main(String[] args) {
        new ProtobufClient().runClient();
    }


}
