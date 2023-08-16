package test.action.juc;

import org.junit.jupiter.api.Test;
import org.lee.study.util.ThreadUtil;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {


    @Test
    void test_condition(){
        final ReentrantLock reentrantLock = new ReentrantLock();
        final Condition c1 = reentrantLock.newCondition();
        final Condition c2 = reentrantLock.newCondition();

        for (int i = 0; i < 10; i++) {
            Thread ddd = new Thread(() -> {
                try {
                    reentrantLock.lock();
                    System.out.println("thread " + Thread.currentThread().getName() + ":" + "start");
                    System.out.println(c2.hashCode());
                    c1.await();
                    System.out.println("thread " + Thread.currentThread().getName() + ":" + "finished");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    reentrantLock.unlock();
                }
            }, "c1___"+i);
            ddd.start();
        }
        for (int i = 0; i < 10; i++) {
            final int fi = i;
            Thread ddd = new Thread(() -> {
                try {
                    reentrantLock.lock();
                    System.out.println("thread " + Thread.currentThread().getName() + ":" + "started"+fi);
                    System.out.println(c1.hashCode());
                    c2.await();
                    System.out.println("thread " + Thread.currentThread().getName() + ":" + "finished");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    reentrantLock.unlock();
                }
            }, "c2___"+i);
            ddd.start();
        }


        ThreadUtil.sleep(1000);
        ThreadUtil.sleep(10000000);

    }
}
