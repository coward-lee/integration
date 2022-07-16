package org.lee.simple.nio.nonblock;

import io.netty.buffer.ByteBuf;
import org.lee.util.ByteBufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MultiThreadServer {
    private static final Logger log = LoggerFactory.getLogger(MultiThreadServer.class);

    public static void main(String[] args) throws IOException {

        ServerSocketChannel ssc  = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT, null);
        ssc.bind(new InetSocketAddress(8080));
        Worker worker = new Worker("worker-0");

        while (true){
            log.info("accept select");
            selector.select();

            log.info("accept selected");
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.info("connected :{}",sc);

                    log.info("before register :{}",sc.getRemoteAddress());
                    worker.register(sc);
                    log.info("after register  :{}",sc.getRemoteAddress());
                }
            }
        }

    }

    static class Worker implements Runnable{
        Selector selector;
        Thread thread;
        String name;
        boolean started = false;

        private ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<>();

        public Worker(String name) {
            this.name = name;
        }

        public void register(SocketChannel sc) throws IOException {
            if (!started){
                thread = new Thread(this, name);
                selector = Selector.open();
                thread.start();
                started = true;
            }
            queue.add(()->{
                try {
                    sc.register(selector, SelectionKey.OP_READ, null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }

            });
            selector.wakeup();
        }

        @Override
        public void run() {
            while (true){
                try {
                    log.info("worker select");
                    selector.select();
                    Runnable task = queue.poll();
                    if (task != null){
                        task.run();
                    }
                    log.info("worker selected");
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey sk = iterator.next();
                        iterator.remove();
                        if (sk.isReadable()){
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel sc = (SocketChannel)sk.channel();
                            log.info("read before:{}", sc.getRemoteAddress());
//                            buffer.flip();
                            int read = sc.read(buffer);
                            buffer.flip();
                            log.info("read before:{} , read:{}", sc.getRemoteAddress(), read);
                            ByteBufferUtils.print(buffer);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
