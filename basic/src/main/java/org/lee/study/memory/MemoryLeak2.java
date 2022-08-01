package org.lee.study.memory;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 为什么自定义的线程池没有释放内存
 * 而commonPool 中的线程池释放的threadLocal 内容
 */
public class MemoryLeak2 {
    // (1)创建了一个核心线程数和最大线程数为 6 的线程池，这个保证了线程池里面随时都有 6 个线程在运行
    final static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(6, 6, 1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>());


    public static void test_memory_leak() throws InterruptedException, IOException {

        System.in.read();
        System.out.println("开始执行");
        // (3)向线程池里面放入 50 个任务
        IntStream.range(1, 50).forEach(i -> {
            poolExecutor.execute(() -> {
                ThreadLocal<DemoPojo> finalLocalVariable = new ThreadLocal<>();
                // (4) 往threadLocal变量设置值
                DemoPojo demoPojo = new DemoPojo(new byte[(1 << 20) * 100]);
                // 会覆盖
                finalLocalVariable.set(demoPojo);
                // (5) 手动清理ThreadLocal
                System.out.println("thread name end：" + Thread.currentThread().getName() + ", value:" + finalLocalVariable.get());
                finalLocalVariable = null;

            });
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        // (6)是否让key失效，都不影响。只要持有的线程存在，都无法回收。

        System.out.println("pool execute over");
        Thread.sleep(1000000);
    }

    static ThreadLocal<DemoPojo> finalLocalVariable = new ThreadLocal<>();

    public static void main(String[] args) throws IOException, InterruptedException {

        System.in.read();
        System.out.println("开始执行");

        IntStream.range(1, 50).parallel().forEach((i) -> {
            // (4) 往threadLocal变量设置值
            DemoPojo demoPojo = new DemoPojo(new byte[(1 << 20) * 100]);
            // 会覆盖
            finalLocalVariable.set(demoPojo);
            // (5) 手动清理ThreadLocal
            System.out.println("thread name end：" + Thread.currentThread().getName() + ", value:" + finalLocalVariable.get());

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            };
        });
        finalLocalVariable = null;

        System.out.println("pool execute over");
        Thread.sleep(1000000);
    }
}
