package org.lee.study.thread.pool;

public interface Executor {
    void execute(Runnable runnable);
}
