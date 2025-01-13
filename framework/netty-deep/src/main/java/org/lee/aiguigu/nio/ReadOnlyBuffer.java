package org.lee.aiguigu.nio;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer allocate = ByteBuffer.allocate(1 << 10);
        for (int i = 0; i < (1 << 6); i++) {
            allocate.put((byte) i);
        }

        allocate.flip();

        ByteBuffer readOnlyBuffer = allocate.asReadOnlyBuffer();

        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }
        readOnlyBuffer.put((byte) 1);
    }
}
