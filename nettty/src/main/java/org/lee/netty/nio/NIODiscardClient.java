package org.lee.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIODiscardClient {
    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(NIOConfigDemo.SOCKET_SERVER_IP, NIOConfigDemo.SOCKET_SERVER_PORT);
        // 1. 获取通道
        SocketChannel socketChannel = SocketChannel.open(address);
        // 2. 切换成非阻塞
        socketChannel.configureBlocking(false);
        // 3. 自旋等待连接完成
        while (! socketChannel.finishConnect()){

        }
        System.out.println("client connected");
        ByteBuffer bf = ByteBuffer.allocate(1024);
        bf.put("hello world".getBytes());
        bf.flip();
        // 发送到服务器
        socketChannel.write(bf);
        socketChannel.shutdownOutput();
        socketChannel.close();
    }
}
