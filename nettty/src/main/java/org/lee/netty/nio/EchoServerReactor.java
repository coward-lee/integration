package org.lee.netty.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Collections;
import java.util.Set;

// 单线程的Reactor模型
public class EchoServerReactor implements Runnable {
    Selector selector;
    ServerSocketChannel serverSocket;

    public EchoServerReactor() throws Exception {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new AcceptHandler());
    }

    @Override
    public void run() {
        try{
            while (!Thread.interrupted()){
                selector.select();
                Set set = Collections.singleton(selector.select());
                set.forEach(sk -> {
                    dispatch((SelectionKey) sk);
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 反应器分发方法
    void dispatch(SelectionKey sk){
        Runnable handler = (Runnable)(sk.attachment());
        if (handler != null){
            handler.run();
        }
    }

    static class AcceptHandler implements Runnable{

        @Override
        public void run() {

        }
    }
}
