package org.lee.simple.bytebufffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

import static org.lee.util.ByteBufferUtils.print;


public class TestByteBufferString {
    public static void main(String[] args) {

        // 1. 将字符串转化为 byteBuffer
        ByteBuffer buffer1 = ByteBuffer.allocate(10);
        buffer1.put("hello".getBytes(StandardCharsets.UTF_8));
        print(buffer1);

        // 2. Charset
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");
        print(buffer2);

        // 3. wrap
        ByteBuffer buffer3 = ByteBuffer.wrap("hello".getBytes(StandardCharsets.UTF_8));
        print(buffer3);

        // 将 byteBuffer转化成 str
        String s1 = StandardCharsets.UTF_8.decode(buffer2).toString();
        System.out.println(s1);
        String s2 = StandardCharsets.UTF_8.decode(buffer1).toString();
        System.out.println(s2);
    }
}
