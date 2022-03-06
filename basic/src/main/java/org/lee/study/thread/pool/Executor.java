package org.lee.study.thread.pool;

public interface Executor {
    void addTask(Runnable runnable);
    void addTask(Thread thread);
}
