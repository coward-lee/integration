package org.lee.study.thread.pool;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool implements Executor{
    private final ThreadFactory threadFactory = new ThreadFactory();
    private final AtomicInteger workerCount = new AtomicInteger(0);

    public void submit(Runnable runnable) {
        threadFactory.newThread(new Worker(runnable)).start();
    }

    @Override
    public void execute(Runnable runnable) {
        submit(runnable);
    }


    class Worker implements Runnable, Serializable {

        final Runnable actualTask;

        public Worker(Runnable actualTask) {
            this.actualTask = actualTask;
        }

        @Override
        public void run() {
            run(actualTask);
        }

        void run(Runnable runnable) {
            workerCount.incrementAndGet();
            System.out.println("[" + Thread.currentThread().getName() + "]:" + (workerCount.get() + 1) + "开始执行");
            actualTask.run();
            workerCount.decrementAndGet();
            System.out.println("[" + Thread.currentThread().getName() + "]:" + (workerCount.get() + 1) + "执行完成");
        }
    }

}
