package org.lee.aiguigu.zero.copy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NioFileCopy {

    public static void main(String[] args) throws IOException, InterruptedException {
        new Thread(() -> {
            try {
                server();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(1000);
        new Thread(() -> {
            try {
                client();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    static void server() throws IOException, InterruptedException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(6666));
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int readCount = selector.select(1000);
//            Thread.sleep(1000);
            if (readCount <= 0) {
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    SocketChannel channel = ssc.accept();

                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ);
                }

                if (key.isReadable()) {
                    long start = System.currentTimeMillis();
                    SocketChannel channel = ((SocketChannel) key.channel());
                    int read = -1;
                    ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

                    try (FileOutputStream fileOutputStream = new FileOutputStream("nioFile",true)) {
                        FileChannel fileChannel = fileOutputStream.getChannel();

//                        fileChannel.transferFrom(channel,0, Long.MAX_VALUE);
                        int read1 = channel.read(buffer);
                        buffer.flip();
                        fileChannel.write(buffer);
                        buffer.clear();
                        System.out.println("cost:" + (System.currentTimeMillis() - start));
                    }
                }

                iterator.remove();
            }
        }
    }

    static void client() throws IOException {
        SocketChannel socket = SocketChannel.open();
        System.out.println("client started");
        socket.connect(new InetSocketAddress("127.0.0.1", 6666));
        System.out.println("client connected");

        FileInputStream fileInputStream = new FileInputStream("client_file");
        FileChannel fileChannel = fileInputStream.getChannel();
        long transfer = 1;
        long offset = 0;
        long size = fileChannel.size();
        while (offset < size && transfer > 0) {
            transfer = fileChannel.transferTo(offset, size, socket.socket().getChannel());
            offset += transfer;
        }

        fileInputStream.close();
    }
}
