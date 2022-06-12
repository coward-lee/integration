package org.lee.study.thread.pool.async;
@FunctionalInterface
public interface Callable<T> {
    T get() throws InterruptedException;
}
