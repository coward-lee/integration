package test.action.juc;

import org.junit.jupiter.api.Test;
import org.lee.study.ObjectLock;
import org.lee.study.util.JvmUtil;
import org.lee.study.util.ThreadUtil;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.util.concurrent.CountDownLatch;

public class SynchronizedKeyWordTest {

    @Test
    void test_demo() {

        String printable = ClassLayout.parseInstance(new ObjectLock()).toPrintable();
        System.out.println(printable);
        System.out.println(VM.current().details());
    }

    @Test
    void biasedLock() throws InterruptedException {
        ObjectLock objectLock = new ObjectLock();
        CountDownLatch count = new CountDownLatch(1);
        Thread.sleep(5000);

        System.out.println("=================================no lock====================================");
        JvmUtil.jvmObject(objectLock);
        System.out.println("=================================no lock====================================");
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (objectLock) {
                    objectLock.inc();
                    System.out.println("thread " + Thread.currentThread().getName() + ":" + Integer.toHexString(Thread.currentThread().hashCode() << 10));
                    System.out.println("thread " + Thread.currentThread().getName() + ":" + Integer.toHexString(Thread.currentThread().hashCode()));
                    JvmUtil.jvmObject(objectLock);
                }
            }
            count.countDown();
        }, "thread-1").start();
        count.await();
        System.out.println("threadid:" + Integer.toHexString(Thread.currentThread().hashCode() << 10));
        JvmUtil.jvmObject(objectLock);
    }

    @Test
    void lightLock() throws InterruptedException {
        ObjectLock objectLock = new ObjectLock();
        CountDownLatch count = new CountDownLatch(2);

        Thread.sleep(5000);

        System.out.println("=================================no lock====================================");
        JvmUtil.jvmObject(objectLock);
        System.out.println("=================================no lock====================================");
        Runnable runnable = () -> {
            for (int i = 0; i < 5; i++) {
                synchronized (objectLock) {
                    objectLock.inc();
                    System.out.println("thread " + Thread.currentThread().getName() + ":" + Integer.toHexString(Thread.currentThread().hashCode() << 10));
                    System.out.println("thread " + Thread.currentThread().getName() + ":" + Integer.toHexString(Thread.currentThread().hashCode()));
                    JvmUtil.jvmObject(objectLock);
                }
            }


            System.out.println("thread " + Thread.currentThread().getName() + ":" + "finished");
            System.out.println("thread " + Thread.currentThread().getName() + ":" + Integer.toHexString(Thread.currentThread().hashCode() << 10));
            System.out.println("thread " + Thread.currentThread().getName() + ":" + Integer.toHexString(Thread.currentThread().hashCode()));
            JvmUtil.jvmObject(objectLock);
            count.countDown();
            for (; ; ) {
                ThreadUtil.sleep(1);
            }
        };
        Runnable runnable2 = () -> {
            for (int i = 0; i < 5; i++) {
                synchronized (objectLock) {
                    objectLock.inc();
                    System.out.println("thread " + Thread.currentThread().getName() + ":" + Integer.toHexString(Thread.currentThread().hashCode() << 10));
                    System.out.println("thread " + Thread.currentThread().getName() + ":" + Integer.toHexString(Thread.currentThread().hashCode()));
                    JvmUtil.jvmObject(objectLock);
                }
                ThreadUtil.sleep(3);
            }
            count.countDown();
        };
        new Thread(runnable, "thread-1").start();
        ThreadUtil.sleep(1000);
        new Thread(runnable2, "thread-2").start();

        count.await();
        System.out.println("threadid:" + Integer.toHexString(Thread.currentThread().hashCode() << 10));
        JvmUtil.jvmObject(objectLock);

    }

    @Test
    void test_object_wait() throws InterruptedException {
        Object lock = new Object();
        new Thread(() -> {
            synchronized (lock) {
                System.out.println("thread " + Thread.currentThread().getName() + ":" + "started");
                try {
                    lock.wait();
                    System.out.println("thread " + Thread.currentThread().getName() + ":" + " wait finished");

                    ThreadUtil.sleep(2000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                ThreadUtil.sleep(2000);
                System.out.println("thread " + Thread.currentThread().getName() + ":" + "finished");
            }
        }, "t-1").start();
        new Thread(() -> {
            ThreadUtil.sleep(1000);
            System.out.println("thread " + Thread.currentThread().getName() + ":" + "prepare");
            synchronized (lock) {
                System.out.println("thread " + Thread.currentThread().getName() + ":" + "started");
                lock.notify();
                System.out.println("thread " + Thread.currentThread().getName() + ":" + "notify");

                ThreadUtil.sleep(2000);
                System.out.println("thread " + Thread.currentThread().getName() + ":" + "finished");
            }
        }, "t-2").start();
        System.out.println("thread " + Thread.currentThread().getName() + ":" + "started");
        ThreadUtil.sleep(1000);

        ThreadUtil.sleep(10000);
        System.out.println("thread " + Thread.currentThread().getName() + ":" + "started");
    }

    @Test
    void test_object_wait_1(){

        Object lock = new Object();
        new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t-1").start();
        new Thread(() -> {
            synchronized (lock) {
                lock.notify();
            }
        }, "t-2").start();

    }
}


