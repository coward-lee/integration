package org.lee.aiguigu;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1. MappedByteBuffer 可以让文件直接在内存修改，操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {


    public static void main(String[] args) throws Throwable {
//        MappedByteBuffer
        try (RandomAccessFile w = new RandomAccessFile("1.txt", "rw")) {
            FileChannel channel = w.getChannel();
            /**
             * READ_WRITE 使用读写模式
             * 0 可以直接修改文件的映射其实位置
             * 5 映射到内存的大小
             */
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
            map.put(0, (byte) 'H');
            map.put(3, (byte) '9');
            map.put(5, (byte) '9');
        }
    }
}
