package org.lee.study.thread.pool;


public class ThreadFactory{

    public Thread newThread(Runnable r) {
        return new Thread(r);
    }
}
