package org.lee.aiguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws Throwable {
        // 创建server socket channel
        ServerSocketChannel ssc = ServerSocketChannel.open();

        // 得到一个对象
        Selector selector = Selector.open();

        ssc.socket().bind(new InetSocketAddress(6666));

        ssc.configureBlocking(false);

        // 把 ServerSocketChannel 注册到 selector
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {


            if (selector.select(1000) == 0) {
                System.out.println("wait 1 second no event happened");
                continue;
            }
            // 如果返回不是0, 就去获取已经关注的事件的集合，在通过selectionKey获取channel进行业务操作
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                try {
                    // 根据key查看channel的事件，进行事件处理
                    if (key.isAcceptable()) {
                        // 此时客户端一定是准备好了的，所以直接连接
                        SocketChannel accept = ssc.accept();
                        System.out.println("accept event:" + accept + accept.hashCode());
                        // 将客户端连接生成的channel 绑定到selector
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    }
                    if (key.isReadable()) {
                        // 通过key 获得channel
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer);
                        System.out.println("from client:" + new String(buffer.array(), 0, read, StandardCharsets.UTF_8));
                        buffer.clear();
                    }
                } catch (IOException t) {
                    key.cancel();
                    t.printStackTrace();
//                    throw new RuntimeException(t);
                }finally {
                    selectionKeys.remove(key);
                }
            }
//            selectionKeys.forEach(key -> {
//                try {
//                    // 根据key查看channel的事件，进行事件处理
//                    if (key.isAcceptable()) {
//                        // 此时客户端一定是准备好了的，所以直接连接
//                        SocketChannel accept = ssc.accept();
//                        System.out.println("accept event:" + accept + accept.hashCode());
//                        // 将客户端连接生成的channel 绑定到selector
//                        accept.configureBlocking(false);
//                        accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
//                    }
//                    if (key.isReadable()) {
//                        // 通过key 获得channel
//                        SocketChannel channel = (SocketChannel) key.channel();
//                        ByteBuffer buffer = (ByteBuffer) key.attachment();
//                        int read = channel.read(buffer);
//                        System.out.println("from client:" + new String(buffer.array(), 0, read, StandardCharsets.UTF_8));
//                    }
//                } catch (Throwable t) {
//                    t.printStackTrace();
////                    throw new RuntimeException(t);
//                }
//
//            });
//            selectionKeys.clear();
        }
    }
}
