package org.lee.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private static ServerSocket serverSocket;
    private static final ReentrantLock lock = new ReentrantLock();

    public static void start(Integer port){
        try {
            lock.lock();
            if (serverSocket == null) {
                serverSocket = new ServerSocket(port);
            }
            lock.unlock();
        } catch (Throwable t) {
            System.out.println("启动出错");
            t.printStackTrace();
        }
    }

    public static Socket accept() throws IOException {
        return serverSocket.accept();
    }
}
