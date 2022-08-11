package org.lee;

import org.lee.net.Server;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        Server.start(9090);
        Socket accept = Server.accept();
        byte[] bytes = new byte[1024];
        int read = accept.getInputStream().read(bytes);
        System.out.println(new String(bytes, 0, read, StandardCharsets.UTF_8));
    }
}