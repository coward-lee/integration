package org.lee.simple.nio.nonblock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WriteClient {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));
        int count = 0;
        while (true){
            ByteBuffer buffer = ByteBuffer.allocate(1<<20);
            count+=sc.read(buffer);
            System.out.println(count);
            buffer.clear();
        }
    }
}
