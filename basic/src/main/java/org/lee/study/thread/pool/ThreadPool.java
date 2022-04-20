package org.lee.study.thread.pool;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class ThreadPool implements Executor {
    private final Lock lock = new ReentrantLock();
    private final ThreadFactory threadFactory = new ThreadFactory();
    private final AtomicInteger workerCount = new AtomicInteger(0);
    private long completedNum = 0;
    private long rejectNum = 0;

    private final List<Worker> workers = new LinkedList<>();
    // 核心线程
    private final Integer coreSize;
    // 线程队列数量
    private final Integer queueSize;
    // 线程队列
    private final AbstractQueue<Worker> idleQueue = new LinkedBlockingQueue<>();
    private final AbstractQueue<Worker> emptyTaskQueue = new LinkedBlockingQueue<>();

    public ThreadPool() {
        coreSize = 1;
        queueSize = 0;
        init();
    }

    public ThreadPool(Integer coreSize) {
        this.coreSize = coreSize;
        queueSize = 0;
        init();
    }

    public ThreadPool(Integer coreSize, Integer queueSize) {
        this.coreSize = coreSize;
        this.queueSize = queueSize;
        init();
    }

    private void init() {

    }


    public <T> Future<T> submit(Supplier<T> supplier) {
        return null;
    }


    @Override
    public void execute(Runnable runnable) {
        doExecute(runnable);
    }

    private void doExecute(Runnable runnable) {
        Worker worker = emptyTaskQueue.poll();
        if (worker != null) {
            worker.setActualTask(runnable);
        }
        worker = new Worker(runnable);
        boolean shouldRun = true;
        lock.lock();
        if (workers.size() >= coreSize) {
            shouldRun = false;
            // todo 放入等待队列
            if (idleQueue.size() >= queueSize) {
                // todo 执行拒绝策略
                System.out.println("执行了拒绝策略[" + (++rejectNum) + "]");
            } else {
                idleQueue.add(worker);
            }
        }
        workers.add(worker);
        lock.unlock();
        if (shouldRun) {
            worker.start();
        }
    }


    class Worker implements Runnable, Serializable {

        private Runnable actualTask;
        private final Thread thread;

        public Worker(Runnable actualTask) {
            this.actualTask = actualTask;
            this.thread = threadFactory.newThread(this, ++completedNum);
        }

        private void start() {
            if (thread != null) {
                thread.start();
                return;
            }
            throw new RuntimeException("worker has not thread to use");
        }


        @Override
        public void run() {
            run(actualTask);
        }

        private void run(Runnable runnable) {
            runBefore();
            runnable.run();
            runAfter();
        }

        private void runBefore() {
            workerCount.incrementAndGet();
            System.out.println("[" + Thread.currentThread().getName() + "  " + LocalDateTime.now() + "]:" + (workerCount.get() + 1) + "开始执行");
        }

        private void runAfter() {
            workerCount.decrementAndGet();
            System.out.println("[" + Thread.currentThread().getName() + "  " + LocalDateTime.now()  + "]:" + (workerCount.get() + 1) + "执行完成");
            emptyTaskQueue.add(this);
            workers.remove(this);
        }

        public void setActualTask(Runnable actualTask) {
            this.actualTask = actualTask;
        }
    }

}
