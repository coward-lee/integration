package org.lee.study.thread.pool;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class ThreadMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ThreadPool threadPool = new ThreadPool(10, 10);
//
//        for (int i = 0; i < 1000; i++) {
//            threadPool.execute(() -> {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//
////            try {
////                Thread.sleep(11);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
//        }

        Worker worker = new Worker(() -> {
            try {
                System.out.println("开始执行  1111");
                Thread.sleep(1);
                System.out.println("执行完成  1111");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread = worker.thread;
        thread.start();

        for (int i = 0; i < 100; i++) {
            int finalI = (i + 1);
            System.out.println("加入了任务：" + (i + 1));
            worker.addTask(() -> {
                try {
                    System.out.println("开始执行  " + finalI);
                    Thread.sleep(100);
                    System.out.println("执行完成  " + finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        Thread.sleep(1000);


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
            System.out.println("加入了任务：" + i);
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


    static class Worker implements Runnable {

        private Runnable actualTask;
        private final ArrayBlockingQueue<Runnable> tasks = new ArrayBlockingQueue<>(999);
        private final Thread thread;
        int count = 0;

        public Worker(Runnable actualTask) {
            this.actualTask = actualTask;
            this.thread = new Thread(this);
        }

        @Override
        public void run() {
            try {
                doRun();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void doRun() throws InterruptedException {
            Runnable task = this.actualTask;
            this.actualTask = null;
            while (task != null || (task = getNewTask()) != null) {
                task.run();
                System.out.println("执行任务 " + count++);
                task = null;
            }
        }

        private Runnable getNewTask() throws InterruptedException {
            return tasks.take();
        }

        public void addTask(Runnable runnable) throws InterruptedException {
            tasks.put(runnable);
        }


        public void setActualTask(Runnable actualTask) {
            this.actualTask = actualTask;
        }
    }
}
