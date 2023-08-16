package test.action.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockDemo {


    @Test
    void test_read_write(){
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        new Thread(()->{
            lock.readLock().lock();
        });

    }
}
