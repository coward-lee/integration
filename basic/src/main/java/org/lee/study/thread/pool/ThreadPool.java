package org.lee.study.thread.pool;

import org.lee.study.thread.pool.async.Callable;
import org.lee.study.thread.pool.async.Future;
import org.lee.study.thread.pool.async.FutureTask;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;


public class ThreadPool implements Executor {

    private final Lock lock = new ReentrantLock();
    private final AtomicInteger workerCount = new AtomicInteger(0);


    private final AtomicInteger compeletedTaskCount = new AtomicInteger(0);


    private List<Task> workers;
    // 核心线程
    private final Integer coreSize;
    // 线程队列数量
    private final Integer queueSize;
    // 线程队列
    private final AbstractQueue<Task> idleWorkers = new LinkedBlockingQueue<>();

    private final BlockingQueue<Runnable> newTasks = new ArrayBlockingQueue<>(32);

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
        workers = new ArrayList<>(coreSize);
        System.out.println("线程初始化完成，核心线程数量：" + coreSize + "  线程队列" + queueSize);
    }


    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        FutureTask<T> tFutureTask = new FutureTask<>(callable);
        execute(tFutureTask);
        return tFutureTask;
    }


    @Override
    public void execute(Runnable runnable) {
        doExecute(runnable);
    }

    private void doExecute(Runnable runnable) {
        boolean shouldRun = false;

        lock.lock();
        Task worker = idleWorkers.poll();

        try {
            if (worker != null) {
                newTasks.put(runnable);
            } else {
                worker = new Task(runnable, this);
            }

            boolean addedWork = addWork(worker);
            if (addedWork) {
                shouldRun = true;
            } else {
                System.out.println("被拒绝了嘛");
                newTasks.put(runnable);
            }
            if (shouldRun) {
                workers.add(worker);
            }
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        if (shouldRun) {
            worker.start();
        }
    }

    private boolean addWork(Task worker) {
        int count = workerCount.get();

        if (workers.size() >= coreSize) {
            return false;
        }
        int curCount;
        while ((curCount = workerCount.incrementAndGet()) == count) {
            count = workerCount.get();
            if (curCount >= coreSize) {
                return false;
            }
        }
        workers.add(worker);
        return true;
    }


    public List<Task> getWorkers() {
        return workers;
    }

    public BlockingQueue<Runnable> getNewTasks() {
        return newTasks;
    }

    public AtomicInteger getCompeletedTaskCount() {
        return compeletedTaskCount;
    }
}
