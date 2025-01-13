package org.lee.netty.nio;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadEchoServerReactor {
    ServerSocketChannel serverSocket;
    AtomicInteger next = new AtomicInteger(0);
    Selector[] selectors = new Selector[2];
    SubSelector[] subSelectors = null;

    MultiThreadEchoServerReactor() throws Exception{
        selectors[0] = Selector.open();
        selectors[1] = Selector.open();
        serverSocket = ServerSocketChannel.open();
        InetSocketAddress address =
                new InetSocketAddress(
                        NIOConfigDemo.SOCKET_SERVER_IP,
                        NIOConfigDemo.SOCKET_SERVER_PORT);
        serverSocket.socket().bind(address);
        serverSocket.configureBlocking(false);
        // 第一个选择器，负责连接新事件
        SelectionKey sk0 = serverSocket.register(selectors[0], SelectionKey.OP_ACCEPT);
//        SelectionKey sk1 = serverSocket.register(selectors[1], SelectionKey.OP_READ);
        // 绑定Handler：attach 线连接监控handler处理器到SelectionKey（选择键）
        sk0.attach(new AcceptHandler());
//        sk1.attach(new MultiThreadEchoHandler(selectors, serverSocket));
//        sk1.attach(new Asy)
        SubSelector subSelector1 = new SubSelector(selectors[0]);
        SubSelector subSelector2 = new SubSelector(selectors[1]);
        subSelectors = new SubSelector[]{subSelector1, subSelector2};
    }
    private void startService(){
        new Thread(subSelectors[0], "Thread ------ 0").start();
        new Thread(subSelectors[1], "Thread ------ 1").start();
    }

    class AcceptHandler implements Runnable{
        public void run(){
            try{
                SocketChannel channel = serverSocket.accept();
                if (channel!=null) {
                    System.out.println("there is a connection:【"+channel.getRemoteAddress()+"】");
                    new MultiThreadEchoHandler(selectors[next.get()], channel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (next.incrementAndGet() == selectors.length){
                next.set(0);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        MultiThreadEchoServerReactor serverReactor = new MultiThreadEchoServerReactor();
        serverReactor.startService();
        System.out.println("started successful");
    }

}
