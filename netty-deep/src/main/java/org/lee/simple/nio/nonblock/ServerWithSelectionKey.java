package org.lee.simple.nio.nonblock;

import org.lee.util.ByteBufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;


public class ServerWithSelectionKey {
    static Logger log = LoggerFactory.getLogger(ServerWithSelectionKey.class);

    public static void main(String[] args) throws IOException {
        // 1. 创建selector
        Selector selector = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 将ServerSocket设置为非阻塞
        ssc.configureBlocking(false);

        // 2. 注册 channel 到 channel 上
        // 这个key就是 channel和selector 的绑定关系 ？
        // selectionKey 就是将来事件发生后，通过它可以知道事件和channel发生的事情
        SelectionKey sscKey = ssc.register(selector, 0, null);

        // accept、connect(客户端连接成功)、read(可读事件)、write(可写事件)
        // 可以 只关注 accept 事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.info("register key:{}", sscKey);

        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            log.info("开始select");
            // 3. 执行select 方法, 没有事件发生,阻塞线程，有时间发生恢复运行
            selector.select();

            log.info("开始了select");
            // 4. 处理时间， selectorKeys 内容包含了所有发生的时间
            // 在进行对这个 selectedKeys 集合进行过滤的时候需要手动将其元素从中移除
            // 这里发生事件之后，selectedKeys 集合只会增加，不会因为我们处理了事件而被移除
            // 所以需要我们手动移除
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 在进行对这个 selectedKeys 集合进行过滤的时候需要手动将其元素从中移除
                iterator.remove();

                log.info("key : {}", key);
                // 5. 区分事件类型
                if (key.isAcceptable()) {// 如果是accept
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);

                    // 将byteBuffer 作为附件关联到 selectionKey
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.info("{}", sc);
                } else if (key.isReadable()) {
                    try {

                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        // 如果是正常断开 那么read返回的是-1, 需要将其移除
                        int read = channel.read(buffer);
                        if (read == -1){
                            key.cancel();
                        }else{
                            ByteBufferUtils.split(buffer);
                            if (buffer.position() == buffer.limit()){
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                                ByteBufferUtils.split(newBuffer);
                            }
//                            buffer.flip();
//                            ByteBufferUtils.print(buffer);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel();
                    }

                }
                // 不对事件进行任何操作，会被加扰cancelChannel队列之中
//                key.cancel();

            }

        }
    }
}