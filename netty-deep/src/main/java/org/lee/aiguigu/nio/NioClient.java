package org.lee.aiguigu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NioClient {
    public static void main(String[] args) throws Throwable {
       // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);
        // 连接服务端
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6666);
        if (!socketChannel.connect(address)){
            while (!socketChannel.finishConnect()){
                System.out.println("client connect failed, to do other thing!");
            }
        }
        // 连接成功发送数据
        String str = "hello nio";
        //
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8));
        // 发送数据
        socketChannel.write(buffer);
        System.in.read();

    }
}
