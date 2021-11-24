package org.lee.netty;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Main {
    public static void main(String[] args) throws Exception{
        System.out.println("ddemo");
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
//        发起远程连接
        socketChannel.connect(new InetSocketAddress("localhost", 80));
        while (! socketChannel.finishConnect()){
//            ServerSocketChannel serverSocketChannel = (SocketChannel) ke
        }

    }
}
