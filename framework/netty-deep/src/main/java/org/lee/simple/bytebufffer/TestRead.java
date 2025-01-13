package org.lee.simple.bytebufffer;

import org.lee.util.ByteBufferUtils;

import java.nio.ByteBuffer;

public class TestRead {
    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a','b','c','d'});
        // 切换为读模式
        buffer.flip();
        // 一次性读取最多四个字节？
        buffer.get(new byte[4]);
        ByteBufferUtils.print(buffer);
        // rewind 从头开始读取
        buffer.rewind();
        ByteBufferUtils.print(buffer);


        // mark & reset
        // mark 做一个标记, 记录 position 位置, reset 是将 position 充值到 mark的位置
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());
        buffer.mark();
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());
        buffer.reset();
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());

        // get(i);
        System.out.println(buffer.get(3));
    }
}
