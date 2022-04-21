package org.lee.study.thread.pool;

import java.io.Serializable;
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
    private long completedNum = 1;
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
        boolean shouldRun = false;
        lock.lock();
        Worker worker = emptyTaskQueue.poll();
        if (worker != null) {
            worker.setActualTask(runnable);
        } else {
            worker = new Worker(runnable);
        }

        boolean addedWork = addWork(worker);
        if (addedWork){
            shouldRun = true;
        }else{
            runnable.run();
        }
        lock.unlock();

        if (shouldRun) {
            worker.start();
        }
    }

    private boolean addWork(Worker worker) {
        int count = workerCount.get();

        if (workers.size() >= coreSize) {
            return false;
        }
        int curCount;
        while ((curCount = workerCount.incrementAndGet()) == count) {
            count = workerCount.get();
            if (curCount >= coreSize){
                return false;
            }
        }
        workers.add(worker);
        return true;
    }


    class Worker implements Runnable, Serializable {

        private Runnable actualTask;
        private final Thread thread;

        public Worker(Runnable actualTask) {
            this.actualTask = actualTask;
            this.thread = threadFactory.newThread(this, completedNum);
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

        private void runBefore() {}

        private void runAfter() {
            System.out.println("[" + Thread.currentThread().getName() + "  " + LocalDateTime.now() + "]:" + (workerCount.get() + 1) + "执行完成");
            workers.remove(this);
            System.out.println("工作线程数量： " + workers.size());
//            completedNum++;
            System.out.println("执行完成的队列数量：[" + emptyTaskQueue.size() + "]");
            thread.interrupt();
            emptyTaskQueue.add(this);
        }

        public void setActualTask(Runnable actualTask) {
            this.actualTask = actualTask;
        }
    }

}
