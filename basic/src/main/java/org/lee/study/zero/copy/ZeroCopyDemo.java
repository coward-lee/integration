package org.lee.study.zero.copy;

import com.google.common.base.Stopwatch;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

public class ZeroCopyDemo {
    int count = 1 << 10 << 10 << 10;

    byte[] bytes = new byte[count];

    public ZeroCopyDemo() {
        Arrays.fill(bytes, (byte) 1);
    }

    public static void main(String[] args) throws IOException {
        ZeroCopyDemo zeroCopyDemo = new ZeroCopyDemo();
//        zeroCopyDemo.normal();
        zeroCopyDemo.zeroCopy();
    }

    public void normal() {
        Stopwatch started = Stopwatch.createStarted();
        for (int i = 0; i < 10; i++) {

            File file = new File("zero.bin");

            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(bytes, 0, count);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        started.stop();
        String s = started.toString();
        System.out.println(s);
    }

    public void zeroCopy()  {
        ByteBuffer src = ByteBuffer.allocateDirect(count);
        src.put(bytes);

        Stopwatch started = Stopwatch.createStarted();
        File file = new File("zero.bin");
        for (int i = 0; i < 10; i++) {
            try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                FileChannel channel = raf.getChannel();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(count);
                WritableByteChannel writableByteChannel = Channels.newChannel(byteArrayOutputStream);
                writableByteChannel.write(src);
                channel.transferTo(0, count, writableByteChannel);
                writableByteChannel.close();
                channel.close();
                byteArrayOutputStream.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        started.stop();
        String s = started.toString();
        System.out.println(s);
    }
}
