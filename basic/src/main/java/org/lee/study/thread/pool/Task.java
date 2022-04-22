package org.lee.study.thread.pool;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Task implements Runnable {

    private Runnable actualTask;
    private final Thread thread;
    private final BlockingQueue<Runnable> newTask;
    private final List<Task> idleWorkers;
    private final AtomicInteger compeletedTaskCount;

    public Task(Runnable actualTask, ThreadPool pool) {
        this.actualTask = actualTask;
        this.idleWorkers = pool.getWorkers();
        this.thread = ThreadFactory.newThread(this);
        this.newTask = pool.getNewTasks();
        this.compeletedTaskCount = pool.getCompeletedTaskCount();

    }

    public void start() {
        if (thread != null) {
            thread.start();
            return;
        }
        throw new ThreadPoolException("worker has not thread to use");
    }


    @Override
    public void run() {
        run(actualTask);
    }

    private void run(Runnable runnable) {
        Runnable task = runnable;
        actualTask = null;
        try {
            while (task != null || (task = getNewTask()) != null) {
                runBefore();
                task.run();
                runAfter();
                task = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private Runnable getNewTask() {
        try {
            return newTask.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return null;
    }

    private void runBefore() {
        System.out.println("[" + Thread.currentThread().getName() + "  " + LocalDateTime.now() + "]:" + "开始开始开始, 当前任务数量 :【" + idleWorkers.size() + "】");
    }

    private void runAfter() throws InterruptedException {
        idleWorkers.add(this);
        System.out.println("[" + Thread.currentThread().getName() + "  " + LocalDateTime.now() + "]:" + "完成完成完成, 当前任务数量 :【" + idleWorkers.size() + "】, 完成数量【" + compeletedTaskCount.incrementAndGet() + "】");
    }


}
