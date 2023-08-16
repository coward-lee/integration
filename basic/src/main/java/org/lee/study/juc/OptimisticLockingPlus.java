package org.lee.study.juc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicStampedReference;

public class OptimisticLockingPlus {
    private static final int THREAD_COUNT = 10;

    private static long valueOffset;
    public volatile int value0 = 0;
    public volatile int value = 0;


    private static final Unsafe unsafe;
    static {
        Field f;
        try {
            f = Unsafe.class.getDeclaredField("theUnsafe");
        } catch (NoSuchFieldException var3) {
            throw new RuntimeException(var3);
        }

        f.setAccessible(true);

        try {
            unsafe = (Unsafe)f.get(null);
        } catch (IllegalAccessException var2) {
            throw new RuntimeException(var2);
        }
    }
    public static final AtomicLong failure = new AtomicLong(0);

    static {
        try {
            valueOffset = unsafe.objectFieldOffset(OptimisticLockingPlus.class.getField("value"));
            System.out.println("valueOffset:" + valueOffset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final boolean unsafeCompareAndSet(int oldValue, int newValue) {
        return unsafe.compareAndSwapInt(this, valueOffset, oldValue, newValue);
    }

    public void selfPlus() {
        int oldValue;
        int i = 0;
        do {
            oldValue = value;
            if (i++ > 1) {
                failure.incrementAndGet();
            }
        } while (!unsafeCompareAndSet(oldValue, oldValue + 1));
    }

    public static void main(String[] args) throws InterruptedException {
        int threadCount = THREAD_COUNT;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        OptimisticLockingPlus plus = new OptimisticLockingPlus();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor.execute(()->{
                for (int j = 0; j < 1000; j++) {
                    plus.selfPlus();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println("value is :"+plus.value);
        System.out.println("failure is :"+plus.failure.get());
        AtomicStampedReference<Object> objectAtomicStampedReference = new AtomicStampedReference<>(new Object(), 1);
//        objectAtomicStampedReference.compareAndSet();
        System.exit(0);
    }
}
