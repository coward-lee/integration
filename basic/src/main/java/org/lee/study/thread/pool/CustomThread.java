package org.lee.study.thread.pool;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomThread extends Thread{
    private final AtomicInteger atomicInteger;

    public CustomThread(Runnable target, AtomicInteger atomicInteger) {
        super(target);
        this.atomicInteger = atomicInteger;
    }


    @Override
    public void run() {
        atomicInteger.incrementAndGet();
        System.out.println(Thread.currentThread().getName()+" ： 执行线程？ " + atomicInteger.get());
        super.run();
        atomicInteger.decrementAndGet();
        System.out.println(Thread.currentThread().getName()+" ： 线程结束？" + atomicInteger.get());

    }
}
