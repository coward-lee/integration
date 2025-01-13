package org.lee.simple.nio.nonblock;

import org.lee.util.ByteBufferUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;


public class Server {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 将ServerSocket设置为非阻塞
        ssc.configureBlocking(false);

        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> channelList = new ArrayList<>();
        while (true){
//            System.out.println("connecting");

            SocketChannel accept = ssc.accept();
            if (accept != null){
                System.out.println("链接成功");
                channelList.add(accept);
                // SocketChannel 同样支持非阻塞
                // 这样可以保证读取buffer的时候也是非阻塞的
                accept.configureBlocking(false);
            }
            for (SocketChannel channel : channelList) {
                channel.configureBlocking(false);
                System.out.println("read start");
                int read = channel.read(buffer);
                buffer.flip();
                ByteBufferUtils.print(buffer);
                buffer.clear();

                System.out.println("read end");
            }
        }
    }
}
