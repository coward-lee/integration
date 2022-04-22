package org.lee.study.thread.pool;


public class ThreadFactory{
    private static int count = 0;
    public static Thread newThread(Runnable r) {
        return new Thread(r, "lee-thread-pool-"+ ++count);
    }
}
