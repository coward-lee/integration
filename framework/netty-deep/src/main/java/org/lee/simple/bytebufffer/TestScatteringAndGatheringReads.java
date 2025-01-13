package org.lee.simple.bytebufffer;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import static org.lee.util.ByteBufferUtils.print;

/**
 * FileChannel只能在阻塞模式下工作
 */
public class TestScatteringAndGatheringReads {
    @Test
    public void test_scatter(){

        try(FileChannel channel = new RandomAccessFile(this.getClass().getResource("/words.txt").getFile(),"r").getChannel()){
            ByteBuffer b1 = ByteBuffer.allocate(3);
            ByteBuffer b2 = ByteBuffer.allocate(3);
            ByteBuffer b3 = ByteBuffer.allocate(5);
            channel.read(new ByteBuffer[]{b1, b2, b3});
            b1.flip();
            b2.flip();
            b3.flip();
            print(b1);
            print(b2);
            print(b3);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test_gathering(){

        try(FileChannel channel = new RandomAccessFile("words2.txt","rw").getChannel()){
            ByteBuffer b1 = StandardCharsets.UTF_8.encode("hello");
            ByteBuffer b2 = StandardCharsets.UTF_8.encode("world");
            ByteBuffer b3 = StandardCharsets.UTF_8.encode("你好");

            channel.write(new ByteBuffer[]{b1, b2, b3});
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
