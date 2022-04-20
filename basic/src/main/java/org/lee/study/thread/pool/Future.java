package org.lee.study.thread.pool;

public interface Future<T> {
    T get();
    void onSuccess();
    void onFailure();
    void onException();
}
