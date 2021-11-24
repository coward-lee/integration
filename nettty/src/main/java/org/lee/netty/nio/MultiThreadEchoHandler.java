package org.lee.netty.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadEchoHandler implements Runnable{
    final SocketChannel channel1;
    final SelectionKey selectionKey;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    static final int RECIEVING = 0, SENDING = 1;
    int state = RECIEVING;
    static ExecutorService pool = Executors.newFixedThreadPool(4);

    public MultiThreadEchoHandler(Selector selector, SocketChannel channel) throws Exception {
        System.out.println("MultiThreadEchoHandler constructor");
        channel1 = channel;
        channel.configureBlocking(false);
        selectionKey = channel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
//        System.out.println(" MultiThreadEchoHandler run one time.");
        pool.execute(new AsyncTask());
    }
    public synchronized void asyncRun(){
        try{
//            System.out.println(" there is a event");
            if (state == SENDING){
                System.out.println(" there is a write event");
                channel1.write(byteBuffer);
                byteBuffer.clear();
                selectionKey.interestOps(SelectionKey.OP_READ);
                state = RECIEVING;
            }else if (state == RECIEVING){
                int length = 0;
                System.out.println(" there is a read event");
                while ((length = channel1.read(byteBuffer))>0){
                    System.out.println(new String(byteBuffer.array(), 0 , length));
                }
                byteBuffer.flip();
                byteBuffer.clear();
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                state = SENDING;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    class AsyncTask implements Runnable{
        @Override
        public void run() {
            MultiThreadEchoHandler.this.asyncRun();
        }
    }
}
