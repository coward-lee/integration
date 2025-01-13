package org.lee.simple.nio.nonblock;

import org.lee.util.ByteBufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;


public class WriteServer {
    static Logger log = LoggerFactory.getLogger(WriteServer.class);

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
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, 0, null);

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 300000000; i++) {
                        sb.append("a");
                    }
                    ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(sb.toString());
                    int write = sc.write(byteBuffer);
                    log.info("{}",write);
                    if (byteBuffer.hasRemaining()){
                        // 4. 关注可写事件
                        scKey.interestOps(scKey.interestOps() | SelectionKey.OP_WRITE);
                        // 5. 把未写完的数据挂到scKey上
                        scKey.attach(byteBuffer);
                    }
                }else if(key.isWritable()){
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    SocketChannel sc = (SocketChannel)key.channel();
                    int write = sc.write(buffer);
                    System.out.println("writable :"+write);

                }
            }

        }
    }
}