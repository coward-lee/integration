package org.lee.aiguigu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * scattering 将数据写入到buffer时，可以采用buffer数组， 依次写入【分散】
 * Gathering：从buffer读取数据是，可以采用buffer数组，依次读取【】
 */
public class ScatteringAngGatheringTest {

    public static void main(String[] args) throws Exception {
        // 使用ServerSocketChannel
        ServerSocketChannel channel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        channel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);

        byteBuffers[1] = ByteBuffer.allocate(3);
        SocketChannel accept = channel.accept();
        System.out.println("started");
        int msgLength = 8;
        while (true) {
            int byteRead = 0;
            while (byteRead < msgLength) {
                long read = accept.read(byteBuffers);
                byteRead += read;
                System.out.println("byteRead=" + byteRead);
                // 使用流打印
                Arrays.asList(byteBuffers).stream()
                        .map(buf ->
                                "position=" + buf.position() + ": limit" + buf.limit())
                        .forEach(System.out::println);
            }
            Arrays.asList(byteBuffers).forEach(ByteBuffer::flip);

            // 将数据读取显示到客户端
            long byteWrite = 0;

            while (byteWrite < msgLength) {
                byteWrite += accept.write(byteBuffers);
            }
            Arrays.asList(byteBuffers).forEach(ByteBuffer::clear);

            System.out.println("byteRead=" + byteRead + "   byteWrite:" + byteWrite);
        }
    }
}
