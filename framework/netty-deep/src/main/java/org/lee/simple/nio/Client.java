package org.lee.simple.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",8080));
        System.out.println("waiting");
        sc.write(Charset.defaultCharset().encode("0123456789abcdef333\n"));
        sc.write(Charset.defaultCharset().encode("0123456789abcdef333\n"));
        System.in.read();
    }
}
