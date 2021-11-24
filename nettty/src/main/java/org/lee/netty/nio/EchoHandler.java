package org.lee.netty.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

// 单线程的Reactor模型
public class EchoHandler implements Runnable {

    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    int state = 0;

    public EchoHandler(Selector selector, SocketChannel channel) throws Exception {
        this.channel = channel;
        sk = channel.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try{
            if (state == 1){
                // 写入通道
                channel.write(byteBuffer);
                byteBuffer.clear();
                sk.interestOps(SelectionKey.OP_READ);
                byteBuffer.clear();
                state = 1;
            }else if(state == 0){
                int len = 0;
                while((len = channel.read(byteBuffer)) > 0){
                    System.out.println(new String(byteBuffer.array(), 0, len));
                }
                byteBuffer.flip();
                sk.interestOps(SelectionKey.OP_WRITE);
                state = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
