package org.lee.study.thread.pool;


import java.util.concurrent.*;

public class ThreadMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ThreadPool threadPool = new ThreadPool(10, 10);

        for (int i = 0; i < 100; i++) {
            threadPool.execute(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


//        test();
    }

    public static void test() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10,
                10,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            threadPoolExecutor.submit(() -> {
                try {

                    System.out.println(Thread.currentThread().getName() + ":执行开始" + finalI);
                    Thread.sleep(100);
                    System.out.println(Thread.currentThread().getName() + ":执行结束" + finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
