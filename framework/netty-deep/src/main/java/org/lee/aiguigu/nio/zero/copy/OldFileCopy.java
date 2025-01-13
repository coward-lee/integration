package org.lee.aiguigu.nio.zero.copy;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class OldFileCopy {

    public static void main(String[] args) throws IOException, InterruptedException {
        new Thread(() -> {
            try {
                server();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(1000);
        new Thread(() -> {
            try {
                client();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    static void server() throws IOException {

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(6666));
        while (true) {
            System.out.println("start to listen");
            Socket accept = serverSocket.accept();
            long start = System.currentTimeMillis();
            byte[] bytes = new byte[1 << 15];
            InputStream inputStream = accept.getInputStream();
            OutputStream outputStream = new FileOutputStream("receive");

            int read = -1;
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();
            System.out.println("cost:" +( System.currentTimeMillis() - start));
            inputStream.close();
            accept.close();
        }
    }

    static void client() throws IOException {
        Socket socket = new Socket();
        System.out.println("client started");
        socket.connect(new InetSocketAddress("127.0.0.1", 6666));
        System.out.println("client connected");
        OutputStream outputStream = socket.getOutputStream();
        InputStream fileInputStream = new FileInputStream("client_file");
        byte[] bytes = fileInputStream.readAllBytes();
        outputStream.write(bytes);
        System.out.println("client send finished");
        outputStream.close();
        fileInputStream.close();
    }
}
