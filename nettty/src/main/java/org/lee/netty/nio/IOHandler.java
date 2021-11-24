package org.lee.netty.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class IOHandler implements Runnable{

    final SocketChannel channel;
    final SelectionKey selectionKey;

    public IOHandler(Selector selector, SocketChannel c) throws Exception {
        channel = c;
        c.configureBlocking(false);
        selectionKey = channel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    @Override
    public void run() {
        // 处理输入输出
    }
}
