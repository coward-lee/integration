package org.lee.simple.nio.block;

import org.lee.util.ByteBufferUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 阻塞时NIO
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.bind(new InetSocketAddress(8080));
        List<SocketChannel> channelList = new ArrayList<>();
        System.out.println("start listening");
        while (true){
            SocketChannel accept = serverSocketChannel.accept();
            channelList.add(accept);
            for (SocketChannel channel : channelList) {
                int read = channel.read(buffer);
                if (read > 0){
                    System.out.println("read start");
                    buffer.flip();
                    ByteBufferUtils.print(buffer);
                    buffer.clear();

                    System.out.println("read end");

                }
            }
        }
    }
}
