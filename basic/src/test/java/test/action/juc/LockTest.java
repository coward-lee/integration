package test.action.juc;

import org.junit.jupiter.api.Test;
import org.lee.study.util.PrintUtil;
import org.lee.study.util.ThreadUtil;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockTest {



    @Test
    void test_with_reentrant_lock() throws InterruptedException {
        // 重入锁是一个 exclusive 锁，他不是一个共享锁，不支持共享锁，不支持偏向锁


        ReentrantLock lock = new ReentrantLock();
        Thread t3 = new Thread(() -> {
            PrintUtil.print("started");
            lock.lock();
            PrintUtil.print("got lock");
            ThreadUtil.sleep(1000);
            lock.unlock();
            PrintUtil.print("end");
        }," lock 1");
        Thread t4 = new Thread(() -> {
            PrintUtil.print("started");
            lock.lock();
            PrintUtil.print("got lock");
            ThreadUtil.sleep(1000);
            lock.unlock();
            PrintUtil.print("end");
        }," lock 2");
        t3.start();
        t4.start();

        new Thread(()->{
            PrintUtil.print(" t3 state:"+t3.getState());
            PrintUtil.print(" t4 state:"+t4.getState());
        }).start();
        t3.join();
        t4.join();
        PrintUtil.print("end");
    }
    @Test
    void test_with_shared() throws InterruptedException {

        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        Thread t1 = new Thread(() -> {
            PrintUtil.print("started");
            ThreadUtil.sleep(1000);
            readLock.lock();
            PrintUtil.print("end");
        },"t1");
        Thread t2 = new Thread(() -> {
            PrintUtil.print("started");
            ThreadUtil.sleep(1000);
            readLock.lock();
            PrintUtil.print("end");
        },"t2");
        Thread t3 = new Thread(() -> {
            PrintUtil.print("started");
            writeLock.lock();
            ThreadUtil.sleep(1000);
            writeLock.unlock();
            PrintUtil.print("end");
        },"write lock 1");
        Thread t4 = new Thread(() -> {
            PrintUtil.print("started");
            writeLock.lock();
            ThreadUtil.sleep(1000);
            writeLock.unlock();
            PrintUtil.print("end");
        },"write lock 2");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        PrintUtil.print("end");
    }
}
