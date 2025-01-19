package org.lee.study.io.mutliplex;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class NioDemo {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(6379));
        serverSocketChannel.configureBlocking(false);
        LinkedList<SocketChannel> list = new LinkedList<>();

        while (true){
            for (SocketChannel channel : list) {
                ByteBuffer dst = ByteBuffer.allocateDirect(1 << 10);
                int read = channel.read(dst); // TODO: 这个read 会进行系统调用，会进行进程上下文切换。
                if (read > 0) {
                    System.out.println("读取数据");
                    dst.flip();
                    byte[] dst1 = new byte[read];
                    dst.get(dst1);
                    System.out.println(new String(dst1));
                    dst.clear();
                }
            }
            SocketChannel channel = serverSocketChannel.accept();
            if (channel != null){
                System.out.println("存在连接成功的连接");
                list.add(channel);
            }
        }
    }
}
