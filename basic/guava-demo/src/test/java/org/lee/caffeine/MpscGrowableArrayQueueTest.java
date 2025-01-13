package org.lee.caffeine;

import org.jctools.queues.MpscGrowableArrayQueue;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MpscGrowableArrayQueueTest {
    ExecutorService executors;

    int nThreads = Runtime.getRuntime().availableProcessors();

    {
        int nThreads = Runtime.getRuntime().availableProcessors();
        executors = Executors.newFixedThreadPool(nThreads);
    }

    /**
     * 16514
     * @throws InterruptedException
     */
    @Test
    void test_mpsc_queue() throws InterruptedException {
        CountDownLatch count = new CountDownLatch(nThreads);
        int i1 = 1 << 24;
        int total = 1 << 28;
        MpscGrowableArrayQueue<Object> queue = new MpscGrowableArrayQueue<>(total);
        long start = System.currentTimeMillis();
        for (int j = 0; j < nThreads; j++) {
            executors.execute(() -> {
                for (int i = 0; i < i1; i++) {
                    queue.add("xxx");
                }
                count.countDown();
                ;
            });
        }
        for (int i = 0; i < total * nThreads; i++) {
            queue.poll();
        }
        count.await();
        System.out.println(String.format("cost:%s", System.currentTimeMillis() - start));
    }

    @Test
    void test_mpsc_queue_for_offer() throws InterruptedException {
        CountDownLatch count = new CountDownLatch(nThreads);
        int i1 = 1 << 16;
        int total = 1 << 16;
        MpscGrowableArrayQueue<Long> queue = new MpscGrowableArrayQueue<>(total);
        for (int i = 0; i < i1; i++) {
            queue.add(Long.MAX_VALUE);
        }
    }

    /**
     * 17089
     * @throws InterruptedException
     */
    @Test
    void test_mpsc_queue1() throws InterruptedException {
        CountDownLatch count = new CountDownLatch(nThreads);
        int i1 = 1 << 19;
        int total = 1 << 26;
        try {
            ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(total);
            long start = System.currentTimeMillis();
            for (int j = 0; j < nThreads; j++) {
                executors.execute(() -> {
                    for (int i = 0; i < i1; i++) {
                        queue.add("xxx");
                    }
                    count.countDown();
                    ;
                });
            }
            for (int i = 0; i < total * nThreads; i++) {
                queue.poll();
            }
            count.await();
            System.out.println(String.format("cost:%s", System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
