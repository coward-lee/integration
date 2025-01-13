package org.lee.netty.netty.coder;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 消息解码器，用于入站
 * ByteToMessageDecoder
 * MessageToMessageDecoder
 * 编码器，用于出战
 * MessageToByteEncoder
 * MessageToMessageEncoder
 *
 */
public class DecoderAndEncoder {

    public static void main(String[] args) {

    }


    // MessageToByteEncoder 简单示例
    class Integer2ByteEncoder extends MessageToByteEncoder<Integer>{
        @Override
        protected void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out) throws Exception {
            out.writeInt(msg);
            System.out.println("encoder int = "+msg);
        }
    }
    // MessageToMessageEncoder 简单示例
    class Integer2StringDecoder extends MessageToMessageDecoder<Integer> {
        @Override
        protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
            out.add(String.valueOf(msg));
        }
    }

}
