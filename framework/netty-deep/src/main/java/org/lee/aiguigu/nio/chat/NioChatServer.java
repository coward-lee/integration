package org.lee.aiguigu.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NioChatServer {
    private final int port = 6666;


    Selector selector;
    ServerSocketChannel ssc;

    public NioChatServer() {
        try {
            selector = Selector.open();
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(port));
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    // 监听代码
    public void listen() throws Exception {
        // 循环处理
        while (true) {
            int count = selector.select(2000);
            if (count > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    // 注册练级的监听事件
                    if (key.isAcceptable()) {
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                    }
                    // 读取数据
                    if (key.isReadable()) {
                        readData(key);
                    }
                    iterator.remove();
                }
            } else {
                System.out.println("waiting");
            }
        }

    }

    private void readData(SelectionKey key) {
        // 定义一个SocketChannel
        SocketChannel channel = null;
        try {
            // 得到Channel
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int readCount = channel.read(buffer);
            if (readCount > 0) {
                String msg = new String(buffer.array(), 0, readCount, StandardCharsets.UTF_8);
                System.out.println("from client:" + msg);
                sendInfoToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " offlined");
                key.cancel();
            } catch (IOException ignored) {

            }
        }
    }

    private void sendInfoToOtherClients(String msg, SocketChannel socketChannel) throws IOException {
        System.out.println("redirecting message in server");

        for (SelectionKey key : selector.keys()) {
            Channel channel = key.channel();
            if (channel instanceof SocketChannel && socketChannel != channel) {
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
                ((SocketChannel) channel).write(buffer);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        NioChatServer nioChatServer = new NioChatServer();
        nioChatServer.listen();
    }
}
