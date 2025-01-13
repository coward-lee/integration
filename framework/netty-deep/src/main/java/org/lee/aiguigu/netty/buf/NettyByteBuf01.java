package org.lee.aiguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
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
        ByteBuf buffer = Unpooled.buffer(10);
    }
}
