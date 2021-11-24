package org.lee.netty.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable{
    Selector selector;
    ServerSocketChannel serverSocket;
    void EchoServerReactor() throws  Exception{
        SelectionKey selectionKey = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new AcceptHandler());
    }
    @Override
    public void run() {
        try{
            while (!Thread.interrupted()){
                selector.select();
                Set set = selector.selectedKeys();
                Iterator it = set.iterator();
                while (it.hasNext()){
                    // 接收事件之后使用dispatch来进行分发
                    SelectionKey sk = (SelectionKey) it.next();
                    dispatch(sk);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    void dispatch(SelectionKey k){
        Runnable handler = (Runnable) k.attachment();
        if (handler != null){
            handler.run();
        }
    }
    class  AcceptHandler implements Runnable{

        @Override
        public void run() {
            // 接收一个新的连接
        }
    }
    class IOHandler implements Runnable{

        final SocketChannel channel;
        final SelectionKey selectionKey;

        public IOHandler(SocketChannel channel, SelectionKey selectionKey) throws Exception {
            this.channel = channel;
            this.selectionKey = selectionKey;
            channel.configureBlocking(false);
            selectionKey = channel.register(selector, 0);
            selectionKey.attach(this);
            selectionKey.interestOps(SelectionKey.OP_READ  | SelectionKey.OP_WRITE);
        }


        @Override
        public void run() {

        }
    }
}

