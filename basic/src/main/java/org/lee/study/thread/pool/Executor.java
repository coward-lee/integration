package org.lee.study.thread.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface Executor {
    <T> Future<T> submit(Supplier<T> task);
    void execute(Runnable runnable);
}
