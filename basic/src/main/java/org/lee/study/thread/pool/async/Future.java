package org.lee.study.thread.pool.async;

public interface Future<T> {

    T get() throws InterruptedException;
    void onSuccess(T data);
    void onFailure();
    void onException();
}
