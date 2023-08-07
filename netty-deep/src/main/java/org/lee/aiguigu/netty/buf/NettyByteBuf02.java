package org.lee.aiguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        /**
         * 1. 创建对象，就是一个数组，里面有一个成员变量 byte[10]
         * 2. netty 底层的buffer中，不需要使用flip进行翻转
         *      他底层维护了read index和write index
         * 3. 通过将read index 和 write index 和 capacity，将 buffer 分成三个区域
         *  0 -- read index 已经读取的区域
         *  read index -- write index 可读区域
         *  write index -- capacity 可写区域
         */
        ByteBuf buffer = Unpooled.copiedBuffer("hello netty".getBytes(StandardCharsets.UTF_8));

        // getByte 不会改变read index 的值
        System.out.println(buffer.arrayOffset()+":"+buffer.readerIndex()+":"+ buffer.writerIndex());
        System.out.println((char)buffer.getByte(1));

        // readByte 不会改变read index 的值
        System.out.println(buffer.arrayOffset()+":"+buffer.readerIndex()+":"+ buffer.writerIndex());
        System.out.println((char)buffer.readByte());
        System.out.println((char)buffer.readByte());
        System.out.println((char)buffer.readByte());
        System.out.println((char)buffer.readByte());
        System.out.println((char)buffer.readByte());
        System.out.println(buffer.arrayOffset()+":"+buffer.readerIndex()+":"+ buffer.writerIndex());
        byte[] bytes = new byte[6];
        ByteBuf byteBuf = buffer.readBytes(bytes); // readBytes 返回的是buffer自身
        System.out.println(byteBuf == buffer);
        System.out.println(new String(bytes));
    }
}
