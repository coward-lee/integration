package org.lee.aiguigu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class NioFileChannel01 {
    static final String fileName = "file01.txt";
    static final String fileName1 = "file02.txt";
    static final String fileName2 = "file03.txt";

    public static void main(String[] args) throws Exception {
//        writeFile();
//        readFile();
        copyFileWithTransfer();
    }

    static void copyFileWithTransfer() throws Exception{
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             FileOutputStream fileOutputStream = new FileOutputStream(fileName1);
             FileOutputStream file2 = new FileOutputStream(fileName2);) {
            FileChannel source = fileInputStream.getChannel();
            FileChannel dest = fileOutputStream.getChannel();
            dest.transferFrom(source, 0, source.size());
            source.transferTo(0,source.size(),file2.getChannel());
        }
    }
    static void copyFileWithPrimitive() throws Exception {
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             FileOutputStream fileOutputStream = new FileOutputStream(fileName1)) {
            FileChannel channel = fileInputStream.getChannel();
            FileChannel channel1 = fileOutputStream.getChannel();

            ByteBuffer allocate = ByteBuffer.allocate(1024);
            while (true) {
                allocate.clear();// 刷新缓冲区
                int read = channel.read(allocate);
                System.out.println(" read="+read);
                if (read == -1) {
                    break;
                }
                channel1.write(allocate.flip());
            }

        }
    }

    static void readFile() throws Exception {
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            FileChannel channel = fileInputStream.getChannel();
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            int read = channel.read(allocate);
            ByteBuffer flip = allocate.flip();
            System.out.println(new String(flip.array(), 0, read, StandardCharsets.UTF_8));
        }

    }

    static void writeFile() throws Exception {

        String str = "hello netty";
        // 创建一个输出流 -> channel
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        FileChannel channel = fileOutputStream.getChannel();
        // 创建一个缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        allocate.put(str.getBytes(StandardCharsets.UTF_8));
        // 对 byte buffer 进行flip操作
        allocate.flip();
        int write = channel.write(allocate);

        System.out.println("write result" + write);
        channel.close();
        fileOutputStream.close();
    }
}
