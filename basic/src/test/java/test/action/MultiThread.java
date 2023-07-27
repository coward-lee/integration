package test.action;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class MultiThread {

    @Test
    void test_thread_pool() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 10, TimeUnit.HOURS, new ArrayBlockingQueue<>(2), new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < 30; i++) {
            executor.submit(() -> {
                return len();
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }


    int len() {
        int _1MB = 1024 * 1024;
        try {
            int len = 0;
            while (true) {
                byte[] b1 = new byte[_1MB];
                len = b1.length;
                if (len == -1){
                    return len;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  验证
     *  1. 超过keepalive time 之后非核心线程线程是否存活，
     *  2. 通过配置允许核心线程也在过期之后 被kill掉
     *
     * @throws InterruptedException
     */
    @Test
    void test_thread_pool1() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 6, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2),new ThreadPoolExecutor.CallerRunsPolicy());
        executor.allowCoreThreadTimeOut(true); // 通过配置允许核心线程也在过期之后 被kill掉
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                return len2();
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }


    int len2() {
        int _1MB = 1024 * 1024;
        try {
            int len = 0;
            System.out.println(Thread.currentThread().getName()+" executing");
            Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName()+" finshed");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

}
