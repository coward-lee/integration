package org.lee.netty.netty.cahnnel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class BufferTypeTest {
    public static void main(String[] args) {
        // 堆
        ByteBuf heap = ByteBufAllocator.DEFAULT.buffer();
        // 直接缓冲区，直接缓冲去在native的内存区域，在执行业务进行读取的时候需要 需要通过getBytes和readBytes
        ByteBuf direct = ByteBufAllocator.DEFAULT.directBuffer();
    }
}
