package org.lee.simple.bytebufffer;

import org.junit.Test;

import java.nio.ByteBuffer;

public class TestByteBufferReadWrite {
    @Test
    public void test_read_and_write(){
        ByteBuffer allocate = ByteBuffer.allocate(10);
        allocate.put((byte) 'a');
        System.out.println(allocate);
        byte b = allocate.get();
        System.out.println(b);
    }
}
