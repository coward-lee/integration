package org.lee.study.io.zero.copy;

import com.google.common.base.Stopwatch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BiDataWriteDemo {
    static int count = 1 << 10 << 10 << 8;
    int writeTimes = 1;

    byte[] bytes = new byte[0];

    public BiDataWriteDemo() {
        StringBuilder sb = new StringBuilder();
        String abcdefgh = "abcdefgh";
        for (int i = 123; i < count; i++) {
            sb.append(abcdefgh);
        }
        bytes = sb.toString().getBytes();
        System.out.println("total bytes is :" + bytes.length);
        System.out.println("total bytes is : " + bytes.length / 1024 + " kb");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        BiDataWriteDemo zeroCopyDemo = new BiDataWriteDemo();
        // 1.962 s
        // 951.8 ms
//        calcTime(() -> {
//            zeroCopyDemo.normal();
//        });
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " prepared" + finalI);
            });
        }
        Thread.sleep(2000);
        calcTime(() -> {
                    for (int i = 0; i < 5; i++) {
                        executorService.submit(() -> {
                            calcTime(zeroCopyDemo::zeroCopy);
                        });
                    }
                }
        );
    }

    public static void calcTime(Runnable runnable) {
        Stopwatch started = Stopwatch.createStarted();
        runnable.run();
        started.stop();
        String s = started.toString();
        System.out.println(s);
    }

    public void normal() {
        for (int i = 0; i < writeTimes; i++) {

            File file = new File("build/io/" + i + "bigData.normal.txt");
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(bytes, 0, bytes.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void zeroCopy() {

        for (int i = 0; i < writeTimes; i++) {
            try {
                File file = new File("build/io/" + i + "big.zero.txt");
                FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
                MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, bytes.length);
                map.put(bytes);
                fileChannel.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
