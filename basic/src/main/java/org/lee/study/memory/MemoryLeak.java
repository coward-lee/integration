package org.lee.study.memory;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class MemoryLeak {

    Long sum = 0l;

    public long addIncremental(long l) {
//        Long sum = 0l;
        sum = sum + 1;
        return sum;
    }

    /**
     * 会造成泄漏？
     *
     * @throws InterruptedException
     */
    public static void test_box_leak() throws InterruptedException {
        MemoryLeak memoryLeak = new MemoryLeak();
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            memoryLeak.addIncremental(i);
        }
        System.out.println("执行完成");
        Thread.sleep(10000);
    }

    public static void test_box_non_leak() throws InterruptedException {
        long s = 0;
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            Long j = Long.valueOf(i);
            j = j + i;
            s = i % 2 == 0 ? j : i;
        }
        System.out.println(s);
        System.out.println("执行完成");
        Thread.sleep(10000);
    }

    public static void text_leak01() {
        DemoPojo demoPojo = new DemoPojo(new byte[(1 << 20) * 100]);
        DemoPojo1 demoPojo1 = new DemoPojo1(new byte[(1 << 20) * 100]);
        demoPojo.setDemoPojo1(demoPojo1);
        demoPojo1.setDemoPojo(demoPojo);
        System.out.println(demoPojo + "  " + demoPojo1);
    }

    public static void test_weak_refernce() {
        new Thread(() -> {
            try {
                DemoPojo demoPojo = new DemoPojo(new byte[(1 << 20) * 80]);
                DemoPojo1 demoPojo1 = new DemoPojo1(new byte[(1 << 20) * 80]);
                System.out.println("已经分配");
                System.in.read();
                WeakReference<DemoPojo> weak1 = new WeakReference<>(demoPojo);
                WeakReference<DemoPojo1> weak2 = new WeakReference<>(demoPojo1);
                demoPojo = null;
                demoPojo1 = null;
                System.out.println("若引用测试，请执行gc");
                System.in.read();
                System.out.println(weak1.get());
                System.out.println(weak2);
//        System.in.read();

                Thread.sleep(Integer.MAX_VALUE);
                System.out.println("执行完成");
            } catch (Throwable t) {
            }
        }).start();
    }


    /**
     * 内存泄漏测试
     */
    static class LocalVariable {
        //总共有5M
        private byte[] locla = new byte[1024 * 1024 * 5];
    }

    // (1)创建了一个核心线程数和最大线程数为 6 的线程池，这个保证了线程池里面随时都有 6 个线程在运行
    final static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(6, 6, 1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>());
    static ThreadLocal<LocalVariable> localVariable = new ThreadLocal<LocalVariable>();


    public static void test_memory_leak() throws InterruptedException, IOException {
        System.in.read();
        System.out.println("开始执行");
        // (3)向线程池里面放入 50 个任务
        for (int i = 0; i < 50; ++i) {
            poolExecutor.execute(() -> {
                // (4) 往threadLocal变量设置值
                LocalVariable localVariable = new LocalVariable();
                // 会覆盖
                MemoryLeak.localVariable.set(localVariable);
                // (5) 手动清理ThreadLocal
                System.out.println("thread name end：" + Thread.currentThread().getName() + ", value:"+ MemoryLeak.localVariable.get());
//                    ThreadLocalOutOfMemoryTest.localVariable.remove();

            });

            Thread.sleep(10);
        }


        // (6)是否让key失效，都不影响。只要持有的线程存在，都无法回收。
        //ThreadLocalOutOfMemoryTest.localVariable = null;
        System.out.println("pool execute over");
        Thread.sleep(1000000);
    }
}
