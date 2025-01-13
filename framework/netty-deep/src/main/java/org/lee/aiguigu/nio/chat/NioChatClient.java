package org.lee.aiguigu.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class NioChatClient {
    private final int port = 6666;
    private final String HOST = "127.0.0.1";


    private Selector selector;
    private SocketChannel ssc;
    private String userName;

    public NioChatClient() {
        try {
            this.selector = Selector.open();
            // 连接服务器
            this.ssc = SocketChannel.open();
            this.ssc.connect(new InetSocketAddress(HOST, port));
            // 配置非阻塞
            this.ssc.configureBlocking(false);
            // 注册channel 到selector
            this.ssc.register(selector, SelectionKey.OP_READ);
            // 得到 user name
            this.userName = ssc.getLocalAddress().toString().substring(1);

            System.out.println(userName + "is ok....");

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        String m = userName + " say :" + msg;

        try {
            ssc.write(ByteBuffer.wrap(m.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readMsg() {
        try {
            int select = selector.select();
            if (select > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int read = channel.read(buffer);
                        String msg = new String(buffer.array(), 0, read, StandardCharsets.UTF_8);
                        System.out.println(msg.trim());
                    }
                }
            } else {
                System.out.println(" no channel to use");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NioChatClient nioChatClient = new NioChatClient();
        new Thread(() -> {
            while (true) {
                nioChatClient.readMsg();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        System.out.println("wait to input");

        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            nioChatClient.sendMsg(msg);
        }
    }
}
