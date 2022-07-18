# commonPool
多核cpu的情况下默认，CompletableFuture和 parallelStream 使用的同一个线程池，默认的线程池数量为核心数量
CompletableFuture.ASYNC_POOL
ForkJoinPool ForkJoinPool.common;
```java
class ForkJoinPool{

    public final ForkJoinTask<V> fork() {
        Thread t; ForkJoinWorkerThread w;
        if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread)
            (w = (ForkJoinWorkerThread)t).workQueue.push(this, w.pool);
        else
            ForkJoinPool.common.externalPush(this);
        return this;
    }

}


```








