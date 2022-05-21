package org.lee.study.thread.pool;

import org.lee.study.thread.pool.async.Callable;
import org.lee.study.thread.pool.async.Future;

import java.util.function.Supplier;

public interface Executor {
    <T> Future<T> submit(Callable<T> task);
    void execute(Runnable runnable);
}
