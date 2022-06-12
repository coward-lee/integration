package org.lee.study.thread.pool.async;

import java.util.concurrent.atomic.AtomicBoolean;

public class FutureTask<T> implements Future<T>, Runnable {

    private final Callable<T> callable;
    AtomicBoolean isFinished = new AtomicBoolean(false);
    Thread thread;
    Fun<T> fun;

    public FutureTask(Callable<T> callable) {
        this.callable = callable;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }


    T result;

    @Override
    public void run() {
        try {
            result = get();
            boolean b = isFinished.get();
            while (!b) {
                b = isFinished.compareAndExchange(false, true);
            }
            onSuccess(result);
            fun.fun(result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public T get() throws InterruptedException {
        T t = callable.get();
        return t;
    }

    @Override
    public void onSuccess(T data) {
        System.out.println("成功了 : "+data);
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onException() {

    }

    public void asyncApply(Fun<T> fun){
        this.fun = fun;
    }



}
