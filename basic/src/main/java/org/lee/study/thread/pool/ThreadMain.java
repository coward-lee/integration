package org.lee.study.thread.pool;


import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ThreadMain {
    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool();
        IntStream.range(1,10).forEach(i->{
            threadPool.submit(()->{
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
//        test();
    }
    public static void test() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10,
                10,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
        );
        threadPoolExecutor.execute(() -> {
                    try {
                        System.out.println("xxxxxxx");
                        Thread.sleep(1000L);
                        System.out.println(Thread.currentThread().getName() + "执行完成 " + threadPoolExecutor.getActiveCount());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        Thread.sleep(3000);

    }
}
