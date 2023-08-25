package test.action.juc;

import org.junit.jupiter.api.Test;
import org.lee.study.util.ThreadUtil;

import java.util.concurrent.locks.LockSupport;

public class ThreadDemo {

    @Test
    void test(){
        Thread demo = new Thread(() -> {
            System.out.println("d1 started");
            LockSupport.park();
            System.out.println("d1 end");
        }, "demo");
        demo.start();
        ThreadUtil.sleep(100);
        new Thread(()->{
            System.out.println("d2 started");
            LockSupport.unpark(demo);
            System.out.println("d2 started");
        },"demo").start();

        ThreadUtil.sleep(200);
    }
    @Test
    void test_with_interrupt(){
        Thread demo = new Thread(() -> {
            System.out.println("d1 started");
            ThreadUtil.sleep(1000);
            System.out.println("d1 end");
        }, "demo");
        demo.start();
        demo.interrupt();
    }
}
