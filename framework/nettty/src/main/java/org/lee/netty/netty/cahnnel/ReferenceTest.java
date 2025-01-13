package org.lee.netty.netty.cahnnel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ReferenceTest {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        System.out.println("created");
        buf.retain();
        System.out.println(buf.refCnt());
        buf.release();
        System.out.println(buf.refCnt());
        buf.release();
        System.out.println(buf.refCnt());
        buf.retain();
        System.out.println(buf.refCnt());
    }
}
