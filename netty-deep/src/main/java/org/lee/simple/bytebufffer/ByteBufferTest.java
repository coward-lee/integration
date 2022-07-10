package org.lee.simple.bytebufffer;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class ByteBufferTest {
    public static void main(String[] args) {
        new ByteBufferTest().test();
    }
    public void test(){

        String path = this.getClass().getResource("/text.txt").getPath();
        try (var fileChannel =new FileInputStream(path).getChannel()) {

            // 分配一个缓存空间来缓存我们从文件去读取的数据
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            while (true) {
                // 从channel 读取数据,并将数据存放在我们定义的byteBuffer中
                int len = fileChannel.read(byteBuffer);
                System.out.println("每一次读取的字节：" + len);
                if (len == -1) {
                    break;
                }
                // 输出buffer的内容
                byteBuffer.flip(); // 将缓冲区置为读模式
                while (byteBuffer.hasRemaining()) {
                    byte b = byteBuffer.get();
                    System.out.println("读取的字节：" + (char) b);
                }
                byteBuffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
