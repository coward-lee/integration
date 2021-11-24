package org.lee.netty.nio;

import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioDiscardServer {

    public  static void starServer() throws Exception {
        // 1.获取选择器
        Selector selector = Selector.open();
        // 2. 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 3.设置非阻塞
        serverSocketChannel.configureBlocking(false);
        // 4.绑定连接
        serverSocketChannel.bind(new InetSocketAddress(80));
        System.out.println("server started");
        // 5. 注册通道到选择器上面
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 6. 轮询IO就绪时间
        while (selector.select() > 0){
            // 7. 获取选择键集合
            Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
            while (selectionKeys.hasNext()){
                // 8. 获取单个的选择健，并处理
                SelectionKey key = selectionKeys.next();
                // 9. 判断事件类型
                if (key.isAcceptable()) {
                    // 10. 如果为接收事件，获取客户端的连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 11. 切换为非阻塞
                    socketChannel.configureBlocking(false);
                    // 12. 将新连接的通道的可读事件注册到选择器上，。。。。。。。。。。。。。。。。。。。注册。。。。。。。。。。
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }else if (key.isReadable()){
                    // 13. 如果为可读
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    // 14. 读取然后丢弃
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int length = 0;
                    while((length = socketChannel.read(byteBuffer)) > 0){
                        byteBuffer.flip();
                        System.out.println("接收到的内容："+new String(byteBuffer.array(), 0 ,length, CharsetUtil.UTF_8));
                        byteBuffer.clear();
                    }
                    socketChannel.close();
                }
                // 15. 移除键
                selectionKeys.remove();
            }
        }
        serverSocketChannel.close();
    }

    public static void main(String[] args) throws Exception {
        starServer();
    }
}
